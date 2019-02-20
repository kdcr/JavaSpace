package dad.javaspace.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javafx.concurrent.Task;
//
public class Server extends Task<Integer> {

	private static  int nPlayers;
	private static ArrayList<Connection> connectionsArray = new ArrayList<Connection>();
	private static String players = "", playersState = "";

	public static String getPlayers() {
		return players;
	}

	public static ArrayList<Connection> getConnectionsArray() {
		return connectionsArray;
	}

	private static  int Puerto ;
	static int numCliente = 0;
	private ArrayList<Integer> disconectedList = new ArrayList<Integer>();

	public Server(int nPlayers, int port) {
		setnPlayers(nPlayers);
		Puerto=port;
	}

	
	
	@Override
	protected Integer call() throws Exception {
		try {

			// Inicio el servidor en el puerto

			ServerSocket skServidor = new ServerSocket(Puerto);

			System.out.println("Escucho el puerto " + Puerto);

			while (numCliente < getnPlayers()) {

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
				players += con.getIdentity() + "," + con.getNombre() + "," + con.getSkin() + "_";
			}

			players.concat("\n");
			Connection.barrera.await();

			Connection.barrera.await();

			while (connectionsArray.size()!=1) {

				playersState = "";
				for (Connection con : connectionsArray) {
					// if(con.getIdentity()!=this.identity)

					try {
						con.recive();

						playersState += con.getItemStateString();
					} catch (NoSuchElementException e) {
						disconectedList.add(con.getIdentity() - 1);
					}

				}
				for (Integer disconected : disconectedList) {
					connectionsArray.remove(disconected);
				}
				disconectedList.clear();

				playersState += "\n";
				for (Connection con : connectionsArray) {
					try {
						System.out.println(playersState);
						con.send(playersState);
					} catch (Exception e) {
					}

				}

			}
			skServidor.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getnPlayers() {
		return nPlayers;
	}

	public static void setnPlayers(int nPlayers) {
		Server.nPlayers = nPlayers;
	}

}
