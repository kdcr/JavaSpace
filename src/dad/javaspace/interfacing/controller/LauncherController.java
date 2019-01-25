package dad.javaspace.interfacing.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javaspace.JavaSpaceAPP;
import javafx.animation.FadeTransition;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class LauncherController implements Initializable {

	/**
	 * Main Menu Components
	 */

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

	/**
	 * CFGView Components
	 */

	@FXML
	private GridPane cfgHoverRoot;

	@FXML
	private ComboBox<String> resolutionComboBox;

	@FXML
	private CheckBox fullScreenCheckBox;

	@FXML
	private TextField nombreField;

	@FXML
	private Slider sonidoSlider;

	/**
	 * EmpezarPartidaHoverView
	 */

	@FXML
	private HBox empezarPartidaHoverRoot;

	@FXML
	private ImageView imageViewEP;

	/**
	 * SalirPartidaView
	 */

	@FXML
	private HBox salirLauncherHoverRoot;

	@FXML
	private ImageView imageViewSalir;

	/**
	 * 
	 * SkinSelectorView
	 */

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


	/**
	 * MediaPlayer
	 */

	private MediaPlayer mpButtons;

	public LauncherController() {
		loadView("/dad/javaspace/interfacing/MainMenuView.fxml");
		loadView("/dad/javaspace/interfacing/EmpezarPartidaHoverView.fxml");
		loadView("/dad/javaspace/interfacing/SalirLauncherView.fxml");
		loadView("/dad/javaspace/interfacing/SkinSelectorView.fxml");
		loadView("/dad/javaspace/interfacing/CFGView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (cfgHoverRoot != null) {

			/**
			 * Imagenes
			 */
			imageViewEP.setImage(new Image("/main/resources/assets/textures/imagenjugar.jpg"));
			rootView.setCenter(empezarPartidaHoverRoot);
			imageViewSalir.setImage(new Image("/main/resources/assets/textures/imagensalir.jpg"));

			// Skins
			skinUno.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinDos.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinTres.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinCuatro.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinCinco.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinSeis.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinSiete.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			skinOcho.setGraphic(new ImageView(new Image("/main/resources/assets/textures/navePrueba.png")));
			
			/*
			 * Audio
			 */

			// Musica menu
			String musicFile = "src/main/resources/assets/sounds/Vigil.mp3";
			Media mainTheme = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mp = new MediaPlayer(mainTheme);
			mp.volumeProperty().bind(sonidoSlider.valueProperty().divide(100));
			mp.setCycleCount(100);
			mp.play();

			// Efecto Sonido Hover
			String buttonMusicFile = "src/main/resources/assets/sounds/ButtonSound1.mp3";
			Media buttonSound = new Media(new File(buttonMusicFile).toURI().toString());
			mpButtons = new MediaPlayer(buttonSound);
			mpButtons.setVolume(1);

			/**
			 * Buttons
			 * 
			 */

			cfgButton.hoverProperty().addListener(e -> onCFGButtonHovered());
			exitButton.hoverProperty().addListener(e -> onExitButtonHovered());
			empezarPartidaButton.hoverProperty().addListener(e -> onEmpezarPartidaButtonHovered());
			selectSkinButton.hoverProperty().addListener(e -> onSelectSkinButtonHovered());

			exitButton.setOnAction(e -> JavaSpaceAPP.getPrimaryStage().close());

		}
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
}
