package dad.javaspace;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.settings.GameSettings;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Main extends GameApplication {
	//
	private Entity player;

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
	            player.translateX(5); // move right 5 pixels
	        }
	    }, KeyCode.D);
	    
	    input.addAction(new UserAction("Move Left") {
	        @Override
	        protected void onAction() {
	            player.translateX(-5); // move left 5 pixels
	        }
	    }, KeyCode.A);
	    
	    input.addAction(new UserAction("Move Up") {
	        @Override
	        protected void onAction() {
	            player.translateY(-5); // move up 5 pixels
	        }
	    }, KeyCode.W);

	    input.addAction(new UserAction("Move Down") {
	        @Override
	        protected void onAction() {
	            player.translateY(5); // move down 5 pixels
	        }
	    }, KeyCode.S);
	    
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
		player = Entities.builder().at(300, 300).viewFromNode(new Rectangle(25, 25, Color.BLUE))
				.buildAndAttach(getGameWorld());
	}

}
