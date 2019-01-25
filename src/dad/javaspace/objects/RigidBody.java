package dad.javaspace.objects;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RigidBody extends Entity {

	private ObjectProperty<Vec2> forces = new SimpleObjectProperty<Vec2>(this, "forces");

	public final ObjectProperty<Vec2> forcesProperty() {
		return this.forces;
	}

	public final Vec2 getForces() {
		return this.forcesProperty().get();
	}

	public final void setForces(final Vec2 forces) {
		this.forcesProperty().set(forces);
	}

}
