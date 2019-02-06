package dad.javaspace.objects.effects;

import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;
import com.almasb.fxgl.util.Supplier;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Componente extends Component {

	private DoubleProperty emitterEmissionProperty = new SimpleDoubleProperty();

	private ParticleEmitter emitter;
	private ParticleComponent pComponent;
	private Entity player = new Entity();

	public Componente(Entity player) {
		
		this.player = player;
		emitter = ParticleEmitters.newFireEmitter(0);
		emitter.setAccelerationFunction(new Supplier<Point2D>() {
			@Override
			public Point2D get() {
//				Gravity
				return new Point2D(0, 0);
			}
		});
		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D(0, 0);
			}
		});
		emitter.setExpireFunction(e -> Duration.seconds(0.3));
		emitter.setAllowParticleRotation(true);
		emitter.setEmissionRate(1);
		emitter.setNumParticles(2);

		pComponent = new ParticleComponent(emitter);
		player.addComponent(pComponent);

		player.addComponent(this);

		emitter.emissionRateProperty().bindBidirectional(emitterEmissionProperty);
	}

	@Override
	public void onAdded() {

		super.onAdded();
	}

	@Override
	public void onUpdate(double tpf) {

		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				int da = 40;
				int db = 45;
				int dc = 90;
				int dd = 45;
				int pa = 35;
				int pb = 30;
				int pc = 90;
				int pd = 30;
				Point2D punto = new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * da) + pa,
						(Math.cos(Math.toRadians(player.getRotation())) * da) + pa);
				if (player.getRotation() > 90 || player.getRotation() <= 180) {
					punto = new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * db) + pb,
							(Math.cos(Math.toRadians(player.getRotation())) * db) + pb);
				}
				if (player.getRotation() > 180 || player.getRotation() <= 240) {
					punto = new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * dc) + pc,
							(Math.cos(Math.toRadians(player.getRotation())) * dc) + pc);
				}
				if (player.getRotation() > 240 || player.getRotation() <= 360) {
					punto = new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * dd) + pd,
							(Math.cos(Math.toRadians(player.getRotation())) * dd) + pd);
				}
				return punto;
			}
		});
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
	}

	public final DoubleProperty emitterEmissionPropertyProperty() {
		return this.emitterEmissionProperty;
	}

	public final double getEmitterEmissionProperty() {
		return this.emitterEmissionPropertyProperty().get();
	}

	public final void setEmitterEmissionProperty(final double emitterEmissionProperty) {
		this.emitterEmissionPropertyProperty().set(emitterEmissionProperty);
	}

}
