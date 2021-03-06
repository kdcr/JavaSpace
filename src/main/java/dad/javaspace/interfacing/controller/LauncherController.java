package dad.javaspace.interfacing.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import dad.javaspace.interfacing.ScreenResolutions;
import dad.javaspace.launchermodel.LauncherModel;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import javafx.scene.transform.Rotate;
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
	private AnchorPane rootView;

	@FXML
	private BorderPane rootBorderPaneView;

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
	private Label avisoLabel;

	@FXML
	private Slider sonidoMusicaSlider;

	@FXML
	private Slider sonidoFXSlider;

	@FXML
	private TextField ipTextField;

	@FXML
	private TextField playersNumText;

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
	private ImageView loadingImage;

	@FXML
	private Button launchButton;

	@FXML
	private Button createRoomButton;

	@FXML
	private Label labelInfo;

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
	 * About us
	 * 
	 ****************************************************************************************************/

	@FXML
	private GridPane aboutUsRoot;

	@FXML
	private Hyperlink linkSam;

	@FXML
	private Hyperlink linkKevin;

	@FXML
	private Hyperlink linkMichael;

	@FXML
	private Hyperlink linkPedro;

	/****************************************************************************************************
	 * 
	 * MediaPlayer
	 * 
	 ***************************************************************************************************/

	private MediaPlayer mpButtons;
	private MediaPlayer mp;
	private MediaPlayer launchButtonMP;

	// window Position
	double ejeX;
	double ejeY;

	// Contador nombre mal escrito (_)
	int nameCount;

	// Array de botones para poder seleccionar el que necesitamos y ponerlo como
	// focused al principio
	private ArrayList<Button> botonesSkins;

	public LauncherController() {
		loadView("/fxml/MainMenuView.fxml");
		loadView("/fxml/EmpezarPartidaHoverView.fxml");
		loadView("/fxml/SalirLauncherView.fxml");
		loadView("/fxml/SkinSelectorView.fxml");
		loadView("/fxml/AboutUsView.fxml");
		loadView("/fxml/CFGView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (cfgHoverRoot != null) {

			/****************************************************************************************************
			 * 
			 * Modelo
			 * 
			 ***************************************************************************************************/

			model = cargarConfig();

			listaResoluciones = ScreenResolutions.getScreenResolutions();
			nameCount = 0;

			/****************************************************************************************************
			 * 
			 * CSS
			 * 
			 ***************************************************************************************************/
			botonesSkins = new ArrayList<>();
			botonesSkins.add(skinUno);
			botonesSkins.add(skinDos);
			botonesSkins.add(skinTres);
			botonesSkins.add(skinCuatro);
			botonesSkins.add(skinCinco);
			botonesSkins.add(skinSeis);
			botonesSkins.add(skinSiete);
			botonesSkins.add(skinOcho);

			onSelectedSkinChanged();

			rootView.getStylesheets().setAll("/css/launcher.css");
			model.selectedSkinProperty().addListener(e -> onSelectedSkinChanged());

			/****************************************************************************************************
			 * 
			 * Imagenes
			 * 
			 ***************************************************************************************************/
			rootBorderPaneView.setCenter(empezarPartidaHoverRoot);
			imageViewSalir.setImage(new Image("/assets/textures/imagensalir.png"));
			imageViewSalir.setFitWidth(0);
			imageViewSalir.setFitHeight(0);
			loadingImage.setImage(new Image("/assets/textures/loadercircle.png"));
			loadingImage.setVisible(false);
			labelInfo.setVisible(false);

			// Skins
			skinUno.setGraphic(new ImageView(new Image("/assets/textures/Nave0.png")));
			skinDos.setGraphic(new ImageView(new Image("/assets/textures/Nave1.png")));
			skinTres.setGraphic(new ImageView(new Image("/assets/textures/Nave2.png")));
			skinCuatro.setGraphic(new ImageView(new Image("/assets/textures/Nave3.png")));
			skinCinco.setGraphic(new ImageView(new Image("/assets/textures/Nave4.png")));
			skinSeis.setGraphic(new ImageView(new Image("/assets/textures/NaveLocked.png")));
			skinSiete.setGraphic(new ImageView(new Image("/assets/textures/xwing.png")));
			skinOcho.setGraphic(new ImageView(new Image("/assets/textures/placeholder.png")));

			// Desactivar botones de dlc
			skinSeis.setDisable(true);
			skinSiete.setDisable(true);
			skinOcho.setDisable(true);

			/****************************************************************************************************
			 * 
			 * Audio
			 * 
			 ***************************************************************************************************/

			// Musica menu
			String musicFile = "/music/Vigil.mp3";
			Media mainTheme = new Media(getClass().getResource(musicFile).toString());
			mp = new MediaPlayer(mainTheme);
			sonidoMusicaSlider.setMax(1.0);
			mp.setCycleCount(MediaPlayer.INDEFINITE);
			mp.play();

			// Efecto Sonido Hover
			String buttonMusicFile = "/assets/sounds/ButtonSound1.mp3";
			Media buttonSound = new Media(getClass().getResource(buttonMusicFile).toString());
			sonidoFXSlider.setMax(1.0);
			mpButtons = new MediaPlayer(buttonSound);

			// Efecto Sonido Click empezar juego
			String buttonStartGame = "/assets/sounds/ButtonSound2.mp3";
			Media buttonSound2 = new Media(getClass().getResource(buttonStartGame).toString());
			launchButtonMP = new MediaPlayer(buttonSound2);

			/****************************************************************************************************
			 * 
			 * BackGround Image
			 * 
			 ***************************************************************************************************/

			BackgroundSize bSize = new BackgroundSize(480, 270, false, false, false, false);

			Background background = new Background(
					new BackgroundImage(new Image("/assets/textures/launcherBackground.gif"), BackgroundRepeat.REPEAT,
							BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, bSize));

			rootView.setBackground(background);

			/****************************************************************************************************
			 * 
			 * Buttons
			 * 
			 ***************************************************************************************************/

			// Hovered properties
			createRoomButton.setOnMousePressed(e -> onCreateRoomPressed());
			aboutButton.hoverProperty().addListener(e -> onAboutUsButtonHovered());
			cfgButton.hoverProperty().addListener(e -> onCFGButtonHovered());
			exitButton.hoverProperty().addListener(e -> onExitButtonHovered());
			selectSkinButton.hoverProperty().addListener(e -> onSelectSkinButtonHovered());
			empezarPartidaButton.hoverProperty().addListener(e -> onEmpezarPartidaButtonHovered());

			// Tooltips
			createRoomButton.setTooltip(new Tooltip("Crear sala usando los datos de la configuración de RED"));
			launchButton.setTooltip(new Tooltip("Empezar partida"));
			skinUno.setTooltip(new Tooltip("Interestelar 1"));
			skinDos.setTooltip(new Tooltip("Supernova"));
			skinTres.setTooltip(new Tooltip("Black Hole"));
			skinCuatro.setTooltip(new Tooltip("Abomination"));
			skinCinco.setTooltip(new Tooltip("Galactic Destroyer"));

			// Actions
			exitButton.setOnAction(e -> onCloseAction());
			launchButton.setOnMousePressed(e -> onLaunchButtonPressed());
			launchButton.setOnAction(e -> guardarConfig());

			// Skins
			skinUno.setOnAction(e -> onSkinUnoAction());
			skinDos.setOnAction(e -> onSkinDosAction());
			skinTres.setOnAction(e -> onSkinTresAction());
			skinCuatro.setOnAction(e -> onSkinCuatroAction());
			skinCinco.setOnAction(e -> onSkinCincoAction());
			skinSeis.setOnAction(e -> onSkinSeisAction());
			skinSiete.setOnAction(e -> onSkinSieteAction());
			skinOcho.setOnAction(e -> onSkinOchoAction());

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
			avisoLabel.setVisible(false);
			Bindings.bindBidirectional(playersNumText.textProperty(), model.numPlayersProperty(),
					new NumberStringConverter());

			// Tamano Launcher
			rootView.setPrefSize(model.getResolucion().getX(), model.getResolucion().getY());

			/**
			 * Listener para no permitir al jugador usar el carácter _ en el nombre
			 */
			model.nombreJugadorProperty().addListener(e -> {
				String text = model.getNombreJugador();
				if (text.contains("_")) {
					nameCount++;
					nombreField.setText(text.replace("_", ""));
					model.setNombreJugador(text.replace("_", ""));
				}

				if (nameCount == 3) {
					avisoLabel.setVisible(true);
				}
			});
		}
	}

	public void onSelectedSkinChanged() {
		for (int i = 0; i < botonesSkins.size(); i++) {
			if (i != model.getSelectedSkin()) {
				botonesSkins.get(i).getStylesheets().clear();
				botonesSkins.get(i).getStylesheets().add("/css/unselectedSkinButton.css");
			} else {
				botonesSkins.get(i).getStylesheets().clear();
				botonesSkins.get(i).getStylesheets().add("/css/selectedSkinButton.css");
			}
		}
	}

	public ImageView getLoadingImage() {
		return loadingImage;
	}

	private void onLaunchButtonPressed() {
		launchButtonMP.stop();
		launchButtonMP.play();
	}

	private void onCreateRoomPressed() {
		launchButtonMP.stop();
		launchButtonMP.play();
	}

	private void onCloseAction() {
		guardarConfig();
		System.exit(0);
		// Platform.exit();
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
			rootBorderPaneView.getCenter().setOpacity(0);
			hoverAnimation(skinTilePaneRoot);
		}
	}

	private void onExitButtonHovered() {
		if (exitButton.isHover()) {
			rootBorderPaneView.getCenter().setOpacity(0);
			hoverAnimation(salirLauncherHoverRoot);
		}
	}

	private void onCFGButtonHovered() {
		if (cfgButton.isHover()) {
			rootBorderPaneView.getCenter().setOpacity(0);
			hoverAnimation(cfgHoverRoot);
		}
	}

	private void onAboutUsButtonHovered() {
		if (aboutButton.isHover()) {
			rootBorderPaneView.getCenter().setOpacity(0);
			hoverAnimation(aboutUsRoot);
		}
	}

	private void onEmpezarPartidaButtonHovered() {
		if (empezarPartidaButton.isHover()) {
			rootBorderPaneView.getCenter().setOpacity(0);
			hoverAnimation(empezarPartidaHoverRoot);
		}

	}

	private void hoverAnimation(Node nodo) {
		mpButtons.stop();
		mpButtons.play();

		rootBorderPaneView.setCenter(nodo);
		FadeTransition fade = new FadeTransition();
		fade.setNode(nodo);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setDuration(new Duration(700));
		fade.play();
	}

	public void loadingAnimation() {
		loadingImage.setVisible(true);
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(loadingImage);
		rotate.setByAngle(360);
		rotate.setDelay(new Duration(0));
		rotate.setAxis(Rotate.Z_AXIS);
		rotate.setDuration(new Duration(1000));
		rotate.setCycleCount(Transition.INDEFINITE);
		rotate.play();
	}

	public void guardarConfig() {
		File file = new File(System.getProperty("user.home") + "/.javaspace/" + "config.ini");

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try {

			Wini ini = new Wini(file);

			ini.put("Opciones de Juego", "resolucion", model.getResolucion());
			ini.put("Opciones de Juego", "pantallaCompleta", model.isPantallaCompleta());
			ini.put("Opciones de Juego", "nombre", model.getNombreJugador());
			ini.put("Opciones de Juego", "volumenMusica", model.getVolumenMusica());
			ini.put("Opciones de Juego", "volumenJuego", model.getVolumenJuego());
			ini.put("Opciones de RED", "numPlayers", model.getNumPlayers());
			ini.put("Opciones de RED", "ip", model.getIp());
			ini.put("Opciones de RED", "puerto", model.getPuerto());
			ini.put("Skins", "Skin Seleccionada", model.getSelectedSkin());
			ini.store();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LauncherModel cargarConfig() {

		LauncherModel modelCarga = new LauncherModel();
		File directorio = new File(System.getProperty("user.home") + "/.javaspace");
		File archivo = new File(System.getProperty("user.home") + "/.javaspace" + "/config.ini");

		if (!directorio.exists())
			directorio.mkdir();

		if (!archivo.exists())
			try {
				archivo.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		else {
			try {
				Wini ini = new Wini(archivo);

				modelCarga
						.setResolucion(new ScreenResolutions(ini.get("Opciones de Juego", "resolucion", String.class)));
				boolean prueba = ini.get("Opciones de Juego", "pantallaCompleta", boolean.class);
				System.out.println(prueba);
				modelCarga.setPantallaCompleta(ini.get("Opciones de Juego", "pantallaCompleta", boolean.class));
				modelCarga.setNombreJugador(ini.get("Opciones de Juego", "nombre", String.class));
				modelCarga.setVolumenMusica(ini.get("Opciones de Juego", "volumenMusica", double.class));
				modelCarga.setVolumenJuego(ini.get("Opciones de Juego", "volumenJuego", double.class));
				modelCarga.setNumPlayers(ini.get("Opciones de RED", "numPlayers", int.class));
				modelCarga.setIp(ini.get("Opciones de RED", "ip", String.class));
				modelCarga.setPuerto(ini.get("Opciones de RED", "puerto", int.class));
				modelCarga.setSelectedSkin(ini.get("Skins", "Skin Seleccionada", int.class));

			} catch (InvalidFileFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return modelCarga;

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

	private void onSkinUnoAction() {
		model.setSelectedSkin(0);
	}

	private void onSkinDosAction() {
		model.setSelectedSkin(1);
	}

	private void onSkinTresAction() {
		model.setSelectedSkin(2);
	}

	private void onSkinCuatroAction() {
		model.setSelectedSkin(3);
	}

	private void onSkinCincoAction() {
		model.setSelectedSkin(4);
	}

	private void onSkinSeisAction() {
		model.setSelectedSkin(5);
	}

	private void onSkinSieteAction() {
		model.setSelectedSkin(6);
	}

	private void onSkinOchoAction() {
		model.setSelectedSkin(7);
	}

	public MediaPlayer getMp() {
		return mp;
	}

	public AnchorPane getRootView() {
		return rootView;
	}

	public Button getLaunchButton() {
		return launchButton;
	}

	public Button getCreateRoomButton() {
		return createRoomButton;
	}

	public LauncherModel getModel() {
		return this.model;
	}

	public Label getLabelInfo() {
		return labelInfo;
	}

	public Hyperlink getLinkSam() {
		return linkSam;
	}

	public Hyperlink getLinkKevin() {
		return linkKevin;
	}

	public Hyperlink getLinkMichael() {
		return linkMichael;
	}

	public Hyperlink getLinkPedro() {
		return linkPedro;
	}

}
