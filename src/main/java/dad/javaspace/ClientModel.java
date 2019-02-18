package dad.javaspace;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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

	private int port;

	private String ip, name = "jugadorTest", skin = "0";

	private DoubleProperty shield = new SimpleDoubleProperty(this, "shield", 1);

	private DoubleProperty playerX = new SimpleDoubleProperty(this, "playerX");
	private DoubleProperty playerY = new SimpleDoubleProperty(this, "playerY");
	private DoubleProperty playerRotation = new SimpleDoubleProperty(this, "playerRotation");

	private Socket socket;
	private OutputStreamWriter writer;
	private Scanner scanner;

	private InputStreamReader flujoEntrada;
	private OutputStreamWriter flujoSalida;

	public InputStreamReader getFlujoEntrada() {
		return flujoEntrada;
	}

	public void setFlujoEntrada(InputStreamReader flujoEntrada) {
		this.flujoEntrada = flujoEntrada;
	}

	public OutputStreamWriter getFlujoSalida() {
		return flujoSalida;
	}

	public void setFlujoSalida(OutputStreamWriter flujoSalida) {
		this.flujoSalida = flujoSalida;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public OutputStreamWriter getWriter() {
		return writer;
	}

	public void setWriter(OutputStreamWriter writer) {
		this.writer = writer;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	private boolean canShoot = false;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCanShoot() {
		return canShoot;
	}

	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}

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

	public final DoubleProperty playerXProperty() {
		return this.playerX;
	}

	public final double getPlayerX() {
		return this.playerXProperty().get();
	}

	public final DoubleProperty playerYProperty() {
		return this.playerY;
	}

	public final double getPlayerY() {
		return this.playerYProperty().get();
	}

	public final DoubleProperty playerRotationProperty() {
		return this.playerRotation;
	}

	public final double getPlayerRotation() {
		return this.playerRotationProperty().get();
	}

	public final DoubleProperty shieldProperty() {
		return this.shield;
	}

	public final double getShield() {
		return this.shieldProperty().get();
	}

	public final void setShield(final double shield) {
		this.shieldProperty().set(shield);
	}

}
