package dad.javaspace.alerts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EndGameScreen extends VBox implements Initializable {

	@FXML
	private VBox rootPane;

	@FXML
	private Label victoryLabel;

	@FXML
	private HBox posPane;

	@FXML
	private Label posLabel;

	@FXML
	private Button cerrarButton;
	
	@FXML
	private Button hideButton;

	private int pos,max;

	public EndGameScreen(int pos, int max) {
		try {
			this.pos = pos;
			this.max = max;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EndGameScreen.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		posLabel.setText(pos + "/" + (max + 1));

		if (pos == 1)
			victoryLabel.setText("¡Ganador!");
		else
			victoryLabel.setText("Lástima...");
		
		hideButton.setOnAction(e->{
			this.setVisible(false);
		});
	}
	
	public Button getCerrarButton() {
		return this.cerrarButton;
	}
	
	public Button getHideButton() {
		return this.hideButton;
	}

}
