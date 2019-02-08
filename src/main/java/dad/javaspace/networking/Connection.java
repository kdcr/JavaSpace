package dad.javaspace.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

import com.almasb.fxgl.entity.Entity;

public class Connection extends Thread {

	private Socket sk;
	private String[] itemState;

	private boolean isDamaged;

	private int identity;

	private String[] nombreSkin;
	private String nombre, skin, playerState;

	private static ArrayList<Connection> connectionsArray;

//	private static CyclicBarrier barrera = new CyclicBarrier(Server.NPLAYERS);

	private static ArrayList<Entity> bulletsArray = new ArrayList<Entity>();

	private static Entity player = new Entity();

	Scanner entrada;
	OutputStreamWriter salida;

	public Connection(Socket sk, int id, ArrayList<Connection> connectionsArray) throws IOException {
		this.sk = sk;
		this.identity = id;
		Connection.connectionsArray = connectionsArray;

		entrada = new Scanner(sk.getInputStream(), "UTF8");
		salida = new OutputStreamWriter(sk.getOutputStream(), "UTF8");
//		
//		try {
//			barrera.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (BrokenBarrierException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	public void run() {
		super.run();

		nombreSkin = entrada.nextLine().toString().split(",");

		nombre = nombreSkin[0];
		skin = nombreSkin[1];

		System.out.println("nombre: " + nombreSkin[0] + " skin: " + nombreSkin[1] + "\n");

		try {
			salida.write(identity);
			salida.flush();
			salida.write(Server.getPlayers());
			salida.flush();

			while (true) {

				itemStateString = entrada.nextLine().toString() + "_";
				salida.write(itemStateString);

				for (Connection con : connectionsArray) {
					playerState += con.getItemStateString();
				}
				salida.write(playerState);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getNombre() {
		return nombre;
	}

	public String getSkin() {
		return skin;
	}

	public int getIdentity() {
		return identity;
	}

	public String getItemStateString() {
		return itemStateString;
	}

	private String itemStateString;

}
