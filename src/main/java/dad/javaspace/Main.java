package dad.javaspace;

import java.io.File;
import java.util.ArrayList;

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

import dad.javaspace.HUD.JavaSpaceHUD;
import dad.javaspace.interfacing.controller.LauncherController;
import dad.javaspace.networking.ClientConnectionTask;
import dad.javaspace.networking.ClientGameThread;
import dad.javaspace.networking.NetworkingPlayer;
import dad.javaspace.networking.NetworkingProyectile;
import dad.javaspace.networking.Server;
import dad.javaspace.objects.EntityTypes;
import dad.javaspace.objects.effects.Animations;
import dad.javaspace.objects.effects.ComponentePropulsor;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends GameApplication {

	// Interfaz
	double viewWidth;
	double viewHeight;

	JavaSpaceHUD hud = new JavaSpaceHUD();

	private int spectatorIndex = 0;

	// Mecanica interna
	Entity player = new Entity();

	private ClientModel model = new ClientModel();

	private LauncherController controller = new LauncherController();

	long coolDown = 0, coolDownStars = 0;

	// Hilos para las conexiones
	private ClientConnectionTask clientConnectionTask;

	private Thread clientConnectionThread;

	ClientGameThread clientGameThread;

	// componente fisico
	private PhysicsComponent physics;

	// Servidor
	private Server serverTask;

	private Thread serverThread;

	// Estetica
	AnchorPane rootView;
	private ArrayList<Entity> starArray = new ArrayList<>();
	ArrayList<Entity> starList = new ArrayList<>();

	private Stage gameStage = new Stage();
	private MediaPlayer mp;

	private Button nextButton = new Button(">");
	private Button previousButton = new Button("<");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {

		settings.setManualResizeEnabled(true);
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
	protected void initGame() {
		super.initGame();
		// Cargar el launcher a la ventana
		rootView = controller.getRootView();
		getGameScene().addUINode(rootView);

		clientConnectionTask = new ClientConnectionTask(model);

		controller.getLoadingImage().setVisible(false);

		controller.getLaunchButton().setOnAction(e -> {
			startConnection();
		});

		controller.getCreateRoomButton().setOnAction(e -> {
			startServer();
			controller.getCreateRoomButton().setDisable(true);
		});

		// Botones de modo espectador
		nextButton.setOnAction(e -> spectateNext());
		previousButton.setOnAction(e -> spectatePrevious());
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

	private void startServer() {
		setConnectionConfig();

		serverTask = new Server(model.getNumPlayers(), model.getPort());

		// Boton de crear sala
		serverTask.setOnFailed(e -> controller.getCreateRoomButton().setDisable(false));
		serverTask.setOnSucceeded(e -> controller.getCreateRoomButton().setDisable(false));
		serverTask.setOnCancelled(e -> controller.getCreateRoomButton().setDisable(false));

		serverThread = new Thread(serverTask);

		serverThread.start();
	}

	private void setConnectionConfig() {

		// Establecer los datos al modelo a partir de los datos del launcher
		model.setIp(controller.getModel().getIp());
		model.setName(controller.getModel().getNombreJugador());
		model.setPort(controller.getModel().getPuerto());
		model.setNumPlayers(controller.getModel().getNumPlayers());

	}

	private void startConnection() {

		// Mostrar el icono de carga y bloquear el boton de jugar
		controller.loadingAnimation();
		controller.getLaunchButton().setDisable(true);

		setConnectionConfig();

		// Configurar y arrancar la task para unirse a una partida
		clientConnectionTask = new ClientConnectionTask(model);

		clientConnectionTask.setOnSucceeded(e -> startGame());

		clientConnectionTask.setOnFailed(e -> {
			controller.getLaunchButton().setDisable(false);
			controller.getLoadingImage().setVisible(false);
			// TODO mostrar cuadro de error
		});
		clientConnectionThread = new Thread(clientConnectionTask);

		clientConnectionThread.start();

	}

	private void startGame() {

		getInput().save(model.getProfile());
		for (NetworkingPlayer netPlayers : model.getJugadores()) {
			getGameWorld().addEntity(netPlayers.getEntity());
			getGameWorld().addEntities(netPlayers.getNameText());
		}

		clientGameThread = new ClientGameThread(model);
		clientGameThread.start();

		getGameScene().removeUINode(rootView);
		controller.guardarConfig();
		model.setEnPartida(true);
		controller.getMp().stop();

		// Bindeos modelo de datos
		model.playerXProperty().bind(player.xProperty());
		model.playerYProperty().bind(player.yProperty());
		model.playerRotationProperty().bind(player.angleProperty());

		hud.getModel().shieldProperty().bind(model.shieldProperty());
		hud.getModel().hpProperty().bind(model.hullProperty());

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

		for (Entity entity : ClientUtils.buildWalls(model.getMARGIN_HORIZONTAL(), model.getMARGIN_VERTICAL())) {
			getGameWorld().addEntity(entity);
		}

	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
		if (model.isEnPartida()) {

			if (!clientGameThread.isAlive())
				System.exit(0);

			physics.setAngularVelocity(model.getAngular());

			generateStars();

			if (!getInput().isHeld(KeyCode.W))
				model.setThrust(model.getThrust() * 0.80);
			maxVel();

			hud.getModel().setSpeed(physics.getLinearVelocity().magnitude());
			checkBounds();

			if (model.getHull() <= 0 && model.isPlayerAlive()) {
				die();
			}
			
			checkShots();
		}
	}

	private void die() {
		model.setPlayerAlive(false);
		getInput().clearAll();
		nextButton.setTranslateY(viewHeight / 2);
		nextButton.setTranslateX(viewWidth - nextButton.getWidth());
		previousButton.setTranslateY(viewHeight / 2);
		getGameScene().addUINodes(nextButton, previousButton);
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

		getGameScene().addUINode(hud);
		hud.setTranslateX(hud.getWidth());

	}

	private void maxVel() {
		double maxVelocity = 400;
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

	private void maxVelExperimental() {
		double maxVelocity = 400;

		double x = 0, y = 0;

		if (physics.getLinearVelocity().magnitude() >= maxVelocity) {

			// TODO procesar x e y para que sea la diferencia de la velocidad maxima y la
			// velocidad actual

			physics.setLinearVelocity(physics.getLinearVelocity().subtract(new Point2D(x, y)));
		}

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

	private void doDamage(double damage) {

		if (model.getShield() <= 0)
			model.setShield(-1);

		if (model.getShield() < 0) {
			model.setHull(model.getHull() - damage);
		} else {
			model.setShield(model.getShield() - damage);
		}

	}

	private void checkBounds() {
		if (model.getPlayerX() > model.getMARGIN_HORIZONTAL() || model.getPlayerX() < -model.getMARGIN_HORIZONTAL()
				|| model.getPlayerY() > model.getMARGIN_VERTICAL() || model.getPlayerY() < -model.getMARGIN_VERTICAL())
			if (System.currentTimeMillis() >= model.getCooldownBounds() + 1000) {
				model.setCooldownBounds(System.currentTimeMillis());
				doDamage(0.2);
			}
	}

	private void spectateNext() {
		if (!model.getJugadores().isEmpty())
			if (spectatorIndex + 1 < model.getJugadores().size()) {
				spectatorIndex++;
				if (!model.getJugadores().get(spectatorIndex).isAlive()) {
					spectateNext();
				} else {
					rebindViewPort();
				}
			} else {
				spectatorIndex = 0;
				rebindViewPort();
			}
	}

	private void spectatePrevious() {
		if (!model.getJugadores().isEmpty())
			if (spectatorIndex - 1 >= 0) {
				spectatorIndex--;
				if (!model.getJugadores().get(spectatorIndex).isAlive()) {
					spectateNext();
				} else {
					rebindViewPort();
				}
			} else {
				spectatorIndex = model.getJugadores().size() - 1;
				rebindViewPort();
			}
	}

	private void rebindViewPort() {
		getGameScene().getViewport().xProperty().bind(model.getJugadores().get(spectatorIndex).getEntity().xProperty()
				.subtract(viewWidth / 2).add(player.widthProperty()));
		getGameScene().getViewport().yProperty().bind(model.getJugadores().get(spectatorIndex).getEntity().yProperty()
				.subtract(viewHeight / 2).add((player.heightProperty())));
	}

	private void checkShots() {
		// Crear disparos
		for (NetworkingPlayer ntp : model.getJugadores()) {
			if (ntp.isShooting()) {
				ntp.setShooting(false);
				model.getProjectiles().add(new NetworkingProyectile(ntp.getName(),
						Animations.shootTransition(ntp.getEntity(), getGameWorld())));
				// if (ntp.getEntity().isWithin(new Rectangle2D(arg0, arg1, arg2, arg3)))
				getAudioPlayer().playSound("laser.mp3");
			}
		}

		// Procesar colisiones
		for (NetworkingProyectile projectile : model.getProjectiles()) {
			if (player.isColliding(projectile.getEntity())) {
				doDamage(0.15);
			}

		}

		// Eliminar del mundo si ya no existe

	}

}
