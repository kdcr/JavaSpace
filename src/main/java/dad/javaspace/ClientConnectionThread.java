package dad.javaspace;

import java.util.Scanner;

import dad.javaspace.networking.NetworkingPlayer;

public class ClientConnectionThread extends Thread {

	private Scanner scanner;
	ClientModel model;

	public ClientConnectionThread(Scanner sc, ClientModel model) {
		scanner = sc;
		this.model = model;
	}

	@Override
	public void run() {
		super.run();

		System.out.println("Thread conexiones up");

		while (true) {
			desempaquetarPosiciones(scanner.nextLine());
			System.out.println("paquete recibido");
		}

	}

	public void desempaquetarPosiciones(String paquete) {
		String[] buffer = paquete.split("_").clone();
		int id = 0;
		System.out.println(paquete);
		for (String str : buffer) {
			id = Integer.parseInt(str.split(",")[0].toString());
			if (id != model.getIdentity()) {
				NetworkingPlayer bufferPlayer = find(id);
				bufferPlayer.getEntity().setX(Double.parseDouble(str.split(",")[1].toString()));
				bufferPlayer.getEntity().setY(Double.parseDouble(str.split(",")[2].toString()));
				bufferPlayer.getEntity().setRotation(Double.parseDouble(str.split(",")[3].toString()));
			}
		}
	}

	private NetworkingPlayer find(int id) {

		for (NetworkingPlayer player : model.getJugadores()) {
			if (player.getId() == id)
				return player;
		}

		return null;
	}
}
