package dad.javaspace.objects.effects;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.time.Timer;
import com.almasb.fxgl.util.Function;

import dad.javaspace.objects.EntityTypes;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {

	public static Entity shootTransition(Entity player, GameWorld gameWorld) {
		
		Entity shoot = new Entity();
		shoot.setViewFromTexture("laser.png");
		shoot.setRotation(player.getRotation());
		shoot.setOnNotActive(() -> {
			explotionTransition(shoot, gameWorld);
		});
		gameWorld.addEntities(shoot);
		double xInicio = player.getCenter().getX() + 9;
		double yInicio = player.getCenter().getY() + 9;
		Point2D puntoInicio = new Point2D(xInicio, yInicio);
		double xFin = (Math.sin(Math.toRadians(player.getRotation())) * 800) + xInicio;
		double yFin = (-Math.cos(Math.toRadians(player.getRotation())) * 800) + yInicio;
		Point2D puntoFin = new Point2D(xFin, yFin);
		Entities.animationBuilder().duration(Duration.seconds(0.3)).translate(shoot).from(puntoInicio).to(puntoFin)
				.buildAndPlay().setOnFinished(() -> {
					if (shoot.isActive()) {
						gameWorld.removeEntity(shoot);
					}
				});
		return shoot;
	}

	public static void hitTransition(Entity player, GameWorld gameWorld) {

//		Terminar animacion. posicion, rotacion, radio, particulas
		double playerPosX = player.getCenter().getX() + 25;
		double playerPosY = player.getCenter().getY() + 25;

		ParticleEmitter hitEmitter = ParticleEmitters.newExplosionEmitter(50);
		hitEmitter.setExpireFunction(e -> Duration.seconds(10));
		hitEmitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				Point2D punto = new Point2D(35, 35);
				return punto;
			}
		});
		
		hitEmitter.setExpireFunction(e -> Duration.seconds(1));
		hitEmitter.setNumParticles((int) ((Math.random() * 5) + 3));
		hitEmitter.setAllowParticleRotation(true);
		hitEmitter.setSize(10, 25);

		ParticleComponent hitComponent = new ParticleComponent(hitEmitter);
		hitComponent.setOnFinished(player::removeFromWorld);

		Entity hit = new Entity();
		hit.addComponent(hitComponent);
		hit.setRotation(player.getRotation());
		gameWorld.addEntities(hit);

		hitEmitter.setExpireFunction(new Function<Integer, Duration>() {

			@Override
			public Duration apply(Integer arg) {
				return Duration.seconds(2);
			}
		});

		hitComponent.setOnFinished(() -> {
			player.getComponents().removeValueByIdentity(hitComponent);
			gameWorld.removeEntity(hit);
		});

		Entities.animationBuilder().duration(Duration.seconds(2)).scale(new Entity()).buildAndPlay()
				.setOnFinished(() -> {
					if (hit.isActive())
						gameWorld.removeEntity(hit);
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
		Entity hiperJump = Entities.builder().at(playerPosX + translateX, playerPosY + translateY)
				.renderLayer(RenderLayer.BOTTOM).buildAndAttach(gameWorld);
		hiperJump.addComponent(hiperJumpComponent);
		hiperJump.setType(EntityTypes.WARPFX);
		hiperJumpComponent.setOnFinished(hiperJump::removeFromWorld);

		// Animacion aumento de tama�o
		Point2D min = new Point2D(0, 0);
		Point2D max = new Point2D(1, 1);
		Entities.animationBuilder().duration(Duration.seconds(duration)).scale(player).from(min).to(max).buildAndPlay()
				.setOnFinished(() -> {

				});

		// Animacion desplazamiento desde X, Y a 0, 0
		Point2D from = new Point2D(playerPosX + translateX, playerPosY + translateY);
		Point2D to = new Point2D(playerPosX, playerPosY);
		Entities.animationBuilder().duration(Duration.seconds(duration)).translate(player).from(from).to(to)
				.buildAndPlay().setOnFinished(() -> {

				});
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
		Entity explotion = Entities.builder().at(playerPosX + 0, playerPosY + 0).buildAndAttach(gameWorld);
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
