package dad.javaspace;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.settings.GameSettings;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class Main extends GameApplication {

	private String ip = "10.2.2.64", name = "jugador_test", skin = "0";

	private InputStreamReader flujoEntrada;
	private OutputStreamWriter flujoSalida;

	PhysicsComponent physicsComponent;

	Entity player = new Entity();

	long coolDown = 0, coolDownEngine = 0;

	private boolean canShoot = false;

	private ClientModel model = new ClientModel();

	public static void main(String[] args) {
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
//				if (player.getRotation() > 1)
//					player.setRotation(player.getRotation() + 0.5);
//				physicsComponent.setAngularVelocity(0.5);
				physicsComponent.applyAngularImpulse(10);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				// if (physicsComponent.getBody().getAngularVelocity() > -10)
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
		vars.put("pixelsMoved", 0);
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
//		if (System.currentTimeMillis() >= coolDownEngine + 100) {
//			coolDownEngine = System.currentTimeMillis();
//			getAudioPlayer().playSound("thruster.mp3");
//		}

		physicsComponent.applyBodyForceToCenter(new Vec2(-Math.sin(physicsComponent.getBody().getAngle()),
				Math.cos(physicsComponent.getBody().getAngle())));

	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
	}

}
