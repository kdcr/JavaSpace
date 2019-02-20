package dad.javaspace.networking;

import com.almasb.fxgl.entity.Entity;

public class NetworkingProyectile {

	private String owner;

	private Entity entity = new Entity();

	public NetworkingProyectile(String owner, Entity projectile) {
		super();
		this.owner = owner;
		this.entity = projectile;
		
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
