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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ca.rokc.kittymunch.geometry.Point;

public class Brick extends Projectile {
	public Brick(Point location, Point velocity, int height) {
		super(location, velocity, height);
	}
	
	@Override
	public void drawOn(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		canvas.drawCircle((int)location.x, (int)location.y, height, paint);
	}
}
