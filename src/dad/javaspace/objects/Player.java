package dad.javaspace.objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player extends RigidBody {

	private DoubleProperty thrust = new SimpleDoubleProperty(this, "thrust", 0);
	private IntegerProperty lives = new SimpleIntegerProperty(this, "lives", 3);

	public Player() {

	}

	public final DoubleProperty thrustProperty() {
		return this.thrust;
	}

	public final double getThrust() {
		return this.thrustProperty().get();
	}

	public final void setThrust(final double thrust) {
		this.thrustProperty().set(thrust);
	}

	public final IntegerProperty livesProperty() {
		return this.lives;
	}

	public final int getLives() {
		return this.livesProperty().get();
	}

	public final void setLives(final int lives) {
		this.livesProperty().set(lives);
	}

}