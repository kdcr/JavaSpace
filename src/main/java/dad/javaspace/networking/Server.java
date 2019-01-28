package dad.javaspace.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	Socket skCliente;
	
	 static final int NPLAYERS=2;
	private static ArrayList<Connection> connectionsArray=new ArrayList<Connection>();
	private static String players="";
	public static String getPlayers() {
		return players;
	}


	

	public static ArrayList<Connection> getConnectionsArray() {
		return connectionsArray;
	}


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

			while (numCliente<NPLAYERS) {

				
				
				Socket skCliente = skServidor.accept();
				

				System.out.println("Cliente conectado");

//				Atiendo al cliente mediante un thread
				connectionsArray.add(new Connection(skCliente, ++numCliente, connectionsArray));
			}
			
			

			for (Connection con : connectionsArray) {
				con.start();
				
			}
			
			
			//TODO cambiar el sleep por una barrera que se abra ina vez se hayan inicializado todas la conexiones
			Thread.sleep(4000);
			
			
			for (Connection con : connectionsArray) {
				players+=con.getIdentity()+","+con.getNombre()+","+con.getSkin()+"_";
			}
			
			players+="\n";
			System.out.println(players.toString());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
