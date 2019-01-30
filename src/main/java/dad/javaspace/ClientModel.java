package dad.javaspace;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientModel {

	private int identity, numPlayers;

	private String[] players;

	private double xForce, yForce;

	private DoubleProperty thrust = new SimpleDoubleProperty();
	
	private FloatProperty angular = new SimpleFloatProperty();

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

	public String[] getPlayers() {
		return players;
	}

	public void setPlayers(String[] players) {
		this.players = players;
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

	public final FloatProperty angularProperty() {
		return this.angular;
	}

	public final float getAngular() {
		return this.angularProperty().get();
	}

	public final void setAngular(final float angular) {
		this.angularProperty().set(angular);
	}

}
