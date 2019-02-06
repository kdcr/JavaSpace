package dad.javaspace.objects.effects;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.util.Function;
import com.almasb.fxgl.util.Supplier;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

		double x = (Math.sin(Math.toRadians(player.getRotation())) * 1500) + xInicio;
		double y = (-Math.cos(Math.toRadians(player.getRotation())) * 1500) + yInicio;

		Point2D puntoInicio = new Point2D(xInicio, yInicio);
		Point2D puntoFin = new Point2D(x, y);

		Entities.animationBuilder().duration(Duration.seconds(0.3)).translate(shoot).from(puntoInicio).to(puntoFin)
				.buildAndPlay().setOnFinished(() -> {
					gameWorld.removeEntity(shoot);
				});
	}

	public static void tinkleTransition(Entity player, int duration, double tamMin, double tamMax, double velocity) {
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);

		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(velocity))
				.repeat(duration).scale(player).from(maxSize).to(minSize);

		maxToMin.buildAndPlay();
	}

	public static void hiperJumpTransition(Entity player, double duration, double translateX, double translateY,
			GameWorld gameWorld) {
		double playerPosX = player.getPosition().getX();
		double playerPosY = player.getPosition().getY();

		ParticleEmitter hiperJumpEmitter = ParticleEmitters.newExplosionEmitter(50);
//		hiperJumpEmitter.setSourceImage(this.getAssetLoader().loadImage("sparkle-yellow.png"));
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
