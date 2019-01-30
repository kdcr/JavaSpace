package dad.javaspace;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.settings.GameSettings;

import dad.javaspace.interfacing.controller.LauncherController;
import javafx.application.Platform;
import javafx.stage.StageStyle;

public class JavaSpaceAPP extends GameApplication {


	@Override
	protected void initGame() {
		super.initGame();			
			getGameScene().addGameView(new EntityView(new LauncherController().getRootView()));
	}

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		settings.setStageStyle(StageStyle.UNDECORATED);
		settings.setWidth(720);
		settings.setHeight(360);
		
	}
}
