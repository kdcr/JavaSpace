package dad.javaspace;

import java.net.Socket;

public class ClientConnectionThread extends Thread {

	private Socket socket;
	
	public ClientConnectionThread(Socket sk) {
		socket = sk;
	}
	
	@Override
	public void run() {
		super.run();
		
	}
}
