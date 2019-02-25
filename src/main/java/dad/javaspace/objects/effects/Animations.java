package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.util.Function;

import dad.javaspace.objects.EntityTypes;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {

	public static Entity shootTransition(Entity player, GameWorld gameWorld, EntityTypes entityType) {

		Entity shoot = new Entity();
		shoot.setViewFromTexture("laser.png");
		shoot.setType(entityType);
		shoot.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.polygon(0, 0, 16, 32, 32, 0)));
		shoot.addComponent(new CollidableComponent(true));
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
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter hitEmitter = ParticleEmitters.newExplosionEmitter(40);
		hitEmitter.setExpireFunction(e -> Duration.seconds(1));
		hitEmitter.setSpawnPointFunction(new Function<Integer, Point2D>() {
			@Override
			public Point2D apply(Integer arg) {
				Point2D punto = new Point2D(35, 35);
				return punto;
			}
		});
		hitEmitter.setExpireFunction(e -> Duration.seconds(2));
		hitEmitter.setNumParticles((int) ((Math.random() * 10) + 5));
		hitEmitter.setAllowParticleRotation(true);
		hitEmitter.setSize(5, 20);
		hitEmitter.setEmissionRate(1);

		ParticleComponent hitComponent = new ParticleComponent(hitEmitter);

		Entity hit = new Entity();
		hit.setPosition(playerPosX, playerPosY);
		hit.addComponent(hitComponent);
		gameWorld.addEntities(hit);

		hitComponent.setOnFinished(hit::removeFromWorld);

		Entities.animationBuilder().duration(Duration.seconds(1)).scale(new Entity()).buildAndPlay()
				.setOnFinished(() -> {
					gameWorld.removeEntity(hit);
				});

	}

	public static void hiperJumpTransition(Entity player, GameWorld gameWorld) {
		double duration = 1;
		double translateX = -Math.sin(Math.toRadians(player.getRotation())) * 100;
		double translateY = Math.cos(Math.toRadians(player.getRotation())) * 100;

		double playerPosX = player.getX();
		double playerPosY = player.getY();

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

	public static void tinkleTransition(Entity player) {
		int duration = 50;
		double tamMin = Math.random();
		double tamMax = (Math.random() * 1) + 1;
		double velocity = (Math.random() * 1.5) + 0.5;
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);
		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(velocity))
				.repeat(duration).scale(player).from(maxSize).to(minSize);
		maxToMin.buildAndPlay();
	}

	public static void hideEndScreen(VBox node, boolean oculta, double viewHeight) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(node);
		if (oculta) {
			transition.setFromY(viewHeight - 20);
			transition.setToY(viewHeight / 2 - node.getPrefHeight() / 2);
		} else {
			transition.setFromY(viewHeight / 2 - node.getPrefHeight() / 2);
			transition.setToY(viewHeight - 20);
		}

		transition.play();
	}
}
