package dad.javaspace;

import java.util.Scanner;

import com.almasb.fxgl.entity.Entity;

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
		
		while(true) {
			desempaquetarPosiciones(scanner.nextLine());
		}
		
	}
	
	public void desempaquetarPosiciones(String paquete) {
		for (String str : paquete.split("_")) {
			if (Integer.parseInt(str.split(",")[0]) != model.getIdentity()) {
			Entity buffer = model.getJugadores().get(Integer.parseInt(str.split(",")[0])).getEntity();
			buffer.setX(Double.parseDouble(str.split(",")[1]));
			buffer.setY(Double.parseDouble(str.split(",")[2]));
			buffer.setRotation(Double.parseDouble(str.split(",")[3]));
			}
		}
	}
}
