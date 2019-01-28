package dad.javaspace.objects.effects;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entities.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.collision.ContactID.Type;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.saving.DataFile;
import com.almasb.fxgl.settings.GameSettings;
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

public class FXGLFPShoot extends GameApplication {

	private final int WIDTH_SCREEN = 450;
	private final int HEIGDTH_SCREEN = 350;

	private final int MAX_SPEED = 20;

//	Control
	Input input;

//	Imagenes
	ImageView jugadorImage = new ImageView(new Image("pointer.png"));
	ImageView bulletImage = new ImageView(new Image("preloader.gif"));

//	Entidades
	private Entity jugador;

//	Variables
	private double velocidad = 0;
	private char direccion = 'w';

	@Override
	protected void initGame() {
//		new Rectangle(10, 10, Color.BLUEVIOLET

//		getAssetLoader().loadCursorImage("pointer.png");
		
		
		ParticleEmitter emitter = new ParticleEmitter();
		emitter.setStartColor(Color.AZURE);
		emitter.setBlendMode(BlendMode.SOFT_LIGHT);
		emitter.setEndColor(Color.RED);
		
		jugador = Entities.builder().at(250, 250).viewFromNode(jugadorImage).buildAndAttach(getGameWorld());
		

		hiperJumpTransition(jugadorImage, 0.4, -350, 0);
		bulletTransition(jugadorImage, 1, 0.5, 1);
	}

	public void bulletTransition(ImageView image, double duration, double tamMin, double tamMax) {
		ScaleTransition scaleA = new ScaleTransition();
		scaleA.setNode(image);
		scaleA.setDuration(Duration.seconds(duration));
		scaleA.setFromX(tamMax);
		scaleA.setFromY(tamMax);
		scaleA.setToX(tamMin);
		scaleA.setToY(tamMin);

		ScaleTransition scaleB = new ScaleTransition();
		scaleB.setNode(image);
		scaleB.setDuration(Duration.seconds(duration));
		scaleB.setFromX(tamMin);
		scaleB.setFromY(tamMin);
		scaleB.setToX(tamMax);
		scaleB.setToY(tamMax);

		SequentialTransition sequential = new SequentialTransition(scaleA, scaleB);
		sequential.play();

		sequential.setOnFinished(e -> {
			sequential.play();
		});
	}

	public void hiperJumpTransition(ImageView image, double duration, double translateX, double translateY) {
		ScaleTransition scale = new ScaleTransition();
		scale.setNode(image);
		scale.setDuration(Duration.seconds(duration));
		scale.setFromX(0);
		scale.setFromY(0);
		scale.setToX(1);
		scale.setToY(1);
		scale.setInterpolator(Interpolator.EASE_IN);

		TranslateTransition translate = new TranslateTransition();
		translate.setNode(image);
		translate.setDuration(Duration.seconds(duration));
		translate.setFromX(translateX);
		translate.setToX(0);
		translate.setFromY(translateY);
		translate.setToY(0);
		translate.setInterpolator(Interpolator.EASE_IN);

		ParallelTransition parallel = new ParallelTransition(scale, translate);
		parallel.play();
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
			@Override
			protected void onAction() {

			}

		}, KeyCode.SPACE);

		input.addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				direccion = 'd';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				direccion = 'a';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Move Up") {
			@Override
			protected void onAction() {
				direccion = 'w';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
			}
		}, KeyCode.W);

		input.addAction(new UserAction("Move Down") {
			@Override
			protected void onAction() {
				direccion = 's';
				moverJugador(direccion, 1);
				if (velocidad < MAX_SPEED)
					velocidad = velocidad + 2;
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

		jugador.setPosition(getInput().getMousePositionWorld());

//		
//		if (jugador.getPosition().getX() > (getWidth() - 32)) {
//			jugador.setPosition(getWidth() - 32, jugador.getPosition().getY());
//		}
//		if (jugador.getPosition().getX() < 0) {
//			jugador.setPosition(0, jugador.getPosition().getY());
//		}
//		if (jugador.getPosition().getY() > (getHeight() - 32)) {
//			jugador.setPosition(jugador.getPosition().getX(), getHeight() - 32);
//		}
//		if (jugador.getPosition().getY() < 0) {
//			jugador.setPosition(jugador.getPosition().getX(), 0);
//		}
//		if (velocidad != 0) {
//			moverJugador(direccion, 0);
//			velocidad = velocidad - 0.5;
//		}

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
	protected void preInit() {
		super.preInit();
	}

	@Override
	protected void onPostUpdate(double tpf) {
		super.onPostUpdate(tpf);
	}

	@Override
	protected DataFile saveState() {
		return super.saveState();
	}

	@Override
	protected void loadState(DataFile dataFile) {
		super.loadState(dataFile);
	}

	@Override
	protected void exit() {
		super.exit();
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
