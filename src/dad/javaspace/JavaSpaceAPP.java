package dad.javaspace;

import dad.javaspace.interfacing.controller.LauncherController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaSpaceAPP extends Application {

	private static Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		JavaSpaceAPP.primaryStage = primaryStage;
		LauncherController controller = new LauncherController();
		
		Scene scene = new Scene(controller.getRootView());
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Prueba JavaSpaceLauncher");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}
