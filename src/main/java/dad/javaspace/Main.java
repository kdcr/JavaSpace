package dad.javaspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.RotationComponent;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.scene.GameScene;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.settings.ReadOnlyGameSettings;

import dad.javaspace.interfacing.controller.LauncherController;
import dad.javaspace.objects.EntityTypes;
import dad.javaspace.objects.effects.Animations;
import dad.javaspace.objects.effects.Componente;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

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
	private LauncherController controller = new LauncherController();
	private MediaPlayer mp;
	long coolDown = 0, coolDownStars = 0;
	private boolean canShoot = false;

	RotationComponent rot;
	PositionComponent pos;

	// Estetica
	private ArrayList<Entity> starArray = new ArrayList<>();
	ArrayList<Entity> starList = new ArrayList<>();

	private GameSettings settings;
	private Stage gameStage = new Stage();

	private PhysicsComponent physics;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {
		
		
		settings.setWidth(controller.getModel().getResolucion().getX());
		settings.setHeight(controller.getModel().getResolucion().getY());
		settings.setTitle("JavaSpace");
		settings.setFullScreenAllowed(controller.getModel().isPantallaCompleta());
		gameStage.setFullScreen(controller.getModel().isPantallaCompleta());
		settings.setVersion(model.getVersion());
		this.settings = settings;
		FXGL.configure(this, settings.toReadOnly(), gameStage);
		
		/**
		 * Las del juego
		 */
//		settings.setWidth((int) Screen.getPrimary().getBounds().getWidth());
//		settings.setHeight((int) Screen.getPrimary().getBounds().getHeight());
//		settings.setTitle("JavaSpace");
//		settings.setFullScreenAllowed(true);
//		settings.setVersion(model.getVersion());
//		this.settings = settings;
//		gameStage.setFullScreen(true);
//		FXGL.configure(this, settings.toReadOnly(), gameStage);

	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				if (model.getAngular() + 0.5 < 3)
					model.setAngular(model.getAngular() + 0.5);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				if (model.getAngular() - 0.5 > -3)
					model.setAngular(model.getAngular() - 0.5);
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Center") {
			@Override
			protected void onAction() {
				model.setAngular(model.getAngular() * 0.8);

			}
		}, KeyCode.Q);

		input.addAction(new UserAction("Add thrust") {
			@Override
			protected void onAction() {
				addThrust();
			}

		}, KeyCode.W);

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

		BorderPane rootView = controller.getRootView();
		getGameScene().addUINode(rootView);

		controller.getLaunchButton().setOnAction(e -> {
			getGameScene().removeUINode(rootView);
			controller.guardarConfig();
			model.setEnPartida(true);
			startGame();
		});

//		startGame();

	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
		if (model.isEnPartida()) {
			textPixels.setText(
					"PosX: " + player.getX() + " PosY: " + player.getY() + "\nVel:" + physics.getLinearVelocity());

			physics.setAngularVelocity(model.getAngular());
			generateStars();

			if (!getInput().isHeld(KeyCode.W))
				model.setThrust(model.getThrust() * 0.80);
			maxVel();
		}
	}

	private void startGame() {
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
		// FIXME arreglar la posicion de la camara para centrar la vista
		getGameScene().getViewport().xProperty()
				.bind(player.xProperty().subtract(viewWidth / 2).add((player.getRightX() - player.getX()) / 2));
		getGameScene().getViewport().yProperty()
				.bind(player.yProperty().subtract(viewHeight / 2).subtract((player.getBottomY() - player.getY()) / 2));
		// player.setRotation(200);
		// Sonido del motor
		mp = new MediaPlayer(new Media(new File("src/main/resources/assets/sounds/thruster.mp3").toURI().toString()));
		mp.setCycleCount(MediaPlayer.INDEFINITE);
		mp.volumeProperty().bind(model.thrustProperty());
		mp.play();

		physics = new PhysicsComponent();

		player.addComponent(physics);

		physics.setBodyType(BodyType.DYNAMIC);

		// Al jugador se le asigna una textura y se agrega al mundo

		player.setViewFromTexture("navePruebaSmall.png");

		getGameWorld().addEntities(player);

		getPhysicsWorld().setGravity(0, 0);

		player.setRenderLayer(RenderLayer.TOP);

		getGameScene().setBackgroundColor(Color.BLACK);

		Animations.hiperJumpTransition(player, 1, -Math.sin(Math.toRadians(player.getRotation())) * 100,
				Math.cos(Math.toRadians(player.getRotation())) * 100, getGameWorld());

		Componente componente = new Componente(player);
		componente.emitterEmissionPropertyProperty().bind(model.thrustProperty());

	}

	private void maxVel() {
		double maxVelocity = 1000;
		double x, y;
		if (physics.getLinearVelocity().getX() > maxVelocity || physics.getLinearVelocity().getX() < -maxVelocity) {
			if (physics.getLinearVelocity().getX() > 0)
				x = maxVelocity;
			else
				x = -maxVelocity;
		} else
			x = physics.getLinearVelocity().getX();

		if (physics.getLinearVelocity().getY() > maxVelocity || physics.getLinearVelocity().getY() < -maxVelocity) {
			if (physics.getLinearVelocity().getY() > 0)
				y = maxVelocity;
			else
				y = -maxVelocity;
		} else
			y = physics.getLinearVelocity().getY();

		physics.setLinearVelocity(x, y);
	}

	private void makeShoot() {
		if (System.currentTimeMillis() >= coolDown + 500) {
			coolDown = System.currentTimeMillis();
			canShoot = true;
//			sendPlayerPosition();
			getAudioPlayer().playSound("laser.mp3");
			Animations.shootTransition(player, getGameWorld());
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
		if (model.getThrust() + 0.5 < 8)
			model.setThrust(model.getThrust() + 0.5);

		physics.applyBodyForceToCenter(new Vec2(model.getThrust() * -Math.sin(physics.getBody().getAngle()),
				model.getThrust() * Math.cos(physics.getBody().getAngle())));
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
