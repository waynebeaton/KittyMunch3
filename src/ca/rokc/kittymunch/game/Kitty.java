/*******************************************************************************
 * Copyright (c) 2010 Wayne Beaton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Wayne Beaton
 *******************************************************************************/
package ca.rokc.kittymunch.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import ca.rokc.kittymunch.geometry.Point;

public class Kitty extends GameObject {

	public int speed;
	public int direction;
	private final Bitmap[] images;
	private int count = 0;
	private int turn = 0;

	public Kitty(Bitmap[] images, Point location, int direction, int speed) {
		super(location);
		this.images = images;
		this.direction = direction;
		this.speed = speed;
		
	}
	
	@Override
	public void doTick() {
		if (turn > 0) {
			turn -= 10;
			return;
		}
		location.x += direction * speed;
		
		if (!game.isVisible(this)) game.remove(this);
	}

	@Override
	public void collideWith(GameObject object) {
		location.x -= direction * speed;
		turnAround();
		object.hitByKitty(this);
	}

	private void turnAround() {
		direction = -direction;
		turn  = 180;
		speed = game.generateKittySpeed();
	}

	/**
	 * When a kitty collides with the receiver, put it back to
	 * where it was before the collision and change the direction
	 * on both the kitty and the receiver.
	 * 
	 * TODO We need to be more clever. Fast kitty runs into slower kitty?
	 */
	@Override
	public void hitByKitty(Kitty object) {
	}
	
	@Override
	public void hitByBrick(Projectile projectile) {
		game.remove(this);
		game.addBackground(new Bloodstain(new Point(location)));
		game.addBackground(new Score(new Point(location), String.valueOf(speed * 100)));	}
	
	@Override
	public void drawOn(Canvas canvas) {
		Bitmap bitmap = images[count++ % images.length];
		
		canvas.save();
		Matrix matrix = canvas.getMatrix();
		matrix.postTranslate( - bitmap.getWidth() / 2, - bitmap.getHeight() / 2);
		if (direction == 1) {
			matrix.postRotate(180);
		}
		if (turn > 0) {
			matrix.postRotate(turn);
		}
		matrix.postTranslate((int)location.x, (int)location.y);
		canvas.setMatrix(matrix);
		//canvas.translate(direction * bitmap.getWidth() / 2, direction * bitmap.getHeight() / 2);
		canvas.drawBitmap(bitmap, 0, 0, new Paint());
		canvas.restore();
		
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setColor(Color.BLUE);
//	
//		int x = (int)location.x;
//		int y = (int)location.y;
//		
//		canvas.drawCircle(x, y, 10, paint);
//		canvas.drawText(String.valueOf(speed), x, y, paint);
	}
}
