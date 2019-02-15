package dad.javaspace.networking;

import com.almasb.fxgl.entity.Entity;

import dad.javaspace.ui.NameTag;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class NetworkingPlayer {

	private Entity entity;
	private NameTag nameText = new NameTag();

	private String name, skin;
	private int id;

	private DoubleProperty shield = new SimpleDoubleProperty();

	public NetworkingPlayer(String name, String skin, int id) {
		this.name = name;
		this.skin = skin;
		this.id = id;

		nameText.setName(name);

		entity = new Entity();
		entity.setViewFromTexture("navePruebaSmall.png");

		nameText.xProperty().bind(entity.xProperty());
		nameText.yProperty().bind(entity.yProperty().subtract(10));
	}

	public NetworkingPlayer() {
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getName() {
		return name;
	}

	public void setNombre(String nombre) {
		this.name = nombre;
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

	public Entity getNameText() {
		return this.nameText;
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
