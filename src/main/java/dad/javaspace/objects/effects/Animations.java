package dad.javaspace.objects.effects;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.animation.ScaleAnimationBuilder;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Animations {
	
	public static void tinkleTransition(Entity player, int duration, double tamMin, double tamMax) {
		Point2D minSize = new Point2D(tamMin, tamMin);
		Point2D maxSize = new Point2D(tamMax, tamMax);

		ScaleAnimationBuilder maxToMin = Entities.animationBuilder().duration(Duration.seconds(0.3)).repeat(duration)
				.scale(player).from(maxSize).to(minSize);

		maxToMin.buildAndPlay();

	}

}
