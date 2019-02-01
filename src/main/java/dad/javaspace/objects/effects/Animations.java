package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;
import com.almasb.fxgl.util.Supplier;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {
	
	public ParticleEmitter propulcionEmitter(Entity player) {
		ParticleEmitter emitter;

//		.newFireEmitter() es distinto a .newFireEmitter(int), el segundo da ya una textura toa flama
		emitter = ParticleEmitters.newFireEmitter(0);
		emitter.setAccelerationFunction(new Supplier<Point2D>() {

			@Override
			public Point2D get() {
//				TODO gravedad contraria a la direccion a la que apunta la nave
				return new Point2D(0, 0);
			}
		});
		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
//				TODO posicion alejada x de la nave
				return new Point2D(0, 0);
			}
		});
		emitter.setExpireFunction(e -> Duration.seconds(0.3));
		emitter.setAllowParticleRotation(true);
//		emitter.setEmissionRate(velocidad / 10);
		emitter.setEmissionRate(0.33);

		ParticleComponent component = new ParticleComponent(emitter);
		component.setOnFinished(player::removeFromWorld);
		player.addComponent(component);
		return emitter;
	}
	
	public static void tinkleTransition(Entity player, int duration, double tamMin, double tamMax, double velocity) {
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);

		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(velocity)).repeat(duration)
				.scale(player).from(maxSize).to(minSize);

		maxToMin.buildAndPlay();
	}

	public static void hiperJumpTransition(Entity player, double duration, double translateX, double translateY, GameWorld gameWorld) {
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter hiperJumpEmitter = ParticleEmitters.newExplosionEmitter(50);
//		hiperJumpEmitter.setSourceImage(this.getAssetLoader().loadImage("sparkle-yellow.png"));
		hiperJumpEmitter.setBlendMode(BlendMode.SRC_OVER);
		hiperJumpEmitter.setStartColor(Color.VIOLET);
		hiperJumpEmitter.setEndColor(Color.CYAN);
		hiperJumpEmitter.setScaleFunction(new Function<Integer, Point2D>() {
			// Tama�o de la particula
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D(-0.1, -0.1);
			}
		});
		hiperJumpEmitter.setExpireFunction(e -> Duration.seconds(duration * 10));

		ParticleComponent hiperJumpComponent = new ParticleComponent(hiperJumpEmitter);
		hiperJumpComponent.setOnFinished(player::removeFromWorld);
		Entity hiperJump = Entities.builder().at(playerPosX + translateX, playerPosY + translateY)
				.buildAndAttach(gameWorld);
		hiperJumpComponent.setOnFinished(() -> {
			gameWorld.removeEntity(hiperJump);
		});
		hiperJump.addComponent(hiperJumpComponent);

		// Animacion aumento de tama�o
		Point2D min = new Point2D(0, 0);
		Point2D max = new Point2D(1, 1);
		Entities.animationBuilder().duration(Duration.seconds(duration)).scale(player).from(min).to(max).buildAndPlay();

		// Animacion desplazamiento desde X, Y a 0, 0
		Point2D from = new Point2D(playerPosX + translateX, playerPosY + translateY);
		Point2D to = new Point2D(playerPosX, playerPosY);
		Entities.animationBuilder().duration(Duration.seconds(duration)).translate(player).from(from).to(to)
				.buildAndPlay();
	}
}
