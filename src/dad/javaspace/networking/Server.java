package dad.javaspace.networking;

import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {
	Socket skCliente;

	private static boolean newPlayer=true;
	
	static final int Puerto = 2000;
	 static int numCliente = 0;


	public Server(Socket sCliente) {
		skCliente = sCliente;
	}

	
	public static void main(String[] arg) {
		

		try {

			// Inicio el servidor en el puerto

			ServerSocket skServidor = new ServerSocket(Puerto);

			
			
			System.out.println("Escucho el puerto " + Puerto);

			while (newPlayer) {



				Socket skCliente = skServidor.accept();
				

				System.out.println("Cliente conectado");

//				Atiendo al cliente mediante un thread
			///TODO crear clase cliente que recibe el socket y que tenga el run
				new Connection(skCliente, numCliente++).start();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	public void run() {
//
//		
//		
//		
//		
//		try {
//
//			// Creo los flujos de entrada y salida
//
//			
//			Scanner scanner=new Scanner(skCliente.getInputStream(), "UTF8");
//			OutputStreamWriter flujo_salida = new OutputStreamWriter(skCliente.getOutputStream(), "UTF8");
//
//			
//			while(!skCliente.isClosed()) {
//				
//				//TODO recibr inputs y manejar las vidas
//				
//				
//				
//				
//			}
//		
// 
//			// Se cierra la conexión
//
//			skCliente.close();
//
//			System.out.println("Cliente desconectado");
//
//		} catch (Exception e) {
//
//			System.out.println(e.getMessage());
//
//		}
//
//	}
}
