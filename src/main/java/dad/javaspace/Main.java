package dad.javaspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.scene.Viewport;
import com.almasb.fxgl.settings.GameSettings;

import dad.javaspace.objects.EntityTypes;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

public class Main extends GameApplication {

	// Conectividad
	private String ip = "10.2.2.64", name = "jugador_test", skin = "0";

	private InputStreamReader flujoEntrada;
	private OutputStreamWriter flujoSalida;

	// Mecanica interna
	PhysicsComponent physicsComponent;
	Entity player = new Entity();
	private ClientModel model = new ClientModel();
	private MediaPlayer mp;
	long coolDown = 0, coolDownStars = 0;
	private boolean canShoot = false;

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
		settings.setVersion(model.getVersion());
	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				physicsComponent.setAngularVelocity(0.5);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				physicsComponent.setAngularVelocity(-0.5);
			}
		}, KeyCode.A);

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
		Text textPixels = new Text();
		textPixels.setTranslateX(50); // x = 50
		textPixels.setTranslateY(100); // y = 100

		getGameScene().addUINode(textPixels); // add to the scene graph
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("lives", 3);
	}

	@Override
	protected void initGame() {
		super.initGame();

//		try {
//			Socket sk;
//
//			System.out.println("Buscando conexion...");
//
//			sk = new Socket(ip, 2000);
//
//			// Espera para que le de tiempo al servidor de mover la conexión a otro puerto
//			Thread.sleep(3000);
//
//			flujoEntrada = new InputStreamReader(sk.getInputStream(), "UTF-8");
//
//			flujoSalida = new OutputStreamWriter(sk.getOutputStream(), "UTF-8");
//
//			flujoSalida.write(name + "," + skin + "\n");
//
//			flujoSalida.flush();
//
//			System.out.println("nombre enviado");
//
//			model.setIdentity(flujoEntrada.read());
//
//			Scanner input = new Scanner(flujoEntrada);
//
//			model.setPlayers(input.nextLine().split("_"));
//
//			System.out.println(model.getPlayers()[0]);
//
//			System.out.println("id recibida");
//
//			System.out.println(model.getIdentity());
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		
		double viewWidth = getGameScene().getViewport().getWidth();
		double viewHeight = getGameScene().getViewport().getHeight();
		getGameScene().getViewport().xProperty().bind(player.xProperty().subtract(viewWidth/2));
		getGameScene().getViewport().yProperty().bind(player.yProperty().subtract(viewHeight/2));

		// Mecanica

		mp = new MediaPlayer(new Media(new File("src/main/resources/assets/sounds/thruster.mp3").toURI().toString()));

		physicsComponent = new PhysicsComponent();

		physicsComponent.setBodyType(BodyType.DYNAMIC);

		getPhysicsWorld().setGravity(0, 0);

		// these are direct jbox2d objects, so we don't actually introduce new API
		FixtureDef fd = new FixtureDef();
		fd.setDensity(0.7f);
		fd.setRestitution(0.3f);
		physicsComponent.setFixtureDef(fd);

		player.setViewFromTexture("player.png");

		player.addComponent(physicsComponent);

		getGameWorld().addEntities(player);
		physicsComponent.getBody().setAngularDamping(0.8f);

		mp.setVolume(0);
		mp.play();

		// Estetico

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
		physicsComponent.applyBodyForceToCenter(new Vec2(-Math.sin(physicsComponent.getBody().getAngle()) * 0.5,
				Math.cos(physicsComponent.getBody().getAngle()) * 0.5));
	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);

		generateStars();

//		if (getInput().isHeld(KeyCode.W))
//			mp.setVolume(mp.getVolume() + 0.1);
//		else
//			mp.setVolume(mp.getVolume() - 0.05);
	}

	private void generateStars() {
		if (System.currentTimeMillis() > coolDownStars + 100) {
			coolDownStars = System.currentTimeMillis();

			Entity newStar = new Entity();
			newStar.setViewFromTexture("star_placeholder.png");
			newStar.setType(EntityTypes.STAR);
			starArray.add(newStar);
			getGameWorld().addEntity(newStar);

			newStar.setX(getGameScene().getViewport().getX() + (Math.random() * getGameScene().getViewport().getWidth())); 
			newStar.setY(getGameScene().getViewport().getY() + (Math.random() * getGameScene().getViewport().getHeight()));

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
