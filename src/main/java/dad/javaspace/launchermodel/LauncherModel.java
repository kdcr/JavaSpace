package dad.javaspace.launchermodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import dad.javaspace.interfacing.ScreenResolutions;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LauncherModel {
	private DoubleProperty volumenMusica;
	private DoubleProperty volumenJuego;
	private StringProperty ip;
	private IntegerProperty puerto;
	private StringProperty nombreJugador;
	private BooleanProperty pantallaCompleta;
	private ObjectProperty<ScreenResolutions> resolucion;

	public LauncherModel() {
		volumenMusica = new SimpleDoubleProperty(this, "volumenMusica");
		volumenJuego = new SimpleDoubleProperty(this, "volumenMusica");
		ip = new SimpleStringProperty(this, "ip");
		puerto = new SimpleIntegerProperty(this, "puerto", 2000);
		nombreJugador = new SimpleStringProperty(this, "nombreJugador");
		pantallaCompleta = new SimpleBooleanProperty(this, "pantallaCompleta", true);
		resolucion = new SimpleObjectProperty<>(this, "resolucion");
	}

	public void guardarConfig() {
		String datos = "";
		
		File file = new File("src/main/resources/persistence/persistence.txt");
		
		datos += volumenMusica.getValue() + ";" + volumenJuego.getValue() + ";" + ip.get() + ";" + puerto.get() + ";" + nombreJugador.get() + ";" + pantallaCompleta.get() + ";" + resolucion.get().toString();
		byte [] tablaBytes = datos.getBytes();
		try {
			Files.write(file.toPath(), tablaBytes, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static LauncherModel cargarConfig() {
		String datos = "";
		
		LauncherModel model = new LauncherModel();
		
		File file = new File("src/main/resources/persistence/persistence.txt");
		
		try {
			byte [] tablaBytes = Files.readAllBytes(file.toPath());
			
			for (int i = 0; i < tablaBytes.length; i++) {
				datos += (char) tablaBytes[i];
			}
			
			String [] nuevosDatos = datos.split(";");
			
			// TODO revisar los valores a colocar en el slider. Probablemente haciendole un bindeo bidireccional
			model.setVolumenMusica(Double.parseDouble(nuevosDatos[0]));
			model.setVolumenJuego(Double.parseDouble(nuevosDatos[1]));
			model.setIp(nuevosDatos[2]);
			model.setPuerto(Integer.parseInt(nuevosDatos[3]));
			model.setNombreJugador(nuevosDatos[4]);
			model.setPantallaCompleta(Boolean.parseBoolean(nuevosDatos[5]));
			//model.setResolucion(ScreenResolutions.valueOf(nuevosDatos[6]));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
		
	}
	
	public final DoubleProperty volumenMusicaProperty() {
		return this.volumenMusica;
	}

	public final double getVolumenMusica() {
		return this.volumenMusicaProperty().get();
	}

	public final void setVolumenMusica(final double volumenMusica) {
		this.volumenMusicaProperty().set(volumenMusica);
	}

	public final DoubleProperty volumenJuegoProperty() {
		return this.volumenJuego;
	}

	public final double getVolumenJuego() {
		return this.volumenJuegoProperty().get();
	}

	public final void setVolumenJuego(final double volumenJuego) {
		this.volumenJuegoProperty().set(volumenJuego);
	}

	public final StringProperty ipProperty() {
		return this.ip;
	}

	public final String getIp() {
		return this.ipProperty().get();
	}

	public final void setIp(final String ip) {
		this.ipProperty().set(ip);
	}

	public final IntegerProperty puertoProperty() {
		return this.puerto;
	}

	public final int getPuerto() {
		return this.puertoProperty().get();
	}

	public final void setPuerto(final int puerto) {
		this.puertoProperty().set(puerto);
	}

	public final StringProperty nombreJugadorProperty() {
		return this.nombreJugador;
	}

	public final String getNombreJugador() {
		return this.nombreJugadorProperty().get();
	}

	public final void setNombreJugador(final String nombreJugador) {
		this.nombreJugadorProperty().set(nombreJugador);
	}

	public final BooleanProperty pantallaCompletaProperty() {
		return this.pantallaCompleta;
	}

	public final boolean isPantallaCompleta() {
		return this.pantallaCompletaProperty().get();
	}

	public final void setPantallaCompleta(final boolean pantallaCompleta) {
		this.pantallaCompletaProperty().set(pantallaCompleta);
	}

	public final ObjectProperty<ScreenResolutions> resolucionProperty() {
		return this.resolucion;
	}

	public final ScreenResolutions getResolucion() {
		return this.resolucionProperty().get();
	}

	public final void setResolucion(final ScreenResolutions resolucion) {
		this.resolucionProperty().set(resolucion);
	}

}
