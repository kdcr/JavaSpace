package dad.javaspace;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.settings.GameSettings;

import dad.javaspace.objects.Player;
import dad.javaspace.physics.Physics;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class Main extends GameApplication {

	private static String ip = "10.2.2.64", name = "jugador_test", skin = "0";

	private static InputStreamReader flujoEntrada;
	private static OutputStreamWriter flujoSalida;

	private static Entity playerEntity = new Entity();

	PhysicsComponent physics;

	long coolDown = 0, coolDownEngine = 0;

	private boolean shooting = false;

	private ClientModel model = new ClientModel();

	public static void main(String[] args) {

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
//			identity = flujoEntrada.read();
//
//			Scanner input = new Scanner(flujoEntrada);
//
//			players = input.nextLine().split("_");
//
//			System.out.println(players[0]);
//
//			System.out.println("id recibida");
//
//			System.out.println(identity);
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		launch(args);

	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(600);
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
				playerEntity.setRotation(playerEntity.getRotation() + 1);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				// playerEntity.setRotation(playerEntity.getRotation() - 1);
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
		vars.put("pixelsMoved", 0);
	}

	@Override
	protected void initGame() {
		super.initGame();

		physics = new PhysicsComponent();

		physics.setBodyType(BodyType.DYNAMIC);

		// these are direct jbox2d objects, so we don't actually introduce new API
		FixtureDef fd = new FixtureDef();
		fd.setDensity(0.7f);
		fd.setRestitution(0.3f);
		physics.setFixtureDef(fd);

		playerEntity.addComponent(physics);
		playerEntity = Entities.builder().at(300, 300).viewFromTexture("player.png").buildAndAttach(getGameWorld());

	}

	private void makeShoot() {
		if (System.currentTimeMillis() > coolDown + 300) {
			coolDown = System.currentTimeMillis();
			shooting = true;
			sendPlayerPosition();
			shooting = false;
			sendPlayerPosition();
			getAudioPlayer().playSound("laser.mp3");
		}
	}

	private void sendPlayerPosition() {
//		try {
//			flujoSalida.write(identity + "," + player.getX() + "," + player.getY() + "," + player.getRotation() + ","
//					+ shooting + "\n");
//		} catch (IOException e) {
//		}
	}

	private void addThrust() {
//		if (System.currentTimeMillis() >= coolDownEngine + 100) {
		coolDownEngine = System.currentTimeMillis();
		getAudioPlayer().playSound("thruster.mp3");
//		}
		if (model.getThrust() + 0.1 <= 3) {
			model.setThrust(model.getThrust() + 0.1);
		}
		physics.setLinearVelocity(1, 0);
	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
	}

}
