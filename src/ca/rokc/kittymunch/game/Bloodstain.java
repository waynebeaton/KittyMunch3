package ca.rokc.kittymunch.game;

import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.geometry.Point;

public class Bloodstain extends GameObject {

	private static final int MAX_AGE = 30;
	private int countdown = MAX_AGE;

	public Bloodstain(Point location) {
		super(location);
	}

	@Override
	public void doTick() {
		if (countdown-- <= 0) game.remove(this);
	}

	@Override
	public void collideWith(GameObject object) {
	}
	
	public void drawOn(android.graphics.Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.RED);
		paint.setAlpha(countdown * 255 / MAX_AGE);
	
		int x = (int)location.x;
		int y = (int)location.y;
		
		canvas.drawCircle(x, y, 10, paint);
	}

}
