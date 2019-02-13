package dad.javaspace.networking;

import com.almasb.fxgl.entity.Entity;

public class NetworkingPlayer {

	private Entity entity;
	private String nombre, skin;
	private int id;

	public NetworkingPlayer(String nombre, String skin, int id) {
		this.nombre = nombre;
		this.skin = skin;
		this.id = id;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
