package dad.javaspace.networking;

import java.io.IOException;

import dad.javaspace.ClientModel;

public class ClientGameThread extends Thread {

	ClientModel model;

	public ClientGameThread(ClientModel model) {
		this.model = model;
	}

	@Override
	public void run() {
		super.run();
		System.out.println("Thread conexiones up");
		System.out.println("Esperando start para sincronizar");
		System.out.println(model.getScanner().nextLine());

		while (model.isEnPartida()) {
			try {
				sendPlayerPosition();
				desempaquetarPosiciones(model.getScanner().nextLine());
			} catch (Exception e) {
				e.printStackTrace();
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

	public void desempaquetarPosiciones(String paquete) {
		String[] buffer = paquete.split("_").clone();
		int id = 0;
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

	private void sendPlayerPosition() {
		try {
			String paquete = Math.round(model.getPlayerX()) + "," + Math.round(model.getPlayerY()) + ","
					+ model.getAngular();

			if (model.isCanShoot()) {
				model.setCanShoot(false);

				model.getWriter().write(paquete + "," + true + "\n");
			} else
				model.getWriter().write(paquete + "," + false + "\n");
			model.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
