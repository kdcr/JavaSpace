package dad.javaspace.networking;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;

import dad.javaspace.objects.EntityTypes;

public class NetworkingProyectile {

	private String owner;

	private Entity entity = new Entity();

	public NetworkingProyectile(String owner, Entity projectile) {
		super();
		this.owner = owner;
		this.entity = projectile;
		
		this.entity.setType(EntityTypes.LASER);
		
		entity.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.polygon(0, 0, 16, 32, 32, 0)));
		
		entity.addComponent(new CollidableComponent(true));
		
		entity.setViewFromTexture("laser.png");
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

}
