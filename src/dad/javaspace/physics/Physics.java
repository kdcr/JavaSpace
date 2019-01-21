package dad.javaspace.physics;

import dad.javaspace.Player;
import dad.javaspace.RigidBody;

public class Physics {

	/*
	 * Mueve el objeto segun la fuerza actual
	 */
	public static void calcCineticMovement(RigidBody object) {
		object.setX(object.getX() + object.getxForce());
		object.setY(object.getY() + object.getyForce());
	}

	/*
	 * Calcula la fuerza que ejerce el impulsor de un jugador sobre si mismo
	 */
	public static void forcePlayerCalc(Player player) {
		int maxForce = 5;
		float x, y;
		if (player.getThrust() != 0) {
			x = (float) (player.getxForce() + (player.getThrust() / 10 * Math.sin((player.getRotation()))));
			y = (float) (player.getyForce() + (player.getThrust() / 10 * -Math.cos((player.getRotation()))));

			player.setxForce(x);
			player.setyForce(y);

			if (player.getxForce() > maxForce)
				player.setxForce(maxForce);

			if (player.getxForce() < -maxForce)
				player.setxForce(-maxForce);

			if (player.getyForce() > maxForce)
				player.setyForce(maxForce);

			if (player.getyForce() < -maxForce)
				player.setyForce(-maxForce);

		}

	}

}
