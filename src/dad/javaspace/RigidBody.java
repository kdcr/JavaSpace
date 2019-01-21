package dad.javaspace;

import com.almasb.fxgl.entity.Entity;

public class RigidBody extends Entity {
	
	private double xForce, yForce;

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
	
}
