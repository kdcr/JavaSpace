package dad.javaspace.networking;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import dad.javaspace.ClientModel;
import javafx.concurrent.Task;

public class ClientConnectionTask extends Task<Integer> {

	ClientModel model;

	public ClientConnectionTask(ClientModel model) {
		this.model = model;
	}

	@Override
	protected Integer call() throws Exception {
		final int MAX_INTENTOS = 5;
		int intentos = 0;
		model.setSocket(new Socket());
		model.setConnectionState("Buscando servidor");
		while (intentos != MAX_INTENTOS && !model.getSocket().isConnected()) {
			try {
				model.setConnectionState(model.getConnectionState() + ".");
				intentos++;
				model.setSocket(new Socket(model.getIp(), 2000));
			} catch (Exception e) {
				// Si se han llegado a 3 intentos se cancela la conexión y da excepción para
				// salir del task
				if (intentos == MAX_INTENTOS) {
					System.err.println("No se ha encontrado un servidor");
					throw new SocketTimeoutException();
				}
				// Ignorar la excepcion y esperar un poco
				Thread.sleep(1000);
			}
		}

		model.setConnectionState("Servidor encontrado, esperando jugadores");

		// Establecer los flujos

		model.setFlujoEntrada(new InputStreamReader(model.getSocket().getInputStream(), "UTF-8"));

		model.setFlujoSalida(new OutputStreamWriter(model.getSocket().getOutputStream(), "UTF-8"));

		model.getFlujoSalida().write(model.getName() + "," + model.getSkin() + "\n");

		model.getFlujoSalida().flush();


		model.setIdentity(model.getFlujoEntrada().read());

		model.setScanner(new Scanner(model.getFlujoEntrada()));

		model.getFlujoSalida().write("ready\n");
		model.getFlujoSalida().flush();

		model.setConnectionState("Esperando señal de inicio desde el servidor...");
		
		System.out.println(model.getScanner().nextLine());

		String tablaJugadores = model.getScanner().nextLine();
		
		for (String str : tablaJugadores.split("_")) {
			if (Integer.parseInt(str.split(",")[0]) != model.getIdentity())
				model.getJugadores().add(new NetworkingPlayer(str.split(",")[1], str.split(",")[2],
						Integer.parseInt(str.split(",")[0])));
		}
		
		model.setConnectionState("Empezando la partida...");

		return 0;
	}
}
