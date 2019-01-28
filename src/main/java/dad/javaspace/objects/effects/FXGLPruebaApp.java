package dad.javaspace.objects.effects;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.SequentialAnimation;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entities.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.collision.ContactID.Type;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.time.Timer;
import com.almasb.fxgl.util.Function;
import com.almasb.fxgl.util.Supplier;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

public class FXGLPruebaApp extends GameApplication {

	private final int WIDTH_SCREEN = 1500;
	private final int HEIGDTH_SCREEN = 500;

	private final int MAX_SPEED = 10;

//	Control
	Input input;

//	Imagenes
//	ImageView jugadorImage = new ImageView(getAssetLoader().loadImage("image.png"));
//	ImageView bulletImage = new ImageView(this.getAssetLoader().loadImage("preloader.gif"));

//	Entidades
	private Entity jugador;

//	Particle
	private ParticleComponent component;
	private ParticleEmitter emitter;

//	Variables
	private double velocidad = 0;
	private char direccion = 'w';

	@Override
	protected void initGame() {
//		new Rectangle(10, 10, Color.BLUEVIOLET
		getGameScene().setBackgroundColor(Color.BLACK);

//		jugador = Entities.builder().at(250, 250).viewFromNode(jugadorImage).buildAndAttach(getGameWorld());
		jugador = Entities.builder().at(250, 250).buildAndAttach(getGameWorld());
		jugador.setViewFromTexture("image.png");

		emitter = ParticleEmitters.newFireEmitter();
//		emitter.setSourceImage(this.getAssetLoader().loadImage("image.gif"));
		emitter.setAccelerationFunction(new Supplier<Point2D>() {

			@Override
			public Point2D get() {
				// TODO Auto-generated method stub
				return new Point2D(-50, 50);
			}
		});
		emitter.setSpawnPointFunction(new Function<Integer, Point2D>() {

			@Override
			public Point2D apply(Integer arg) {
				// TODO Auto-generated method stub
				return new Point2D(15, 15);
			}
		});
		emitter.setBlendMode(BlendMode.SRC_OVER);
		emitter.setStartColor(Color.ORANGERED);
		emitter.setEndColor(Color.YELLOW);
		emitter.setExpireFunction(e -> Duration.seconds(0.5));
		emitter.setEmissionRate(velocidad / 10);
		component = new ParticleComponent(emitter);

		component.setOnFinished(jugador::removeFromWorld);

		jugador.addComponent(component);

		hiperJumpTransition(jugador, 0.3, -150, 0);
	}

	public void bulletTransition(Entity player, double duration, double tamMin, double tamMax) {
		Point2D defaultSize = new Point2D(1, 1);
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);
		Entities.animationBuilder().duration(Duration.seconds(duration)).scale(player).from(minSize).to(maxSize)
				.buildAndPlay().setOnFinished(() -> {
					Entities.animationBuilder().duration(Duration.seconds(duration)).scale(player).from(maxSize)
							.to(defaultSize).buildAndPlay();
				});
	}

	public void hiperJumpTransition(Entity player, double duration, double translateX, double translateY) {

		Point2D min = new Point2D(0, 0);
		Point2D max = new Point2D(1, 1);
		Entities.animationBuilder().duration(Duration.seconds(duration)).scale(player).from(min).to(max).buildAndPlay();

		Point2D from = new Point2D(player.getPosition().getX() + translateX, player.getPosition().getY() + translateY);
		Point2D to = new Point2D(player.getPosition().getX(), player.getPosition().getY());
		Entities.animationBuilder().duration(Duration.seconds(duration)).translate(player).from(from).to(to)
				.buildAndPlay();

	}

	@Override
	protected void initPhysics() {
		// 3. get physics world and register a collision handler
		// between Type.PLAYER and Type.ENEMY

		PhysicsComponent physics = new PhysicsComponent();

		physics.setBodyType(BodyType.DYNAMIC);

		physics.setOnPhysicsInitialized(() -> {
			physics.setLinearVelocity(5, 5);
		});

		jugador.addComponent(physics);

	}

	@Override
	protected void initInput() {

		input = getInput();

		input.addAction(new UserAction("Shoot") {
			Entity explosion;

			@Override
			protected void onActionBegin() {
				explosion = new Entity();
				explosion.setPosition(input.getMousePositionWorld());

				// 2. create and configure emitter + component
				ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
				emitter.setBlendMode(BlendMode.SRC_OVER);
				// emitter.setEndColor(Color.WHITE);
				// emitter.setExpireFunction((i, x, y) -> Duration.seconds(5));
				ParticleComponent component = new ParticleComponent(emitter);

				// we also want the entity to destroy itself when particle component is done
				component.setOnFinished(explosion::removeFromWorld);

				// 3. add control to entity
				explosion.addComponent(component);

				// 4. add entity to game world
				getGameWorld().addEntity(explosion);
			}

			@Override
			protected void onAction() {

			}

			@Override
			protected void onActionEnd() {
				getGameWorld().removeEntity(explosion);
			}
		}, KeyCode.SPACE);

		input.addAction(new UserAction("Move Right") {
			ParticleComponent component;
			ParticleEmitter emitter;

			@Override
			protected void onActionBegin() {

			}

			@Override
			protected void onAction() {
				direccion = 'd';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}

			@Override
			protected void onActionEnd() {
			}

		}, KeyCode.D);

		input.addAction(new UserAction("Move Left") {
			Entity explosion;

			@Override
			protected void onActionBegin() {
				// TODO Auto-generated method stub
				super.onActionBegin();
			}

			@Override
			protected void onAction() {
				direccion = 'a';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}

			@Override
			protected void onActionEnd() {
				// TODO Auto-generated method stub
				super.onActionEnd();
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Move Up") {
			Entity explosion;

			@Override
			protected void onActionBegin() {
				// TODO Auto-generated method stub
				super.onActionBegin();
			}

			@Override
			protected void onAction() {
				direccion = 'w';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}

			@Override
			protected void onActionEnd() {
				// TODO Auto-generated method stub
				super.onActionEnd();
			}
		}, KeyCode.W);

		input.addAction(new UserAction("Move Down") {
			Entity explosion;

			@Override
			protected void onActionBegin() {
				// TODO Auto-generated method stub
				super.onActionBegin();
			}

			@Override
			protected void onAction() {
				direccion = 's';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}

			@Override
			protected void onActionEnd() {
				// TODO Auto-generated method stub
				super.onActionEnd();
			}
		}, KeyCode.S);
	}

	@Override
	protected void initUI() {
		Text textPixels = new Text();
		textPixels.setTranslateX(50); // x = 50
		textPixels.setTranslateY(100); // y = 100

		getGameScene().addUINode(textPixels); // add to the scene graph

		textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString());
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("pixelsMoved", 0);
	}

	@Override
	protected void onUpdate(double tpf) {

		emitter.setEmissionRate(velocidad / 10);
//		jugador.setPosition(getInput().getMousePositionWorld());

		if (jugador.getPosition().getX() > (getWidth() - 32)) {
			jugador.setPosition(getWidth() - 32, jugador.getPosition().getY());
		}
		if (jugador.getPosition().getX() < 0) {
			jugador.setPosition(0, jugador.getPosition().getY());
		}
		if (jugador.getPosition().getY() > (getHeight() - 32)) {
			jugador.setPosition(jugador.getPosition().getX(), getHeight() - 32);
		}
		if (jugador.getPosition().getY() < 0) {
			jugador.setPosition(jugador.getPosition().getX(), 0);
		}
		if (velocidad != 0) {
			moverJugador(direccion, 0);
			velocidad = velocidad - 0.5;
		}

	}

	public void moverJugador(char direccion, int speed) {
		if (direccion == 'd' && jugador.getPosition().getX() < (getWidth() - 32)) {
			jugador.translateX(speed + velocidad);
			getGameState().increment("pixelsMoved", +1);
		}
		if (direccion == 'a' && jugador.getPosition().getX() > 0) {
			jugador.translateX(-speed + Math.negateExact((int) velocidad));
			getGameState().increment("pixelsMoved", +1);
		}
		if (direccion == 's' && jugador.getPosition().getY() < (getHeight() - 32)) {
			jugador.translateY(speed + velocidad);
			getGameState().increment("pixelsMoved", +1);
		}
		if (direccion == 'w' && jugador.getPosition().getY() > 0) {
			jugador.translateY(-speed + Math.negateExact((int) velocidad));
			getGameState().increment("pixelsMoved", +1);
		}
	}

	public void moverBullet(Entity entity, char direccion) {
		if (direccion == 'd')
			jugador.translateX(8);
		if (direccion == 'a')
			jugador.translateX(-8);
		if (direccion == 's')
			jugador.translateY(8);
		if (direccion == 'w')
			jugador.translateY(-8);
	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(WIDTH_SCREEN);
		settings.setHeight(HEIGDTH_SCREEN);
		settings.setTitle("Game App");
		settings.setVersion("0.1");

	}

	public static void main(String[] args) {
		launch(args);

	}

}
