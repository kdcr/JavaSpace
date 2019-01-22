package dad.javaspace;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.settings.GameSettings;

import dad.javaspace.objects.Player;
import dad.javaspace.physics.Physics;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class Main extends GameApplication {

	private StringProperty version = new SimpleStringProperty(this, "version", "0.0.1");

	private static String ip = "10.2.2.64", name = "jugador_test", skin = "0";
	private static int identity, numPlayers;

	private static String[] players;
	
	private static InputStreamReader flujoEntrada;
	private static OutputStreamWriter flujoSalida;

	private static Player player = new Player();

	private static Physics physics = new Physics();

	long coolDown = 0;

	private boolean shooting = false;

	public static void main(String[] args) {

		try {
			Socket sk;

			System.out.println("Buscando conexion...");

			sk = new Socket(ip, 2000);

			// Espera para que le de tiempo al servidor de mover la conexión a otro puerto
			Thread.sleep(3000);

			flujoEntrada = new InputStreamReader(sk.getInputStream(), "UTF-8");

			flujoSalida = new OutputStreamWriter(sk.getOutputStream(), "UTF-8");

			flujoSalida.write(name + "," + skin + "\n");

			flujoSalida.flush();

			System.out.println("nombre enviado");

			identity = flujoEntrada.read();
			
			Scanner input = new Scanner(flujoEntrada);

			players = input.nextLine().split("_");
			
			System.out.println(players[0]);

			System.out.println("id recibida");

			System.out.println(identity);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		launch(args);

	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(600);
		settings.setHeight(600);
		settings.setTitle("JavaSpace");
		settings.setVersion(version.get());
	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Add thrust") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
				if (player.getThrust() + 0.1 <= 3) {
					player.setThrust(player.getThrust() + 0.1);
				}
			}
		}, KeyCode.W);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
				makeShoot();
			}
		}, KeyCode.SPACE);

		// Bindeos para enviar la informacion segun hay cambios en el jugador
		player.thrustProperty().addListener(e -> sendPlayerPosition());
		player.angleProperty().addListener(e -> sendPlayerPosition());

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

//		player = Entities.builder().at(300, 300).viewFromNode(new ImageView("/dad/javaspace/resources/images/player.png"))
//				.buildAndAttach(getGameWorld());

	}

	private void makeShoot() {
		if (System.currentTimeMillis() > coolDown + 1000) {
			coolDown = System.currentTimeMillis();
			shooting = true;
			sendPlayerPosition();
			shooting = false;
			sendPlayerPosition();
		}
	}

	private void sendPlayerPosition() {
		try {
			flujoSalida.write(identity + "," + player.getX() + "," + player.getY() + "," + player.getRotation() + ","
					+ shooting + "\n");
		} catch (IOException e) {
		}
	}

	public final StringProperty versionProperty() {
		return this.version;
	}

	public final String getVersion() {
		return this.versionProperty().get();
	}

	public final void setVersion(final String version) {
		this.versionProperty().set(version);
	}

}
