package dad.javaspace;

import java.io.File;
import java.util.ArrayList;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
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
import dad.javaspace.radar.RadarController;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends GameApplication {

	// Interfaz
	double viewWidth;
	double viewHeight;

	JavaSpaceHUD hud = new JavaSpaceHUD();
	RadarController radar = new RadarController();

	private int spectatorIndex = 0;

	// Mecanica interna
	Input input;
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

	ComponentePropulsor componentePropulsor;

	private Stage gameStage = new Stage();
	private MediaPlayer mp;
	private MediaPlayer musica;
	private ArrayList<Media> listaCanciones;
	private int cancionActual = 0;

	private Button nextButton = new Button();
	private Button previousButton = new Button();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {

		settings.setManualResizeEnabled(true);
		settings.setWidth(controller.getModel().getResolucion().getX());
		settings.setHeight(controller.getModel().getResolucion().getY());

		settings.setTitle("JavaSpace");
		settings.setFullScreenAllowed(controller.getModel().isPantallaCompleta());
		gameStage.setFullScreen(controller.getModel().isPantallaCompleta());
		settings.setVersion(model.getVersion());
		settings.setMenuEnabled(false);

		FXGL.configure(this, settings.toReadOnly(), gameStage);
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

		// Mensaje de estado del launcher
		// FIXME rompe el juego, ni siquiera importa si se desbindea etc
		// controller.getLabelInfo().textProperty().bind(model.connectionStateProperty());
	}

	@Override
	protected void initInput() {
		input = getInput();

		input.addAction(new UserAction("Rotate Right") {
			@Override
			protected void onAction() {
				if (model.isPlayerAlive())
					if (model.getAngular() + 0.5 < 3)
						model.setAngular(model.getAngular() + 0.5);
			}
		}, KeyCode.D);

		input.addAction(new UserAction("Rotate Left") {
			@Override
			protected void onAction() {
				if (model.isPlayerAlive())
					if (model.getAngular() - 0.5 > -3)
						model.setAngular(model.getAngular() - 0.5);
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Center") {
			@Override
			protected void onAction() {
				if (model.isPlayerAlive())
					model.setAngular(model.getAngular() * 0.9);

			}
		}, KeyCode.Q);

		input.addAction(new UserAction("Add thrust") {
			@Override
			protected void onAction() {
				if (model.isPlayerAlive())
					addThrust();
			}

		}, KeyCode.W);

		input.addAction(new UserAction("Shoot") {
			@Override
			protected void onAction() {
				if (model.isPlayerAlive())
					makeShoot();
			}
		}, KeyCode.SPACE);

	}

	private void startServer() {
		setServerConnectionConfig();

		serverTask = new Server(model.getNumPlayers(), model.getPort());

		// Boton de crear sala
		serverTask.setOnFailed(e -> controller.getCreateRoomButton().setDisable(false));
		serverTask.setOnSucceeded(e -> controller.getCreateRoomButton().setDisable(false));
		serverTask.setOnCancelled(e -> controller.getCreateRoomButton().setDisable(false));

		serverThread = new Thread(serverTask);

		serverThread.start();
	}

	private void setServerConnectionConfig() {

		// Establecer los datos al modelo a partir de los datos del launcher
		model.setIp(controller.getModel().getIp());
		model.setName(controller.getModel().getNombreJugador());
		model.setPort(controller.getModel().getPuerto());
		model.setNumPlayers(controller.getModel().getNumPlayers());

	}

	private void startConnection() {

		model.setSkin(controller.getModel().getSelectedSkin() + "");

		controller.getLabelInfo().setVisible(true);

		// Mostrar el icono de carga y bloquear el boton de jugar
		controller.loadingAnimation();
		controller.getLaunchButton().setDisable(true);

		setServerConnectionConfig();

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

		// Añadir las entidades de otros jugadores
		for (NetworkingPlayer netPlayers : model.getJugadores()) {
			getGameWorld().addEntity(netPlayers.getEntity());
			getGameWorld().addEntities(netPlayers.getNameText());
		}

		// Arrancar el hilo para la conexion del juego
		clientGameThread = new ClientGameThread(model);
		clientGameThread.start();

		// Eliminar la interfaz del launcher
		clearLauncher();

		// Bindeos modelo de datos
		model.playerXProperty().bind(player.xProperty());
		model.playerYProperty().bind(player.yProperty());
		model.playerRotationProperty().bind(player.angleProperty());

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
		
		// Música del juego
		listaCanciones = new ArrayList<>();
		listaCanciones.add(new Media(new File("src/main/resources/music/track01.mp3").toURI().toString()));
		listaCanciones.add(new Media(new File("src/main/resources/music/track02.mp3").toURI().toString()));
		listaCanciones.add(new Media(new File("src/main/resources/music/track03.mp3").toURI().toString()));
		listaCanciones.add(new Media(new File("src/main/resources/music/track04.mp3").toURI().toString()));

		playMediaTracks(new ArrayList<>(listaCanciones));

		
		// Inicializar las fisicas del propio jugador y el modelo de colisiones
		physics = new PhysicsComponent();
		player.addComponent(physics);
		physics.setBodyType(BodyType.DYNAMIC);

		// Colocar al jugador en una zona aleatoria del mundo
		player.setX(Math.random() * model.getMARGIN_HORIZONTAL());
		player.setY(Math.random() * model.getMARGIN_VERTICAL());
		player.setRotation(Math.random() * 360);

		// 0 gravedad
		getPhysicsWorld().setGravity(0, 0);

		player.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.polygon(0, 0, 25, 50, 50, 0)));
		player.addComponent(new CollidableComponent(true));

		// Al jugador se le asigna una textura y se agrega al mundo

		player.setViewFromTexture("Nave" + controller.getModel().getSelectedSkin() + ".png");
		player.setType(EntityTypes.PLAYER);

		getGameWorld().addEntities(player);

		initGameUI();

		initGameEffects();

		for (Entity entity : ClientUtils.buildWalls(model.getMARGIN_HORIZONTAL(), model.getMARGIN_VERTICAL())) {
			getGameWorld().addEntity(entity);
		}

		// Este timer hace que el juego se brickee
//		getMasterTimer().runOnceAfter(() -> {
//			for (Entity entity : getGameWorld().getEntities()) {
//				if (entity.getType() == EntityTypes.WARPFX)
//					getGameWorld().removeEntity(entity);
//			}
//		}, Duration.seconds(5));
	}

	private void clearLauncher() {
		getGameScene().removeUINode(rootView);
		controller.guardarConfig();
		model.setEnPartida(true);
		controller.getMp().stop();

	}

	@Override
	protected void initPhysics() {
		super.initPhysics();

		// Colision de un proyectil enemigo con la nave propia
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, EntityTypes.ENEMY_LASER) {

			@Override
			protected void onCollisionBegin(Entity player, Entity laser) {
				doDamage(0.15);

				getGameWorld().removeEntity(laser);
			}
		});

		// Colision del proyectil propio
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.ENEMY_PLAYER, EntityTypes.LASER) {

			@Override
			protected void onCollisionBegin(Entity player, Entity laser) {
				getGameWorld().removeEntity(laser);
			}
		});
	}

	@Override
	protected void onUpdate(double tpf) {
		super.onUpdate(tpf);
		if (model.isEnPartida()) {

			// Si se cae la conexión sale del programa
			// TODO cambiar a volver al launcher
			if (!clientGameThread.isAlive())
				System.exit(0);

			physics.setAngularVelocity(model.getAngular());

			// Llamar al método para generar estrellas
			generateStars();

			// Si no se esta pulsando W, decrementa el empuje del motor
			if (!getInput().isHeld(KeyCode.W))
				model.setThrust(model.getThrust() * 0.80);

			// Limitar la velocidad
			maxVel();

			// Como no hay property de la velocidad lineal, se actualiza el hud a cada frame
			hud.getModel().setSpeed((int) physics.getLinearVelocity().magnitude());

			// Comprueba que el jugador nos e haya salido
			checkBounds();

			if (model.getHull() <= 0 && model.isPlayerAlive()) {
				die();
			}

			// Comprobar el estado de otras naves (si ya fue destruida)
			for (NetworkingPlayer ntp : model.getJugadores()) {
				if (ntp.getHull() <= 0 && ntp.isAlive()) {
					ntp.setAlive(false);
					ntp.getComponentePropulsor().onShipDestroyed();
				}
			}
			
			// Recargar escudos
			reloadShield();

			// Comprobar nuevos disparos de otros jugadores
			checkShots();
		}
	}

	private void die() {
		model.setPlayerAlive(false);

		// Anadiendo CSS a los botones
		nextButton.getStylesheets().setAll("/css/forwardbutton.css");
		previousButton.getStylesheets().setAll("/css/previousbutton.css");
		previousButton.setMinWidth(120);
		previousButton.setMinHeight(120);
		nextButton.setMinWidth(120);
		nextButton.setMinHeight(120);

		nextButton.setTranslateY(viewHeight / 2);
		nextButton.setTranslateX(viewWidth - nextButton.getMinWidth());
		previousButton.setTranslateY(viewHeight / 2);
		getGameScene().addUINodes(nextButton, previousButton);
		componentePropulsor.onShipDestroyed();

		player.setViewFromTexture("Nave" + model.getSkin() + "Destroyed.png");

		// TODO animacion para morir
	}

	// Efectos del jugador
	private void initGameEffects() {
		Animations.hiperJumpTransition(player, getGameWorld());

		componentePropulsor = new ComponentePropulsor(player);
		componentePropulsor.emissionRateProperty().bind(model.thrustProperty());

	}

	// La interfaz del juego en si
	private void initGameUI() {
		player.setRenderLayer(RenderLayer.TOP);
		getGameScene().setBackgroundColor(Color.BLACK);

		// Hud
		hud.getModel().shieldProperty().bind(model.shieldProperty());
		hud.getModel().hpProperty().bind(model.hullProperty());
		hud.getModel().thrustProperty().bind(model.thrustProperty().divide(8.0));
		hud.getModel().setNombreJugador(model.getName());
		getGameScene().addUINode(hud);
		hud.setTranslateX(hud.getWidth());

		// Radar/minimapa
		getGameScene().addUINode(radar);
		radar.setTranslateX(viewWidth - radar.getPrefWidth());
		radar.getModel().playerXProperty()
				.bind((player.xProperty().divide(radar.prefWidthProperty()).add(radar.prefWidthProperty().divide(2))));
		radar.getModel().playerYProperty().bind(
				(player.yProperty().divide(radar.prefHeightProperty()).add(radar.prefHeightProperty().divide(2))));
		radar.getModel().playerRotationProperty().bind(player.angleProperty());
	}

	// Limite de velocidad en el eje x e y, por eso en diagonal no funciona bien
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

	/*
	 * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * Metodos de gameplay
	 * 
	 * * * * * * * * * * * * * * * * * * * *
	 */

	private void reloadShield() {
		if (model.getShield() < 1)
			if (System.currentTimeMillis() > model.getCooldownShield() + 3000) {
				model.setCooldownShield(System.currentTimeMillis());
				if (model.getShield() + 0.1 >= 1)
					model.setShield(1);
				else
					model.setShield(model.getShield() + 0.1);
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

			Animations.tinkleTransition(newStar);

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

		// Animacion
		Animations.hitTransition(player, getGameWorld());
		
		// Reiniciar la carga de escudo
		model.setCooldownShield(System.currentTimeMillis());
		
		// Distribuir el daño al escudo o casco
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

		// Si un jugador enemigo se ha muerto y sale de los bordes, se elimina del mundo
		for (NetworkingPlayer ntp : model.getJugadores()) {
			if (!ntp.isAlive()) {
				if (ntp.getEntity().getX() > model.getMARGIN_HORIZONTAL() + 100
						|| ntp.getEntity().getX() < -model.getMARGIN_HORIZONTAL() - 100
						|| ntp.getEntity().getY() > model.getMARGIN_VERTICAL() + 100
						|| ntp.getEntity().getY() < -model.getMARGIN_VERTICAL() - 100)
					getGameWorld().removeEntity(ntp.getEntity());
			}
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

	// Para cuando se cambie de jugador al que se está observando
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
				if (ntp.getEntity().distance(player) < 1000)
					getAudioPlayer().playSound("laser.mp3");
			}
		}
		// La animacion borra la entidad del mundo
	}

	private void playMediaTracks(ArrayList<Media> lista) {
		if (lista.size() == 0)
			return;
		if (cancionActual == 4)
			cancionActual = 0;
		MediaPlayer mediaPlayer = new MediaPlayer(lista.get(cancionActual++));
		mediaPlayer.play();
		
		mediaPlayer.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				playMediaTracks(lista);
			}
		});
		
	}
}
