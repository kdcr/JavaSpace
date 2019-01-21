package dad.javaspace.networking;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import com.almasb.fxgl.entity.Entity;

public class Connection extends Thread{
	
	Socket sk;
	String position;
	private int id;
	private String[] nombreSkin;
	
	
	
	private static Entity player;
	
	Scanner entrada ;
	OutputStreamWriter salida;
	
	
	
	public Connection(Socket sk, int id) throws IOException {
		this.sk=sk;
		this.id=id;
		
		
		entrada= new Scanner(sk.getInputStream(), "UTF8");
		salida=new OutputStreamWriter(sk.getOutputStream(), "UTF8");
		
		
	}
	
	@Override
	public void run() {
		super.run();
		System.out.println(id);
		nombreSkin=entrada.nextLine().toString().split(",");
		
		System.out.println("nombre: "+nombreSkin[0]+" skin: "+nombreSkin[1]+"\n");
		try {
				salida.write(id);
				salida.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
