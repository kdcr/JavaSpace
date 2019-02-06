package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ComponentePropulsor extends Component {

	private ParticleEmitter emitter;
	private Entity player = new Entity();

	public ComponentePropulsor(Entity player) {
		this.player = player;
		emitter = ParticleEmitters.newFireEmitter(0);
		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * 20) + 35,
						(Math.cos(Math.toRadians(player.getRotation())) * 45) + 35);
			}
		});
		emitter.setExpireFunction(e -> Duration.seconds(0.2));
		emitter.setAllowParticleRotation(true);
		emitter.setEmissionRate(1);
		emitter.setNumParticles(1);
		ParticleComponent particleComponent = new ParticleComponent(emitter);
		player.addComponent(particleComponent);
		player.addComponent(this);
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
				return new Point2D((-Math.sin(Math.toRadians(player.getRotation())) * 45) + 35,
						(Math.cos(Math.toRadians(player.getRotation())) * 45) + 35);
			}
		});
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
	}

	public final DoubleProperty emissionRateProperty() {
		return this.emitter.emissionRateProperty();
	}

	public final double getEmissionRate() {
		return this.emissionRateProperty().get();
	}

	public final void setEmissionRate(final double emissionRate) {
		this.emissionRateProperty().set(emissionRate);
	}

}
