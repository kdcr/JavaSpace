package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.util.Function;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {
	
	public static void tinkleTransition(Entity player, int duration, double tamMin, double tamMax) {
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);

		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(0.3)).repeat(duration)
				.scale(player).from(maxSize).to(minSize);

		maxToMin.buildAndPlay();
	}

	public void hiperJumpTransition(Entity player, double duration, double translateX, double translateY, GameWorld gameWorld) {
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter hiperJumpEmitter = ParticleEmitters.newExplosionEmitter(50);
//		hiperJumpEmitter.setSourceImage(this.getAssetLoader().loadImage("sparkle-yellow.png"));
		hiperJumpEmitter.setBlendMode(BlendMode.SRC_OVER);
		hiperJumpEmitter.setStartColor(Color.VIOLET);
		hiperJumpEmitter.setEndColor(Color.CYAN);
		hiperJumpEmitter.setScaleFunction(new Function<Integer, Point2D>() {
			// Tamaño de la particula
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

		// Animacion aumento de tamaño
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
