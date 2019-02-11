package dad.javaspace;

import java.util.ArrayList;

import dad.javaspace.networking.NetworkingPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientModel {

	private int identity, numPlayers;

	private double xForce, yForce;

	private ArrayList<NetworkingPlayer> jugadores = new ArrayList<>();

	private BooleanProperty enPartida = new SimpleBooleanProperty(this, "enPartida", false);

	private DoubleProperty thrust = new SimpleDoubleProperty();

	private DoubleProperty angular = new SimpleDoubleProperty();

	private StringProperty version = new SimpleStringProperty(this, "version", "0.0.1");

	public final DoubleProperty thrustProperty() {
		return this.thrust;
	}

	public final double getThrust() {
		return this.thrustProperty().get();
	}

	public final void setThrust(final double thrust) {
		this.thrustProperty().set(thrust);
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public double getxForce() {
		return xForce;
	}

	public void setxForce(double xForce) {
		this.xForce = xForce;
	}

	public double getyForce() {
		return yForce;
	}

	public void setyForce(double yForce) {
		this.yForce = yForce;
	}

	public final StringProperty versionProperty() {
		return this.version;
	}

	public final String getVersion() {
		return this.versionProperty().get();
	}

	public final void setVersion(final String version) {
		this.versionProperty().set(version);
	}

	public final DoubleProperty angularProperty() {
		return this.angular;
	}

	public final double getAngular() {
		return this.angularProperty().get();
	}

	public final void setAngular(final double angular) {
		this.angularProperty().set(angular);
	}

	public final BooleanProperty enPartidaProperty() {
		return this.enPartida;
	}

	public final boolean isEnPartida() {
		return this.enPartidaProperty().get();
	}

	public final void setEnPartida(final boolean enPartida) {
		this.enPartidaProperty().set(enPartida);
	}

	public ArrayList<NetworkingPlayer> getJugadores() {
		return jugadores;
	}

	public void setJugadores(ArrayList<NetworkingPlayer> jugadores) {
		this.jugadores = jugadores;
	}

}
