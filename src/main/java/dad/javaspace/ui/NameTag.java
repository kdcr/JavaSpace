package dad.javaspace.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.almasb.fxgl.entity.Entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.Initializable;
import javaspace.nombre.ComponenteNombre;

public class NameTag extends Entity implements Initializable {

	private ComponenteNombre componenteNombre = new ComponenteNombre();

	private DoubleProperty shield = new SimpleDoubleProperty(this, "shield", 1);

	public NameTag() {
		super();
		this.setView(componenteNombre);
		componenteNombre.shieldProperty().bind(this.shield);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setName(String name) {
		componenteNombre.setText(name);
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
