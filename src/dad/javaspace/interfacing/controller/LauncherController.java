package dad.javaspace.interfacing.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	
	private MediaPlayer mpButtons;

	public LauncherController() {
		loadView("/dad/javaspace/interfacing/MainMenuView.fxml");
		loadView("/dad/javaspace/interfacing/EmpezarPartidaHoverView.fxml");
		loadView("/dad/javaspace/interfacing/CFGView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (cfgHoverRoot != null) {

			imageViewEP.setImage(new Image("/dad/javaspace/resources/images/imagenjugar.jpg"));
			rootView.setCenter(empezarPartidaHoverRoot);

			/*
			 * Audio
			 */

			String musicFile = "src/dad/javaspace/resources/sound/Vigil.mp3";
			Media mainTheme = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mp = new MediaPlayer(mainTheme);
			mp.volumeProperty().bind(sonidoSlider.valueProperty().divide(100));
			mp.setCycleCount(100);
			mp.play();

			cfgButton.hoverProperty().addListener(e -> onCFGButtonHovered());
			empezarPartidaButton.hoverProperty().addListener(e -> onEmpezarPartidaButtonHovered());
			
			String buttonMusicFile = "src/dad/javaspace/resources/sound/ButtonSound1.mp3";
			Media buttonSound = new Media(new File(buttonMusicFile).toURI().toString());
			mpButtons = new MediaPlayer(buttonSound);
			mpButtons.setVolume(1);

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
