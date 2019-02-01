package dad.javaspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.RotationComponent;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.settings.GameSettings;

import dad.javaspace.interfacing.controller.LauncherController;
import dad.javaspace.objects.EntityTypes;
import dad.javaspace.objects.effects.Animations;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Main extends GameApplication {

	// Interfaz
	Text textPixels;

	// Conectividad
	private String ip = "10.2.2.64", name = "jugador_test", skin = "0";

	private InputStreamReader flujoEntrada;
	private OutputStreamWriter flujoSalida;

	// Mecanica interna
	Entity player = new Entity();
	private ClientModel model = new ClientModel();
	private MediaPlayer mp;
	long coolDown = 0, coolDownStars = 0;
	private boolean canShoot = false;

	RotationComponent rot;
	PositionComponent pos;

	// Estetica
	private ArrayList<Entity> starArray = new ArrayList<>();
	ArrayList<Entity> starList = new ArrayList<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(800);
		settings.setHeight(600);
		settings.setTitle("JavaSpace");
		settings.setFullScreenAllowed(true);
		settings.setVersion(model.getVersion());
		settings.setProfilingEnabled(true);
	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				if (model.getAngular() < 3)
					model.setAngular(model.getAngular() + 0.08);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				if (model.getAngular() > -3)
					model.setAngular(model.getAngular() - 0.08);
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Center") {
			@Override
			protected void onAction() {
				model.setAngular(model.getAngular() * 0.99);
			}
		}, KeyCode.Q);

//		input.addAction(new UserAction("Add thrust") {
//			@Override
//			protected void onAction() {
//				addThrust();
//			}
//
//		}, KeyCode.W);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				makeShoot();
			}
		}, KeyCode.SPACE);

	}

	@Override
	protected void initUI() {
		textPixels = new Text();
		textPixels.setFill(Color.WHITE);
		textPixels.setTranslateX(0);
		textPixels.setTranslateY(20);

		getGameScene().addUINode(textPixels); // add to the scene graph

	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("lives", 3);
	}

	@Override
	protected void initGame() {
		super.initGame();

		LauncherController controller = new LauncherController();
		getGameScene().addUINode(controller.getRootView());
		
		controller.getLaunchButton().setOnAction(e -> {
			getGameScene().clearUINodes();
			metodoKevinInit();
		});

	}

	private void metodoKevinInit() {
		// try {
		// Socket sk;
		//
		// System.out.println("Buscando conexion...");
		//
		// sk = new Socket(ip, 2000);
		//
		// // Espera para que le de tiempo al servidor de mover la conexión a otro
		// puerto
		// Thread.sleep(3000);
		//
		// flujoEntrada = new InputStreamReader(sk.getInputStream(), "UTF-8");
		//
		// flujoSalida = new OutputStreamWriter(sk.getOutputStream(), "UTF-8");
		//
		// flujoSalida.write(name + "," + skin + "\n");
		//
		// flujoSalida.flush();
		//
		// System.out.println("nombre enviado");
		//
		// model.setIdentity(flujoEntrada.read());
		//
		// Scanner input = new Scanner(flujoEntrada);
		//
		// model.setPlayers(input.nextLine().split("_"));
		//
		// System.out.println(model.getPlayers()[0]);
		//
		// System.out.println("id recibida");
		//
		// System.out.println(model.getIdentity());
		//
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		// Estas cuatro lineas se encargan de mover la camara con el jugador, se hace
		// asi para evitar que la camara rote
		double viewWidth = getGameScene().getViewport().getWidth();
		double viewHeight = getGameScene().getViewport().getHeight();
		getGameScene().getViewport().xProperty().bind(player.xProperty().subtract(viewWidth / 2));
		getGameScene().getViewport().yProperty().bind(player.yProperty().subtract(viewHeight / 2));
		player.setRotation(200);
		// Sonido del motor
		mp = new MediaPlayer(new Media(new File("src/main/resources/assets/sounds/thruster.mp3").toURI().toString()));
		mp.setCycleCount(MediaPlayer.INDEFINITE);
		mp.volumeProperty().bind(model.thrustProperty());
		mp.play();

		// Al jugador se le asigna una textura y se agrega al mundo

		player.setViewFromTexture("navePruebaSmall.png");

		getGameWorld().addEntities(player);

		player.setRenderLayer(RenderLayer.TOP);

		getGameScene().setBackgroundColor(Color.BLACK);

		Animations.hiperJumpTransition(player, 1, -Math.sin(Math.toRadians(player.getRotation())) * 100,
				Math.cos(Math.toRadians(player.getRotation())) * 100, getGameWorld());
	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
		textPixels.setText("PosX: " + player.getX() + " PosY: " + player.getY() + "\nForceX: " + model.getxForce()
				+ " ForceY: " + model.getyForce() + "\nFPS: ");

		player.rotateBy(model.getAngular());

		generateStars();

		calcPhysics();

		movePlayer();

		if (getInput().isHeld(KeyCode.W))
			addThrust();
		else
			model.setThrust(model.getThrust() * 0.99);

	}

	/*
	 * Si se está usando el motor de la nave, se aplica la fuerza relativa entre 80
	 * de la impulsion del motor en ambos ejes
	 */
	private void calcPhysics() {
		Double playerRotation = Math.toRadians(player.getRotation());
		double maxForce = 20;
		float x, y;
		if (model.getThrust() != 0) {
			x = (float) (model.getxForce() + (model.getThrust() * Math.sin((playerRotation))));
			y = (float) (model.getyForce() + (model.getThrust() * -Math.cos((playerRotation))));

			model.setxForce(x);
			model.setyForce(y);

			if (model.getxForce() > maxForce)
				model.setxForce(maxForce);

			if (model.getxForce() < -maxForce)
				model.setxForce(-maxForce);

			if (model.getyForce() > maxForce)
				model.setyForce(maxForce);

			if (model.getyForce() < -maxForce)
				model.setyForce(-maxForce);

		}
	}

	private void makeShoot() {
		if (System.currentTimeMillis() >= coolDown + 500) {
			coolDown = System.currentTimeMillis();
			canShoot = true;
//			sendPlayerPosition();
			getAudioPlayer().playSound("laser.mp3");
		}
	}

	private void sendPlayerPosition() {
		try {
			if (canShoot) {
				canShoot = false;
				flujoSalida.write(model.getIdentity() + "," + player.getX() + "," + player.getY() + ","
						+ player.getRotation() + "," + true + "\n");
			} else
				flujoSalida.write(model.getIdentity() + "," + player.getX() + "," + player.getY() + ","
						+ player.getRotation() + "," + false + "\n");

		} catch (IOException e) {
		}
	}

	private void addThrust() {
		if (model.getThrust() < 1)
			model.setThrust(model.getThrust() + 0.005);
	}

	/*
	 * se coge la posicion actual del jugador y directamente se reposiciona sumando
	 * 0.7 veces la fuerza en cada eje
	 */
	private void movePlayer() {
		player.setX(player.getX() + model.getxForce() * 0.7);
		player.setY(player.getY() + model.getyForce() * 0.7);
	}

	/*
	 * genera una estrella cada 80 milisegundos, ademas borra de forma aleatoria
	 * algunas que ya existian de antes
	 */
	private void generateStars() {
		if (System.currentTimeMillis() > coolDownStars + 160) {
			coolDownStars = System.currentTimeMillis();

			Entity newStar = new Entity();
			newStar.setRenderLayer(RenderLayer.BACKGROUND);
			newStar.setViewFromTexture("star_placeholder.png");

			Animations.tinkleTransition(newStar, 50, Math.random(), (Math.random() * 1) + 1,
					(Math.random() * 1.5) + 0.5);
			newStar.setType(EntityTypes.STAR);
			starArray.add(newStar);
			getGameWorld().addEntity(newStar);

			newStar.setX(
					getGameScene().getViewport().getX() + (Math.random() * getGameScene().getViewport().getWidth()));
			newStar.setY(
					getGameScene().getViewport().getY() + (Math.random() * getGameScene().getViewport().getHeight()));

			starList = new ArrayList<>();

			for (Entity entity : starArray) {
				if (Math.random() > 0.8) {
					starList.add(entity);
				}
			}

			starArray.removeAll(starList);

			getGameWorld().removeEntities(starList);

		}
	}

}
