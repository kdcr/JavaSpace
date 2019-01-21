package dad.javaspace;

public class Player extends RigidBody {

	private double thrust = 0;
	private int lives = 3;

	public Player() {

	}

	public double getThrust() {
		return thrust;
	}

	public void setThrust(double thrust) {
		this.thrust = thrust;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

}