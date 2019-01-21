package dad.javaspace;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
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
	private static int id;

	private static InputStreamReader flujoEntrada;
	private static OutputStreamWriter flujoSalida;

	private static Player player = new Player();

	private static Physics physics = new Physics();

	public static void main(String[] args) {

		try {
			Socket sk;
			
			sk = new Socket(ip, 2000);
			
			Thread.sleep(3000);
			
			flujoEntrada = new InputStreamReader(sk.getInputStream());
			
			flujoSalida = new OutputStreamWriter(sk.getOutputStream());
			
			flujoSalida.write(name + "," + skin + "\n");
			
			flujoSalida.flush();
			
			System.out.println("nombre enviado");
			
			id = flujoEntrada.read();
			
			System.out.println("id recibida");
			
			System.out.println(id);
			
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
			}
		}, KeyCode.W);

		input.addAction(new UserAction("Remove thrust") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
			}
		}, KeyCode.S);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				// Enviar input al servidor
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

//		player = Entities.builder().at(300, 300).viewFromNode(new ImageView("/dad/javaspace/resources/images/player.png"))
//				.buildAndAttach(getGameWorld());

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
