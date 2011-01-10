package ca.rokc.kittymunch.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.geometry.Point;

public class Score extends GameObject {
	public static final int MAX_AGE = 50;
	
	public String value;
	public int countdown = MAX_AGE;

	public Score(Point location, String value) {
		super(location);
		this.value = value;
	}

	@Override
	public void doTick() {
		if (--countdown <= 0) {
			game.remove(this);
		}
		location.y -= 1;
	}

	@Override
	public void collideWith(GameObject object) {
		// TODO Auto-generated method stub
	}
	
	public void drawOn(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setAlpha(countdown * 255 / MAX_AGE);
		
		canvas.drawText(value, (int)location.x, (int)location.y, paint);
	}
}
