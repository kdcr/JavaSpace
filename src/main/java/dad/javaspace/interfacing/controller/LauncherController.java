package dad.javaspace.interfacing.controller;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import dad.javaspace.ClientModel;
import dad.javaspace.JavaSpaceAPP;
import dad.javaspace.Main;
import dad.javaspace.interfacing.ScreenResolutions;
import dad.javaspace.launchermodel.LauncherModel;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

public class LauncherController implements Initializable {

	/****************************************************************************************************
	 * 
	 * Modelo
	 * 
	 ***************************************************************************************************/
	private LauncherModel model;
	private ListProperty<ScreenResolutions> listaResoluciones;

	/****************************************************************************************************
	 * 
	 * Main Menu Components
	 * 
	 ***************************************************************************************************/

	@FXML
	private BorderPane rootView;

	@FXML
	private VBox cajaBotones;

	@FXML
	private Button empezarPartidaButton;

	@FXML
	private Button cfgButton;

	@FXML
	private Button selectSkinButton;

	@FXML
	private Button aboutButton;

	@FXML
	private Button exitButton;

	@FXML
	private HBox cajaVersion;

	@FXML
	private Label versionLabel;

	/****************************************************************************************************
	 * 
	 * CFGView Components
	 * 
	 ***************************************************************************************************/

	@FXML
	private ScrollPane cfgHoverRoot;

	@FXML
	private GridPane cfgGridHoverRoot;

	@FXML
	private ComboBox<ScreenResolutions> resolutionComboBox;

	@FXML
	private CheckBox fullScreenCheckBox;

	@FXML
	private TextField nombreField;

	@FXML
	private Slider sonidoMusicaSlider;

	@FXML
	private Slider sonidoFXSlider;

	@FXML
	private TextField ipTextField;

	@FXML
	private TextField puertoTextField;

	/****************************************************************************************************
	 * 
	 * EmpezarPartidaHoverView
	 * 
	 ***************************************************************************************************/

	@FXML
	private StackPane empezarPartidaHoverRoot;

	@FXML
	private ImageView imageViewEP;

	@FXML
	private Button launchButton;

	/****************************************************************************************************
	 * 
	 * SalirPartidaView
	 * 
	 ***************************************************************************************************/

	@FXML
	private HBox salirLauncherHoverRoot;

	@FXML
	private ImageView imageViewSalir;

	/****************************************************************************************************
	 * 
	 * SkinSelectorView
	 * 
	 ***************************************************************************************************/

	@FXML
	private ScrollPane skinTilePaneRoot;

	@FXML
	private TilePane skinTilePane;

	@FXML
	private Button skinUno;

	@FXML
	private Button skinDos;

	@FXML
	private Button skinTres;

	@FXML
	private Button skinCuatro;

	@FXML
	private Button skinCinco;

	@FXML
	private Button skinSeis;

	@FXML
	private Button skinSiete;

	@FXML
	private Button skinOcho;

	/****************************************************************************************************
	 * 
	 * MediaPlayer
	 * 
	 ***************************************************************************************************/

	private MediaPlayer mpButtons;

	// window Position
	double ejeX;
	double ejeY;

	public LauncherController() {
		loadView("/fxml/MainMenuView.fxml");
		loadView("/fxml/EmpezarPartidaHoverView.fxml");
		loadView("/fxml/SalirLauncherView.fxml");
		loadView("/fxml/SkinSelectorView.fxml");
		loadView("/fxml/CFGView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (cfgHoverRoot != null) {

//			TODO Platform.setImplicitExit(false);

			/****************************************************************************************************
			 * 
			 * Modelo
			 * 
			 ***************************************************************************************************/

			model = cargarConfig();

			listaResoluciones = ScreenResolutions.getScreenResolutions();

			/****************************************************************************************************
			 * 
			 * CSS
			 * 
			 ***************************************************************************************************/

			rootView.getStylesheets().setAll("/css/launcher.css");

			/****************************************************************************************************
			 * 
			 * Imagenes
			 * 
			 ***************************************************************************************************/
			imageViewEP.setImage(new Image("/assets/textures/imagenjugar.jpg"));
			rootView.setCenter(empezarPartidaHoverRoot);
			imageViewSalir.setImage(new Image("/assets/textures/imagensalir.jpg"));

			// Skins
			skinUno.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinDos.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinTres.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinCuatro.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinCinco.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinSeis.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinSiete.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));
			skinOcho.setGraphic(new ImageView(new Image("/assets/textures/navePrueba.png")));

			/****************************************************************************************************
			 * 
			 * Audio
			 * 
			 ***************************************************************************************************/

			// Musica menu
			String musicFile = "/assets/sounds/Vigil.mp3";
			Media mainTheme = new Media(getClass().getResource(musicFile).toString());
			MediaPlayer mp = new MediaPlayer(mainTheme);
			sonidoMusicaSlider.setMax(1.0);
			mp.setCycleCount(100);
			mp.play();

			// Efecto Sonido Hover
			String buttonMusicFile = "/assets/sounds/ButtonSound1.mp3";
			Media buttonSound = new Media(getClass().getResource(buttonMusicFile).toString());
			sonidoFXSlider.setMax(1.0);
			mpButtons = new MediaPlayer(buttonSound);

			/****************************************************************************************************
			 * 
			 * BackGround Image
			 * 
			 ***************************************************************************************************/

			BackgroundSize bSize = new BackgroundSize(720, 360, false, false, true, true);

			Background background = new Background(
					new BackgroundImage(new Image("/assets/textures/launcherBackground.gif"),
							BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));

			rootView.setBackground(background);

			/****************************************************************************************************
			 * 
			 * Buttons
			 * 
			 ***************************************************************************************************/

			cfgButton.hoverProperty().addListener(e -> onCFGButtonHovered());
			exitButton.hoverProperty().addListener(e -> onExitButtonHovered());
			empezarPartidaButton.hoverProperty().addListener(e -> onEmpezarPartidaButtonHovered());
			selectSkinButton.hoverProperty().addListener(e -> onSelectSkinButtonHovered());
			exitButton.setOnAction(e -> onCloseAction());
			launchButton.setOnAction(e -> guardarConfig());

			/****************************************************************************************************
			 * 
			 * Stage dragging behaviour
			 * 
			 ***************************************************************************************************/

			/*
			 * rootView.setOnMousePressed(e -> onMousePressed(e));
			 * rootView.setOnMouseDragged(e -> onMouseDrag(e));
			 */

			/****************************************************************************************************
			 * 
			 * ComboBox resoluciones
			 * 
			 ***************************************************************************************************/

			resolutionComboBox.itemsProperty().bind(listaResoluciones);
			resolutionComboBox.getSelectionModel().select(model.getResolucion());

			/***************************************************************************************************
			 * 
			 * Bindeos con el modelo
			 * 
			 ***************************************************************************************************/

			// Bindeo audio con modelo
			Bindings.bindBidirectional(sonidoMusicaSlider.valueProperty(), model.volumenMusicaProperty());
			mp.volumeProperty().bind(model.volumenMusicaProperty());

			Bindings.bindBidirectional(sonidoFXSlider.valueProperty(), model.volumenJuegoProperty());
			mpButtons.volumeProperty().bind(model.volumenJuegoProperty());

			// Datos del jugador
			Bindings.bindBidirectional(nombreField.textProperty(), model.nombreJugadorProperty());
			Bindings.bindBidirectional(ipTextField.textProperty(), model.ipProperty());
			Bindings.bindBidirectional(puertoTextField.textProperty(), model.puertoProperty(),
					new NumberStringConverter());
			Bindings.bindBidirectional(fullScreenCheckBox.selectedProperty(), model.pantallaCompletaProperty());
			model.resolucionProperty().bind(resolutionComboBox.getSelectionModel().selectedItemProperty());

		}
	}

	private void onCloseAction() {
		guardarConfig();
		Platform.exit();
	}

//	private void onLaunchAction() {
//		model.guardarConfig();
//		dad.javaspace.Main main = new dad.javaspace.Main();
//		String[] table = {};
//		
//		Platform.exit();
//		JavaSpaceAPP.getPrimaryStage().hide();
//		Platform.runLater(() -> {
//		JavaSpaceAPP.getPrimaryStage().setScene(main.getGameScene());
//		Main.main(table);
//			
//		});
//	}

	/*
	 * private void onMouseDrag(MouseEvent e) {
	 * JavaSpaceAPP.getPrimaryStage().setX(e.getScreenX() + ejeX);
	 * JavaSpaceAPP.getPrimaryStage().setY(e.getScreenY() + ejeY); }
	 * 
	 * private void onMousePressed(MouseEvent e) { ejeX =
	 * JavaSpaceAPP.getPrimaryStage().getX() - e.getScreenX(); ejeY =
	 * JavaSpaceAPP.getPrimaryStage().getY() - e.getScreenY(); }
	 */

	private void onSelectSkinButtonHovered() {
		if (selectSkinButton.isHover()) {
			rootView.getCenter().setOpacity(0);
			hoverAnimation(skinTilePaneRoot);
		}
	}

	private void onExitButtonHovered() {
		if (exitButton.isHover()) {
			rootView.getCenter().setOpacity(0);
			hoverAnimation(salirLauncherHoverRoot);
		}
	}

	private void onCFGButtonHovered() {
		if (cfgButton.isHover()) {
			rootView.getCenter().setOpacity(0);
			hoverAnimation(cfgHoverRoot);
		}

	}

	private void onEmpezarPartidaButtonHovered() {
		if (empezarPartidaButton.isHover()) {
			rootView.getCenter().setOpacity(0);
			hoverAnimation(empezarPartidaHoverRoot);
		}

	}

	private void hoverAnimation(Node nodo) {
		mpButtons.stop();
		mpButtons.play();

		rootView.setCenter(nodo);
		FadeTransition fade = new FadeTransition();
		fade.setNode(nodo);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setDuration(new Duration(700));
		fade.play();
	}

	public void guardarConfig() {
		LauncherModel model = new LauncherModel();

		File file = new File(System.getProperty("user.home") + "/.javaspace/" + model.getNombreJugador() + ".ini");

		if (!file.exists())
			try {
				file.createNewFile();

				Wini ini = new Wini(file);

				ini.put("Opciones de Juego", "Resoluci�n", model.getResolucion());
				ini.put("Opciones de Juego", "Pantalla Completa", model.isPantallaCompleta());
				ini.put("Opciones de Juego", "Nombre", model.getNombreJugador());
				ini.put("Opciones de Juego", "Volumen M�sica", model.getVolumenMusica());
				ini.put("Opciones de Juego", "Volumen Juego", model.getVolumenJuego());
				ini.put("Opciones de RED", "Direccion IP", model.getIp());
				ini.put("Opciones de RED", "Puerto", model.getPuerto());
				ini.store();

			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public LauncherModel cargarConfig() {

		try {
			File file = new File(System.getProperty("user.home") + "/.javaspace");

			if (!file.exists())
				file.mkdir();

			file = new File(System.getProperty("user.home") + "/.javaspace/" + model.getNombreJugador() + ".ini");

			Wini ini = new Wini(file);

			model.setResolucion(new ScreenResolutions(ini.get("Opciones de Juego", "Resoluci�n", String.class)));
			model.setPantallaCompleta(ini.get("Opciones de Juego", "Pantalla Completa", boolean.class));
			// TODO seguir por aqui
			model.setResolucion(new ScreenResolutions(ini.get("Opciones de Juego", "Nombre", String.class)));
			model.setResolucion(new ScreenResolutions(ini.get("Opciones de Juego", "Volumen M�sica", String.class)));
			model.setResolucion(new ScreenResolutions(ini.get("Opciones de Juego", "Volumen Juego", String.class)));
			
			
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	private void loadView(String ruta) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getRootView() {
		return rootView;
	}

	public LauncherModel getModel() {
		return this.model;
	}
}
