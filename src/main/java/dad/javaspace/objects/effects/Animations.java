package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.RenderLayer;
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

	public static void shootTransition(Entity player, GameWorld gameWorld) {
		Entity shoot = new Entity();
		shoot.setViewFromTexture("laser.png");
		shoot.setRotation(player.getRotation());
		gameWorld.addEntities(shoot);
		double xInicio = player.getCenter().getX() + 33;
		double yInicio = player.getCenter().getY() + 25;
		Point2D puntoInicio = new Point2D(xInicio, yInicio);
		double xFin = (Math.sin(Math.toRadians(player.getRotation())) * 1500) + xInicio;
		double yFin = (-Math.cos(Math.toRadians(player.getRotation())) * 1500) + yInicio;
		Point2D puntoFin = new Point2D(xFin, yFin);
		Entities.animationBuilder().duration(Duration.seconds(0.3)).translate(shoot).from(puntoInicio).to(puntoFin)
				.buildAndPlay().setOnFinished(() -> {
					gameWorld.removeEntity(shoot);
				});
	}

	public static void hiperJumpTransition(Entity player, double duration, double translateX, double translateY,
			GameWorld gameWorld) {
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter hiperJumpEmitter = ParticleEmitters.newExplosionEmitter(50);
		hiperJumpEmitter.setBlendMode(BlendMode.SRC_OVER);
		hiperJumpEmitter.setStartColor(Color.VIOLET);
		hiperJumpEmitter.setEndColor(Color.CYAN);
		hiperJumpEmitter.setScaleFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D(-0.1, -0.1);
			}
		});
		hiperJumpEmitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				Point2D punto = new Point2D(25, 25);
				return punto;
			}
		});
		hiperJumpEmitter.setExpireFunction(e -> Duration.seconds(duration * 10));

		ParticleComponent hiperJumpComponent = new ParticleComponent(hiperJumpEmitter);
		hiperJumpComponent.setOnFinished(player::removeFromWorld);
		Entity hiperJump = Entities.builder().at(playerPosX + translateX, playerPosY + translateY)
				.renderLayer(RenderLayer.BOTTOM).buildAndAttach(gameWorld);
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

	public static void explotionTransition(Entity player, GameWorld gameWorld) {
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter explotionEmitter = ParticleEmitters.newExplosionEmitter(100);
		explotionEmitter.setBlendMode(BlendMode.SRC_OVER);
		explotionEmitter.setStartColor(Color.RED);
		explotionEmitter.setEndColor(Color.ORANGE);
		explotionEmitter.setAllowParticleRotation(true);
		explotionEmitter.setScaleFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				return new Point2D(-0.1, -0.1);
			}
		});
		explotionEmitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				Point2D punto = new Point2D(35, 35);
				return punto;
			}
		});
		explotionEmitter.setExpireFunction(e -> Duration.seconds(1));
		explotionEmitter.setNumParticles((int) ((Math.random() * 10) + 5));

		ParticleComponent explotionComponent = new ParticleComponent(explotionEmitter);
		explotionComponent.setOnFinished(player::removeFromWorld);
		Entity explotion = Entities.builder().at(playerPosX + 0, playerPosY + 0).renderLayer(RenderLayer.TOP)
				.buildAndAttach(gameWorld);
		explotionComponent.setOnFinished(() -> {
			gameWorld.removeEntity(explotion);
		});
		explotion.addComponent(explotionComponent);

		Point2D min = new Point2D(0, 0);
		Point2D max = new Point2D(1, 1);
		Entities.animationBuilder().duration(Duration.seconds(0.5)).scale(player).from(max).to(min).buildAndPlay()
				.setOnFinished(() -> {
					player.setScaleX(1);
					player.setScaleY(1);
				});
	}

	public static void tinkleTransition(Entity player, int duration, double tamMin, double tamMax, double velocity) {
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);
		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(velocity))
				.repeat(duration).scale(player).from(maxSize).to(minSize);
		maxToMin.buildAndPlay();
	}
}
