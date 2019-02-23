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
		int indexError = 0;
		System.out.println("Thread conexiones up");
		System.out.println("Esperando start para sincronizar");
		System.out.println(model.getScanner().nextLine());
		model.setEnPartida(true);
		try {

			while (model.isEnPartida()) {
				try {
					sendPlayerPosition();
					desempaquetarPosiciones(model.getScanner().nextLine());
					indexError = 0;
				} catch (Exception e) {
					indexError++;
					if (indexError == 5) {
						System.err.println("Se ha perdido la conexión con el servidor, partida cancelada");
						e.printStackTrace();
						model.setEnPartida(false);
					}
				}
			}
		} catch (Exception e) {
			this.interrupt();
		}
		// Sale del bucle por lo que termina la partida
		// TODO recibir informacion post-partida
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
				if (!(str.split(",").length >= 2)) {
					bufferPlayer.getEntity().setX(Double.parseDouble(str.split(",")[1]));
					bufferPlayer.getEntity().setY(Double.parseDouble(str.split(",")[2]));
					bufferPlayer.getEntity().setRotation(Double.parseDouble(str.split(",")[3]));
					if (Boolean.parseBoolean(str.split(",")[4]))
						bufferPlayer.setShooting(true);
					bufferPlayer.setShield(Double.parseDouble(str.split(",")[5]));
					bufferPlayer.setHull(Double.parseDouble(str.split(",")[6]));
				} else {
					bufferPlayer.setHull(0);
				}
			}
		}
	}

	private void sendPlayerPosition() {
		try {
			String paquete = model.getPlayerX() + "," + model.getPlayerY() + "," + model.getPlayerRotation();

			if (model.isCanShoot()) {
				model.setCanShoot(false);

				model.getFlujoSalida().write(paquete + "," + true + "\n");
			} else
				model.getFlujoSalida()
						.write(paquete + "," + false + "," + model.getShield() + "," + model.getHull() + "\n");
			model.getFlujoSalida().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
