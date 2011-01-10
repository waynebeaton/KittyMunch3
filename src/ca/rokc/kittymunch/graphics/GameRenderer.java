package ca.rokc.kittymunch.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.game.GameObject;
import ca.rokc.kittymunch.game.KittyMunch;

public class GameRenderer {
	
	private final KittyMunch game;
	
	public GameRenderer(KittyMunch game, final Context context) {
		this.game = game;
	}

	public void drawOn(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		
		int middle = width / 2;
		
		// Front of roof
		canvas.drawLine(middle - game.half_building_top, height-game.ledge, middle + game.half_building_top, height-game.ledge, paint);
		// Left side of roof
		canvas.drawLine(middle - game.half_building_top, height-game.ledge, middle - game.half_building_top, height, paint);
		// Right side of roof
		canvas.drawLine(middle + game.half_building_top, height-game.ledge, middle + game.half_building_top, height, paint);
		// Left side of facade
		canvas.drawLine(middle - game.half_building_top, height-game.ledge, middle - game.half_building_bottom, height-(game.ledge+game.facade), paint);
		// Right side of facade
		canvas.drawLine(middle + game.half_building_top, height-game.ledge, middle + game.half_building_bottom, height-(game.ledge+game.facade), paint);
		// Sidewalk
		canvas.drawLine(0, height-(game.ledge+game.facade), width, height-(game.ledge+game.facade), paint);
		canvas.drawLine(0, height-(game.ledge+game.facade+game.sidewalk), width, height-(game.ledge+game.facade+game.sidewalk), paint);
		canvas.drawLine(0, height-(game.ledge+game.facade+game.sidewalk+2), width, height-(game.ledge+game.facade+game.sidewalk+2), paint);
		
		for (GameObject object : game.background) object.drawOn(canvas);
		for (GameObject object : game.targets) object.drawOn(canvas);
		for (GameObject object : game.projectiles) object.drawOn(canvas);
		game.protagonist.drawOn(canvas);
	}
}
