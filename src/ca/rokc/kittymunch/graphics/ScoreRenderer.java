package ca.rokc.kittymunch.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.game.Score;

public class ScoreRenderer {

	private final Score score;

	public ScoreRenderer(Score score) {
		this.score = score;
	}
	
	public void drawOn(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setAlpha(score.countdown * 255 / Score.MAX_AGE);
		
		canvas.drawText(score.value, (int)score.location.x, (int)score.location.y, paint);
	}
}
