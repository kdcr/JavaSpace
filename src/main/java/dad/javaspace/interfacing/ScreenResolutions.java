package dad.javaspace.interfacing;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class ScreenResolutions {
	private IntegerProperty x;
	private IntegerProperty y;

	public ScreenResolutions() {
		x = new SimpleIntegerProperty(this, "x", 1280);
		y = new SimpleIntegerProperty(this, "y", 720);
	}

	public ScreenResolutions(int x, int y) {
		this();
		this.x.set(x);
		this.y.set(y);
	}

	public ScreenResolutions(String res) {
		this();
		if (null != res) {
			String[] coordenadas = res.split("x");
			this.x = new SimpleIntegerProperty(this, "x", Integer.parseInt(coordenadas[0]));
			this.y = new SimpleIntegerProperty(this, "y", Integer.parseInt(coordenadas[1]));
		}

	}

	public static ListProperty<ScreenResolutions> getScreenResolutions() {
		ListProperty<ScreenResolutions> listaResoluciones = new SimpleListProperty<>(ScreenResolutions.class,
				"listaResoluciones", FXCollections.observableArrayList());
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DisplayMode[] modes = device.getDisplayModes();

		for (int j = 0; j < modes.length; j++) {
			DisplayMode m = modes[j];
			ScreenResolutions res = new ScreenResolutions((int) m.getWidth(), (int) m.getHeight());
			if (!listaResoluciones.contains(res))
				listaResoluciones.add(res);
		}

		return listaResoluciones;
	}
	
	public static ScreenResolutions resolucionMinima() {
		return ScreenResolutions.getScreenResolutions().get(0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof ScreenResolutions))
			return false;

		ScreenResolutions otra = (ScreenResolutions) obj;

		if (otra.getX() == this.getX() && otra.getY() == this.getY())
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return String.valueOf(x.get()) + "x" + String.valueOf(y.get());
	}

	public final IntegerProperty xProperty() {
		return this.x;
	}

	public final int getX() {
		return this.xProperty().get();
	}

	public final void setX(final int x) {
		this.xProperty().set(x);
	}

	public final IntegerProperty yProperty() {
		return this.y;
	}

	public final int getY() {
		return this.yProperty().get();
	}

	public final void setY(final int y) {
		this.yProperty().set(y);
	}

}
