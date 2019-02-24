package dad.javaspace.reports;

public class ServerReportBean {

	private int disparos = 0;
	private String ganador;
	private String[] nombreJugadores;

	public int getDisparos() {
		return disparos;
	}

	public void setDisparos(int disparos) {
		this.disparos = disparos;
	}

	public String getGanador() {
		return ganador;
	}

	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	public String[] getNombreJugadores() {
		return nombreJugadores;
	}

	public void setNombreJugadores(String[] nombreJugadores) {
		this.nombreJugadores = nombreJugadores;
	}

}
