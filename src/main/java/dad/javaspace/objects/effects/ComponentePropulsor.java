package dad.javaspace.objects.effects;

import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class ComponentePropulsor extends Component {

	private ParticleEmitter emitter;
	private Entity player = new Entity();
	private boolean dead = false;

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
		if (dead) {
			emitter.setNumParticles((int) (Math.random() * 2));
			emitter.setExpireFunction(e -> Duration.seconds(Math.random() * 3));
			
			emitter.setScaleFunction(e-> {
				Double seed = Math.random();
				return new Point2D(seed, seed);
			});
			
			emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
				@Override
				public Point2D apply(Integer arg) {
					return new Point2D((-Math.sin(Math.toRadians(Math.random() * 360))) + Math.random() * 50,
							(Math.cos(Math.toRadians(Math.random() * 360))) + Math.random() * 50);
				}
			});
		}
	}

	public void onShipDestroyed() {
		dead = true;
		AssetLoader loader = new AssetLoader();
		// emitter.setBlendMode(BlendMode.SRC_OVER);
		emitter.setSourceImage(loader.loadImage("smoke.png"));

		emitter.emissionRateProperty().unbind();
		emitter.setEmissionRate(0.15);
		
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
