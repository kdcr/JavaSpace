package dad.javaspace.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import dad.javaspace.ClientModel;
import dad.javaspace.reports.ServerReportBean;
import javafx.concurrent.Task;

//
public class Server extends Task<Integer> {

	private static int nPlayers;
	private static ArrayList<Connection> playersArray = new ArrayList<Connection>();

	private static ArrayList<Connection> connectionsArray = new ArrayList<Connection>();
	private static String players = "", playersState = "";

	private ClientModel model;
	private ServerSocket skServidor;

	private ServerReportBean serverReportBean = new ServerReportBean();

	public static String getPlayers() {
		return players;
	}



	private static int Puerto;
	static int numCliente = 0;
	private ArrayList<Integer> disconectedList = new ArrayList<Integer>();

	/**
	 * Constructor del servidor que recibe como parámetro el modelo del cliente
	 * para inicializar sus variables
	 * 
	 * @param model
	 */
	public Server(ClientModel model) {
		this.model = model;
		nPlayers = model.getNumPlayers();
		Puerto = model.getPort();

	}

	/**
	 * esccha las distintas peticiones de los clientes según el número de
	 * jugadores establecido por el usuario en el correspondiente apartado de la
	 * intrerfaz. se crea una clase Connection por cada usuario, usando una
	 * cyclicBarrier para sincronizar todos los clientes
	 * 
	 * En un bucle while se realizan las distintas funciones necesarias para el
	 * funcionamiento del juego y se sale de este una vez haya quedado solo
	 * unjugador en partida, es decir, el ganador.
	 * 
	 * @return null
	 * 
	 */
	@Override
	protected Integer call() throws Exception {
		try {

			// Inicio el servidor en el puerto

			skServidor = new ServerSocket(Puerto);
			model.setServerSocket(skServidor);

			System.out.println("Escucho el puerto " + Puerto);

			while (numCliente < getnPlayers()) {

				Socket skCliente = skServidor.accept();

				System.out.println("Cliente conectado");

//				Atiendo al cliente mediante un thread
				playersArray.add(new Connection(skCliente, ++numCliente, playersArray));
			}

			connectionsArray.addAll(playersArray);
			for (Connection con : playersArray) {
				con.start();

			}

			Connection.barrera.await();

			for (Connection con : playersArray) {
				players += con.getIdentity() + "," + con.getNombre() + "," + con.getSkin() + "_";
			}

			players.concat("\n");
			Connection.barrera.await();

			Connection.barrera.await();

			while (playersArray.size() != 1) {

				playersState = "";
				for (Connection con : playersArray) {
					// if(con.getIdentity()!=this.identity)

					try {
						con.recive();

						// Sumar un disparo
						if (con.getItemStateString().split(",")[3] == "true") {
							serverReportBean.setDisparos(serverReportBean.getDisparos() + 1);
						}

						if (Double.parseDouble(con.getItemStateString().split(",")[5]) <= 0.0)
							disconectedList.add(con.getIdentity() - 1);
						playersState += con.getItemStateString();
					} catch (NoSuchElementException e) {
						disconectedList.add(con.getIdentity() - 1);
						playersState += con.disconnect();
					}

				}

				for (Integer disconected : disconectedList) {

					playersArray.remove(disconected.intValue());
				}
				disconectedList.clear();

				playersState += "\n";
				for (Connection con : playersArray) {
					try {
						System.out.println(playersState);
						con.send(playersState);
					} catch (Exception e) {
					}

				}

			}
			for (Connection con : connectionsArray) {
				if(con.isAlive())
					con.getSocket().close();
				
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

}
