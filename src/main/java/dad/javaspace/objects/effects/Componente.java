package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;
import com.almasb.fxgl.util.Supplier;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Componente extends Component {

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
	}

	@Override
	public void onAdded() {
		// TODO Auto-generated method stub
		
		super.onAdded();
	}

	@Override
	public void onUpdate(double tpf) {
		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D(-Math.sin(Math.toRadians(player.getRotation())) * 50, Math.cos(Math.toRadians(player.getRotation())) * 50);
			}
		});
	}

	@Override
	public void onRemoved() {
		// TODO Auto-generated method stub
		super.onRemoved();
	}

}
