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
		for (String str : paquete.split("_")) {
			if (Integer.parseInt(str.split(",")[0]) - 1 != model.getIdentity()) {
				model.getJugadores().get(Integer.parseInt(str.split(",")[0]) - 1).getEntity()
						.setX(Double.parseDouble(str.split(",")[1]));
				model.getJugadores().get(Integer.parseInt(str.split(",")[0]) - 1).getEntity()
						.setY(Double.parseDouble(str.split(",")[2]));
				model.getJugadores().get(Integer.parseInt(str.split(",")[0]) - 1).getEntity()
						.setRotation(Double.parseDouble(str.split(",")[3]));
			}
		}
	}
}
