package dad.javaspace.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.almasb.fxgl.entity.Entity;

public class Connection extends Thread {

	private Socket sk;
	private String[] itemState;

	private boolean isDamaged;

	private int identity;

	private String[] nombreSkin;
	private String nombre, skin, playerState;
	static CyclicBarrier barrera = new CyclicBarrier(Server.NPLAYERS + 1);

	private static ArrayList<Connection> connectionsArray;


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

	}

	@Override
	public void run() {
		super.run();

		nombreSkin = entrada.nextLine().toString().split(",");

		nombre = nombreSkin[0];
		skin = nombreSkin[1];

		System.out.println("nombre: " + nombreSkin[0] + " skin: " + nombreSkin[1] + "\n");

		try {
			barrera.await();
			salida.write(identity + "\n");
			salida.flush();

			barrera.await();

			System.out.println(Server.getPlayers());
			salida.write(Server.getPlayers());
			salida.flush();

//			while (true) {
//
//				itemStateString = identity+","+entrada.nextLine().toString() + "_";
//				
//
//				for (Connection con : connectionsArray) {
//					//if(con.getIdentity()!=this.identity)
//					playerState += con.getItemStateString();
//				}
//				System.out.println(playerState);
//				salida.write(playerState);
//				salida.flush();
//
//			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
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
