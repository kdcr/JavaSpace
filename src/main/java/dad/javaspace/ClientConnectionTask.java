package dad.javaspace;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.almasb.fxgl.entity.GameWorld;

import dad.javaspace.networking.NetworkingPlayer;
import javafx.concurrent.Task;

public class ClientConnectionTask extends Task<Integer> {

	ClientModel model;
	// Conectividad


	private GameWorld gameWorld;

	public ClientConnectionTask(ClientModel model, GameWorld world) {
		this.model = model;
		this.gameWorld = world;
	}


	@Override
	protected Integer call() throws Exception {
		try {

			System.out.println("Buscando conexion...");

			model.setSocket(new Socket(model.getIp(), 2000));

			// Espera para que le de tiempo al servidor de mover la conexión a otro puerto
			Thread.sleep(3000);

			model.setFlujoEntrada(new InputStreamReader(model.getSocket().getInputStream(), "UTF-8"));

			model.setFlujoSalida(new OutputStreamWriter(model.getSocket().getOutputStream(), "UTF-8"));

			model.getFlujoSalida().write(model.getName() + "," + model.getSkin() + "\n");

			model.getFlujoSalida().flush();

			System.out.println("nombre enviado");

			model.setIdentity(model.getFlujoEntrada().read());

			System.out.println(model.getIdentity());
			model.setScanner(new Scanner(model.getFlujoEntrada()));

			System.out.println("id recibida");
			model.getFlujoSalida().write("ready\n");
			model.getFlujoSalida().flush();

			System.out.println(model.getScanner().nextLine());

			String test = model.getScanner().nextLine();
			System.out.println(test);
			for (String str : test.split("_")) {
				if (Integer.parseInt(str.split(",")[0]) != model.getIdentity())
					model.getJugadores().add(new NetworkingPlayer(str.split(",")[1], str.split(",")[2],
							Integer.parseInt(str.split(",")[0])));
			}

			System.out.println("Jugadores recibidos");

			for (NetworkingPlayer netPlayers : model.getJugadores()) {
				gameWorld.addEntity(netPlayers.getEntity());
				gameWorld.addEntities(netPlayers.getNameText());
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return 1;
	}
}
