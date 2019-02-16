package dad.javaspace.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	Socket skCliente;
	
	 static final int NPLAYERS=2;
	private static ArrayList<Connection> connectionsArray=new ArrayList<Connection>();
	private static String players="", playersState="";
	private static boolean gameFinished=false;
	
	
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
			
			Connection.barrera.await();
			
			
			for (Connection con : connectionsArray) {
				players+=con.getIdentity()+","+con.getNombre()+","+con.getSkin()+"_";
			}
				
			players.concat("\n");
			Connection.barrera.await();
			
			Connection.barrera.await();
			
			while(true) {
				playersState="";
				for (Connection con : connectionsArray) {
					// if(con.getIdentity()!=this.identity)
					con.recive();
					playersState += con.getItemStateString();
					//TODO comprobar si disparamos
					//TODO comprobar si hay alguien cerca
					//TODO función para comprobar si acierta el disparo
					
				}
				
				
				
				playersState+="\n";
				for (Connection con : connectionsArray) {
					System.out.println(playersState);
					con.send(playersState);
					
				}
				
				
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
