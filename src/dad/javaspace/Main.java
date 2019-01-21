package dad.javaspace;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.settings.GameSettings;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class Main extends GameApplication {
	//

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(600);
		settings.setHeight(600);
		settings.setTitle("JavaSpace");
		settings.setVersion("0.0.1");
	}
	
	@Override
	protected void initInput() {
	    Input input = getInput();

	    input.addAction(new UserAction("Move Right") {
	        @Override
	        protected void onAction() {
	            // Enviar input al servidor
	        }
	    }, KeyCode.D);
	    
	    input.addAction(new UserAction("Move Left") {
	        @Override
	        protected void onAction() {
	            // Enviar input al servidor
	        }
	    }, KeyCode.A);
	    
	    input.addAction(new UserAction("Move Up") {
	        @Override
	        protected void onAction() {
	            // Enviar input al servidor
	        }
	    }, KeyCode.W);

	    input.addAction(new UserAction("Move Down") {
	        @Override
	        protected void onAction() {
	            // Enviar input al servidor
	        }
	    }, KeyCode.S);
	    
	    input.addAction(new UserAction("Shoot") {
	        @Override
	        protected void onAction() {
	            // Enviar input al servidor
	        }
	    }, KeyCode.SPACE);
	    
	}
	
	@Override
	protected void initUI() {
	    Text textPixels = new Text();
	    textPixels.setTranslateX(50); // x = 50
	    textPixels.setTranslateY(100); // y = 100

	    getGameScene().addUINode(textPixels); // add to the scene graph
	}
	
	@Override
	protected void initGameVars(Map<String, Object> vars) {
	    vars.put("pixelsMoved", 0);
	}

	@Override
	protected void initGame() {
		super.initGame();
//		player = Entities.builder().at(300, 300).viewFromNode(new ImageView("/dad/javaspace/resources/images/player.png"))
//				.buildAndAttach(getGameWorld());
	}

}
