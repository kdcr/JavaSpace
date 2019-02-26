package dad.javaspace.reports;

import java.time.LocalDate;
import java.util.ArrayList;

public class ServerReportBean {

	private int disparos = 0;
	private String ganador = "";
	private ArrayList<String> ranking = new ArrayList<>() {
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			String ts = "";
			for (String nombre : this) {
				ts = ts.concat(nombre).concat("\n");
			}
			
			ts = ts.substring(0, ts.length()-2);

			return super.toString();
		}
	};
	private LocalDate fechaInicio, fechaFin;

	public ArrayList<String> getRanking() {
		return ranking;
	}

	public void setRanking(ArrayList<String> ranking) {
		this.ranking = ranking;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

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

}
