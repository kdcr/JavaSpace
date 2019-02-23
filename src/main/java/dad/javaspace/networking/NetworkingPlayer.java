package dad.javaspace.networking;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;

import dad.javaspace.objects.EntityTypes;
import dad.javaspace.objects.effects.ComponentePropulsor;
import dad.javaspace.ui.NameTag;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class NetworkingPlayer {

	private boolean alive;

	private Entity entity;
	private NameTag nameText = new NameTag();

	private String name, skin;
	private int id;

	private DoubleProperty shield = new SimpleDoubleProperty(this, "shield", 1);
	private DoubleProperty hull = new SimpleDoubleProperty(this, "hull", 1);

	private ComponentePropulsor componentePropulsor;

	private boolean shooting = false;

	public NetworkingPlayer(String name, String skin, int id) {
		this.name = name;
		this.skin = skin + 1;
		this.id = id;

		nameText.setName(name);

		entity = new Entity();
		entity.setViewFromTexture("Nave" + skin + ".png");

		entity.setRenderLayer(RenderLayer.TOP);

		entity.setType(EntityTypes.ENEMY_PLAYER);

		componentePropulsor = new ComponentePropulsor(entity);
		componentePropulsor.setEmissionRate(0.33);

		nameText.xProperty().bind(entity.xProperty().subtract(50));
		nameText.yProperty().bind(entity.yProperty().subtract(50));
	}

	public ComponentePropulsor getComponentePropulsor() {
		return componentePropulsor;
	}

	public boolean isShooting() {
		return shooting;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
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

	public final DoubleProperty hullProperty() {
		return this.hull;
	}

	public final double getHull() {
		return this.hullProperty().get();
	}

	public final void setHull(final double hull) {
		this.hullProperty().set(hull);
	}

}
