package dad.javaspace;

import java.util.Scanner;

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
		System.out.println(paquete);
		String[] buffer = paquete.split("_").clone();
		int id = 0;
		for (String str : buffer) {
			if (Integer.parseInt(str.split(",")[0].toString()) != model.getIdentity()) {
				id = Integer.parseInt(str.split(",")[0].toString()) - 1;
				model.getJugadores().get(id).getEntity().setX(Double.parseDouble(str.split(",")[1].toString()));
				model.getJugadores().get(id).getEntity().setY(Double.parseDouble(str.split(",")[2].toString()));
				model.getJugadores().get(id).getEntity().setRotation(Double.parseDouble(str.split(",")[3].toString()));
			}
		}
	}
}
