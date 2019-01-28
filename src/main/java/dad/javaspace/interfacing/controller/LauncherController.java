package dad.javaspace.interfacing.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javaspace.ClientModel;
import dad.javaspace.JavaSpaceAPP;
import dad.javaspace.Main;
import dad.javaspace.interfacing.ScreenResolutions;
import dad.javaspace.launchermodel.LauncherModel;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

			model = new LauncherModel();

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
			sonidoMusicaSlider.setValue(100.0);
			mp.setCycleCount(100);
			mp.play();

			// Efecto Sonido Hover
			String buttonMusicFile = "/assets/sounds/ButtonSound1.mp3";
			Media buttonSound = new Media(getClass().getResource(buttonMusicFile).toString());
			sonidoFXSlider.setValue(100);
			mpButtons = new MediaPlayer(buttonSound);
			mpButtons.setVolume(1);

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
			exitButton.setOnAction(e -> JavaSpaceAPP.getPrimaryStage().close());
			launchButton.setOnAction(e -> model.guardarConfig());

			/****************************************************************************************************
			 * 
			 * Stage dragging behaviour
			 * 
			 ***************************************************************************************************/

			rootView.setOnMousePressed(e -> onMousePressed(e));
			rootView.setOnMouseDragged(e -> onMouseDrag(e));

			/****************************************************************************************************
			 * 
			 * ComboBox resoluciones
			 * 
			 ***************************************************************************************************/

			resolutionComboBox.getItems().setAll(ScreenResolutions.values());

			/***************************************************************************************************
			 * 
			 * Bindeos con el modelo
			 * 
			 ***************************************************************************************************/
			
			// Bindeo audio con modelo
			model.volumenMusicaProperty().bind(sonidoMusicaSlider.valueProperty().divide(100));
			mp.volumeProperty().bind(model.volumenMusicaProperty());
			
			model.volumenJuegoProperty().bind(sonidoFXSlider.valueProperty().divide(100));
			mpButtons.volumeProperty().bind(model.volumenJuegoProperty());
			
			// Datos del jugador
			Bindings.bindBidirectional(nombreField.textProperty(), model.nombreJugadorProperty());
			Bindings.bindBidirectional(ipTextField.textProperty(), model.ipProperty());
			Bindings.bindBidirectional(puertoTextField.textProperty(), model.puertoProperty(), new NumberStringConverter());
			Bindings.bindBidirectional(fullScreenCheckBox.selectedProperty(), model.pantallaCompletaProperty());
// TODO		Bindings.bindBidirectional(resolutionComboBox.getSelectionModel().selectedItemProperty(), model.resolucionProperty());
			//Provisionalmente bindeo de un solo lado
			model.resolucionProperty().bind(resolutionComboBox.getSelectionModel().selectedItemProperty());
			// No puedo bindear bidireccional porque el combo es ReadOnly
			

		}
	}

//	private void onLaunchAction() {
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

	private void onMouseDrag(MouseEvent e) {
		JavaSpaceAPP.getPrimaryStage().setX(e.getScreenX() + ejeX);
		JavaSpaceAPP.getPrimaryStage().setY(e.getScreenY() + ejeY);
	}

	private void onMousePressed(MouseEvent e) {
		ejeX = JavaSpaceAPP.getPrimaryStage().getX() - e.getScreenX();
		ejeY = JavaSpaceAPP.getPrimaryStage().getY() - e.getScreenY();
	}

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
