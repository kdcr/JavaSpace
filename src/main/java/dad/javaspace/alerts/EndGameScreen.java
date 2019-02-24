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

	private int pos;

	public EndGameScreen(int pos) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EndGameScreen.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();

			this.pos = pos;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		posLabel.setText(pos + "");

		if (pos == 1)
			victoryLabel.setText("¡Ganador!");
		else
			victoryLabel.setText("Lástima...");
		
		cerrarButton.setOnAction(e->{
			this.setVisible(false);
		});
	}

}
