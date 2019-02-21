package dad.javaspace;

import com.almasb.fxgl.entity.Entity;

public class ClientUtils {

	public static Entity[] buildWalls(int maxWidth, int maxHeight) {
		Entity[] walls = new Entity[4];

		for (int i = 0; i < walls.length; i++) {
			walls[i] = new Entity();
			walls[i].setViewFromTexture("dangerZone.png");
		}

		walls[0].setScaleX(maxWidth / 100);
		walls[0].setY(maxHeight);

		walls[1].setScaleY(maxHeight / 100);
		walls[1].setX(maxWidth);

		walls[2].setScaleX(maxWidth / 100);
		walls[2].setY(-maxWidth);

		walls[3].setScaleY(maxHeight / 100);
		walls[3].setX(-maxWidth);

		return walls;
	}

}
