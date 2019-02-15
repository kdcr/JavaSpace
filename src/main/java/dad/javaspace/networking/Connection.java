package dad.javaspace.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.almasb.fxgl.entity.Entity;

public class Connection extends Thread {

	private Socket sk;
	private String[] itemState;

	
	
	
	private int identity;

	private String[] nombreSkin;
	private String nombre, skin;
	static CyclicBarrier barrera = new CyclicBarrier(Server.NPLAYERS + 1);

	private static ArrayList<Connection> connectionsArray;



	Scanner entrada;
	OutputStreamWriter salida;

	public Connection(Socket sk, int id, ArrayList<Connection> connectionsArray) throws IOException {
		this.sk = sk;
		this.identity = id;
		Connection.connectionsArray = connectionsArray;

		entrada = new Scanner(this.sk.getInputStream(), "UTF8");
		salida = new OutputStreamWriter(this.sk.getOutputStream(), "UTF8");

	}

	@Override
	public void run() {
		super.run();
		boolean aux = false;
		nombreSkin = entrada.nextLine().toString().split(",");

		nombre = nombreSkin[0];
		skin = nombreSkin[1];

		System.out.println("nombre: " + nombreSkin[0] + " skin: " + nombreSkin[1] + "\n");

		try {
			barrera.await();
			salida.write(identity);
			salida.flush();

			 barrera.await();
				do {
					try {
						aux = false;
						System.out.println(entrada.nextLine());

					} catch (NoSuchElementException e) {
						aux = true;
					}
				} while (aux);
				salida.write("ready\n");
				salida.flush();

			System.out.println(Server.getPlayers().toString());
			salida.write(Server.getPlayers().toString()+"\n");
			salida.flush();
			
			salida.write("start\n");
			salida.flush();
			
				
			barrera.await();	
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

	}
	
	public void send(String str) throws IOException {
		salida.write(str);
		salida.flush();
	}

	
	public void recive() {
		itemStateString = identity + "," + entrada.nextLine() + "_";
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
