package dad.javaspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.settings.GameSettings;
import dad.javaspace.interfacing.controller.LauncherController;
import dad.javaspace.networking.ClientGameThread;
import dad.javaspace.objects.EntityTypes;
import dad.javaspace.objects.effects.Animations;
import dad.javaspace.objects.effects.ComponentePropulsor;
import dad.javaspace.ui.NameTag;
import dad.javaspace.ui.ThrustIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends GameApplication {

	// Interfaz
	Text textPixels;
	ThrustIndicator thrustIndicator;
	double viewWidth;
	double viewHeight;

	// Mecanica interna
	Entity player = new Entity();
	NameTag playerNameTag = new NameTag();
	
	private ClientModel model = new ClientModel();
	
	private LauncherController controller = new LauncherController();
	private MediaPlayer mp;
	long coolDown = 0, coolDownStars = 0;

	private ClientConnectionTask clientConnectionTask;

	private Thread clientConnectionThread;

	ClientGameThread clientGameThread;

	private PhysicsComponent physics;

	AnchorPane rootView;

	// Estetica
	private ArrayList<Entity> starArray = new ArrayList<>();
	ArrayList<Entity> starList = new ArrayList<>();

	private Stage gameStage = new Stage();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {

		settings.setWidth(controller.getModel().getResolucion().getX());
		settings.setHeight(controller.getModel().getResolucion().getY());
//		settings.setWidth((int) Screen.getPrimary().getBounds().getWidth());
//		settings.setHeight((int) Screen.getPrimary().getBounds().getHeight());
		// settings.setWidth(720);
		// settings.setHeight(300);
		settings.setTitle("JavaSpace");
		settings.setFullScreenAllowed(controller.getModel().isPantallaCompleta());
		gameStage.setFullScreen(controller.getModel().isPantallaCompleta());
		settings.setVersion(model.getVersion());
		FXGL.configure(this, settings.toReadOnly(), gameStage);

		// Configuracion de los hilos

		/**
		 * Las del juego
		 */
//		settings.setWidth((int) Screen.getPrimary().getBounds().getWidth());
//		settings.setHeight((int) Screen.getPrimary().getBounds().getHeight());
//		settings.setTitle("JavaSpace");
//		settings.setFullScreenAllowed(true);
//		settings.setVersion(model.getVersion());
//		this.settings = settings;
//		gameStage.setFullScreen(true);
//		FXGL.configure(this, settings.toReadOnly(), gameStage);

	}

	@Override
	protected void initInput() {
		Input input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				if (model.getAngular() + 0.5 < 3)
					model.setAngular(model.getAngular() + 0.5);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				if (model.getAngular() - 0.5 > -3)
					model.setAngular(model.getAngular() - 0.5);
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Center") {
			@Override
			protected void onAction() {
				model.setAngular(model.getAngular() * 0.9);

			}
		}, KeyCode.Q);

		input.addAction(new UserAction("Add thrust") {
			@Override
			protected void onAction() {
				addThrust();
			}

		}, KeyCode.W);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				makeShoot();
			}
		}, KeyCode.SPACE);

	}

	@Override
	protected void initUI() {
		super.initUI();

//		thrustIndicator = new ThrustIndicator();
//
//		thrustIndicator.setTranslateX(0);
//		thrustIndicator.setTranslateY(20/* viewHeight - thrustIndicator.getHeight() */);
//
//		getGameScene().addUINode(thrustIndicator);
//		thrustIndicator.setMaxSize(0, 8);
//		thrustIndicator.progressProperty().bind(model.thrustProperty());
	}

	@Override
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("lives", 3);
	}

	@Override
	protected void initGame() {
		super.initGame();
		
		rootView = controller.getRootView();
		getGameScene().addUINode(rootView);

		clientConnectionTask = new ClientConnectionTask(model, getGameWorld());
		clientConnectionTask.setOnSucceeded(e -> startGame());

		controller.getLaunchButton().setOnAction(e -> {
			startConnection();
		});

	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
		if (model.isEnPartida()) {

			physics.setAngularVelocity(model.getAngular());
			generateStars();

			if (!getInput().isHeld(KeyCode.W))
				model.setThrust(model.getThrust() * 0.80);
			maxVel();
		}
	}

	private void startConnection() {
		model.setIp(controller.getModel().getIp());
		model.setName(controller.getModel().getNombreJugador());
		model.setPort(controller.getModel().getPuerto());
		clientConnectionThread = new Thread(clientConnectionTask);

		clientConnectionThread.start();

	}

	private void startGame() {
		
		clientGameThread = new ClientGameThread(model);
		clientGameThread.start();

		getGameScene().removeUINode(rootView);
		controller.guardarConfig();
		model.setEnPartida(true);
		controller.getMp().stop();


		model.playerXProperty().bind(player.xProperty());
		model.playerYProperty().bind(player.yProperty());
		model.playerRotationProperty().bind(player.angleProperty());

		playerNameTag.xProperty().bind(player.xProperty().subtract(50));
		playerNameTag.yProperty().bind(player.yProperty().subtract(50));

		getGameWorld().addEntity(playerNameTag);

		// Estas cuatro lineas se encargan de mover la camara con el jugador, se hace
		// asi para evitar que la camara rote
		viewWidth = getGameScene().getViewport().getWidth();
		viewHeight = getGameScene().getViewport().getHeight();

		getGameScene().getViewport().xProperty()
				.bind(player.xProperty().subtract(viewWidth / 2).add(player.widthProperty()));
		getGameScene().getViewport().yProperty()
				.bind(player.yProperty().subtract(viewHeight / 2).add((player.heightProperty())));

		// Sonido del motor
		mp = new MediaPlayer(new Media(new File("src/main/resources/assets/sounds/thruster.mp3").toURI().toString()));
		mp.setCycleCount(MediaPlayer.INDEFINITE);
		mp.volumeProperty().bind(model.thrustProperty());
		mp.play();

		physics = new PhysicsComponent();

		player.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.polygon(0, 0, 25, 50, 50, 0)));
		player.addComponent(physics);

		physics.setBodyType(BodyType.DYNAMIC);
		// Al jugador se le asigna una textura y se agrega al mundo

		player.setViewFromTexture("navePruebaSmall.png");

		getGameWorld().addEntities(player);

		getPhysicsWorld().setGravity(0, 0);

		initGameUI();

		initGameEffects();

	}

	private void initGameEffects() {
		Animations.hiperJumpTransition(player, 1, -Math.sin(Math.toRadians(player.getRotation())) * 100,
				Math.cos(Math.toRadians(player.getRotation())) * 100, getGameWorld());

		ComponentePropulsor componente = new ComponentePropulsor(player);
		componente.emissionRateProperty().bind(model.thrustProperty());
	}

	private void initGameUI() {
		player.setRenderLayer(RenderLayer.TOP);
		getGameScene().setBackgroundColor(Color.BLACK);

		playerNameTag.setName(model.getName());
		playerNameTag.shieldProperty().bind(model.shieldProperty());
	}

	private void maxVel() {
		double maxVelocity = 1000;
		double x, y;
		if (physics.getLinearVelocity().getX() > maxVelocity || physics.getLinearVelocity().getX() < -maxVelocity) {
			if (physics.getLinearVelocity().getX() > 0)
				x = maxVelocity;
			else
				x = -maxVelocity;
		} else
			x = physics.getLinearVelocity().getX();

		if (physics.getLinearVelocity().getY() > maxVelocity || physics.getLinearVelocity().getY() < -maxVelocity) {
			if (physics.getLinearVelocity().getY() > 0)
				y = maxVelocity;
			else
				y = -maxVelocity;
		} else
			y = physics.getLinearVelocity().getY();

		physics.setLinearVelocity(x, y);
	}

	private void makeShoot() {
		if (System.currentTimeMillis() >= coolDown + 500) {
			coolDown = System.currentTimeMillis();
			model.setCanShoot(true);
			getAudioPlayer().playSound("laser.mp3");
			Animations.shootTransition(player, getGameWorld());
		}
	}

	private void addThrust() {
		if (model.getThrust() + 0.5 < 8)
			model.setThrust(model.getThrust() + 0.1);

		physics.applyBodyForceToCenter(new Vec2(model.getThrust() * -Math.sin(physics.getBody().getAngle()),
				model.getThrust() * Math.cos(physics.getBody().getAngle())));
	}

	/*
	 * genera una estrella cada 80 milisegundos, ademas borra de forma aleatoria
	 * algunas que ya existian de antes
	 */
	private void generateStars() {
		if (System.currentTimeMillis() > coolDownStars + 160) {
			coolDownStars = System.currentTimeMillis();

			Entity newStar = new Entity();
			newStar.setRenderLayer(RenderLayer.BACKGROUND);
			newStar.setViewFromTexture("star_placeholder.png");

			Animations.tinkleTransition(newStar, 50, Math.random(), (Math.random() * 1) + 1,
					(Math.random() * 1.5) + 0.5);

			newStar.setType(EntityTypes.STAR);
			starArray.add(newStar);
			getGameWorld().addEntity(newStar);

			newStar.setX(
					getGameScene().getViewport().getX() + (Math.random() * getGameScene().getViewport().getWidth()));
			newStar.setY(
					getGameScene().getViewport().getY() + (Math.random() * getGameScene().getViewport().getHeight()));

			starList = new ArrayList<>();

			for (Entity entity : starArray) {
				if (Math.random() > 0.8) {
					starList.add(entity);
				}
			}

			starArray.removeAll(starList);

			getGameWorld().removeEntities(starList);

		}
	}

}
