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
		System.out.println("Buscando conexion...");
		int intentos = 0;
		model.setSocket(new Socket());
		while (intentos != MAX_INTENTOS && !model.getSocket().isConnected()) {
			try {
				intentos++;
				System.out.println("Intento " + intentos);
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


		System.out.println("Servidor encontrado");

		// Establecer los flujos

		model.setFlujoEntrada(new InputStreamReader(model.getSocket().getInputStream(), "UTF-8"));

		model.setFlujoSalida(new OutputStreamWriter(model.getSocket().getOutputStream(), "UTF-8"));

		model.getFlujoSalida().write(model.getName() + "," + model.getSkin() + "\n");

		model.getFlujoSalida().flush();

		System.out.println("Nombre enviado");

		model.setIdentity(model.getFlujoEntrada().read());

		System.out.println(model.getIdentity());
		model.setScanner(new Scanner(model.getFlujoEntrada()));

		System.out.println("Id recibida");
		model.getFlujoSalida().write("ready\n");
		model.getFlujoSalida().flush();

		System.out.println("Esperando señal de inicio desde el servidor...");

		System.out.println(model.getScanner().nextLine());

		String test = model.getScanner().nextLine();
		System.out.println(test);
		for (String str : test.split("_")) {
			if (Integer.parseInt(str.split(",")[0]) != model.getIdentity())
				model.getJugadores().add(new NetworkingPlayer(str.split(",")[1], str.split(",")[2],
						Integer.parseInt(str.split(",")[0])));
		}

		System.out.println("Jugadores recibidos");

		return 0;
	}
}
