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

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.geometry.Point;

public class Brick extends Projectile {
	float rotation = 0;
	float spin = (float) (new Random().nextFloat() - 0.5);
	
	public Brick(Point location, Point velocity, int height) {
		super(location, velocity, height);
	}
	
	@Override
	public void drawOn(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.DKGRAY);
		int left = (int)location.x - height;
		int right = (int)location.x + height;
		int top = (int)location.y - (height / 2);
		int bottom = (int)location.y + (height / 2);
		
		
		canvas.save();
		canvas.rotate(rotation, (float)location.x, (float)location.y);
		canvas.drawRect(left, top, right, bottom, paint);
		canvas.restore();
		
		rotation += spin;
	}
}
