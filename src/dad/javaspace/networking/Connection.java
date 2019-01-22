package dad.javaspace.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.almasb.fxgl.entity.Entity;

public class Connection extends Thread{
	
	Socket sk;
	String[] itemState;
	private int identity;
	

	private String[] nombreSkin;
	private String nombre, skin;


	private static  ArrayList<Connection> connectionsArray;
	
	private static CyclicBarrier barrera =new CyclicBarrier(Server.NPLAYERS);

	
	private static Entity player=new Entity();
	
	Scanner entrada ;
	OutputStreamWriter salida;
	
	
	
	
	public Connection(Socket sk, int id, ArrayList<Connection> connectionsArray) throws IOException {
		this.sk=sk;
		this.identity=id;
		this.connectionsArray=connectionsArray;
		
		
		entrada= new Scanner(sk.getInputStream(), "UTF8");
		salida=new OutputStreamWriter(sk.getOutputStream(), "UTF8");
//		
//		try {
//			barrera.await();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BrokenBarrierException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	public void run() {
		super.run();
		
		
		nombreSkin=entrada.nextLine().toString().split(",");
		
		nombre=nombreSkin[0];
		skin=nombreSkin[1];
		
		System.out.println("nombre: "+nombreSkin[0]+" skin: "+nombreSkin[1]+"\n");
		
		try {
				salida.write(identity);
				salida.flush();
				salida.write(Server.getPlayers());
				salida.flush();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		while(true){
			
			itemState=entrada.nextLine().toString().split(",");
			//if(itemState[3].equals("true"))
			//TODO hacer el evento de disparar
			
			
			
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
	

}
