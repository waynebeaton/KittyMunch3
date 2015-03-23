/*******************************************************************************
 * Copyright (c) 2010, 2015 Wayne Beaton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Wayne Beaton
 *******************************************************************************/
package ca.rokc.kittymunch.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import ca.rokc.kittymunch.R;
import ca.rokc.kittymunch.geometry.Point;

public class Protagonist extends GameObject {


	private Point targetLocation;

	private final Bitmap bitmap;

	private Point home, shoulder, elbow;

	private int size;
	
	public Protagonist(Context context, Point location) {
		super(new Point(location.x, location.y));
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand);
		size = bitmap.getHeight();
		
		this.location.y -= size;
		home = location.copy();
		shoulder = location.copy();
		elbow = new Point(0,0);
		
		computeArmPosition();		
	}
	
	/**
	 * Set the location that the receiver is to move toward. Note that
	 * it is very likely that this method will be called from a different
	 * thread than the game itself is running in. Care needs to be taken
	 * to ensure that we don't run into any threading issues. The
	 * {@link #tick()} method, for example, assigns this value into a
	 * local variable before doing anything with it.
	 * 
	 * Note also that it is assumed that this object is not changed, but
	 * rather is replaced when new values are available.
	 * 
	 * A <code>null</code> value indicates that movement is to stop.
	 * 
	 * @param point May be <code>null</code>.
	 */
	public void moveToward(Point point) {
		targetLocation = point;
	}
	
	@Override
	public void moveBy(Point delta) {
		super.moveBy(delta);
		computeArmPosition();
	}
	
	@Override
	public void moveTo(Point target) {
		super.moveTo(target);
		computeArmPosition();
	}
		
	@Override
	public void tick() {
		// Use a local variable to avoid the need for synchronization.
		// The targetLocation may be switched (or nulled-out) asynchronously; 
		// the state of the instance should never change.
		Point targetLocation = this.targetLocation;
		if (targetLocation == null) return;
		if (location.isProximateTo(targetLocation, game.protagonist_speed)) {
			moveTo(targetLocation);
			targetLocation = null;
		} else {
			int distanceToTarget = (int)Math.min(game.protagonist_speed, location.distanceTo(targetLocation));
			moveBy(location.unitVectorTo(targetLocation, distanceToTarget));
		}
	}
	
	/**
	 * Release whatever we're holding, if anything.
	 */
	public void release() {
		targetLocation = null;
		
		game.addBrick(new Point(location));
	}

	@Override
	public void doTick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collideWith(GameObject object) {
		// Don't care about collisions by the protagonist.
	}
	
	@Override
	public void drawOn(Canvas canvas) {
		Paint paint = new Paint();
		
		drawForearmOn(canvas, paint);
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.RED);
		canvas.drawLine(shoulder.x, shoulder.y, elbow.x, elbow.y, paint);
		canvas.drawLine(elbow.x, elbow.y, location.x, location.y, paint);
	}

	private void drawForearmOn(Canvas canvas, Paint paint) {
		canvas.save();
		Matrix matrix = canvas.getMatrix();
		matrix.postTranslate( - bitmap.getWidth() / 2, - bitmap.getHeight());
		matrix.postRotate(elbow.angleTo(location));

		matrix.postTranslate((int)elbow.x, (int)elbow.y);
		canvas.setMatrix(matrix);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		canvas.restore();
	}
	
	private void computeArmPosition() {
        if (location.y > (home.y-size)) shoulder.y = location.y + size;
        else shoulder.y = home.y;

        double h = Math.sqrt(Math.pow(location.x-home.x, 2) + Math.pow(home.y-location.y, 2));
        double max = size * 32 / 17;
        if (home.y - location.y > max) location.y = (float) (home.y - max);

        if (h > max)
                shoulder.x = (float) (location.x + (location.x < home.x ? 1 : -1) * Math.sqrt(Math.pow(max,2)-Math.pow(location.y-shoulder.y,2)));
        else shoulder.x = home.x;

        double a2 = Math.pow(location.x-shoulder.x, 2) + Math.pow(shoulder.y-location.y, 2);
        double a = Math.min(Math.sqrt(a2),2.0*size);

        double A = Math.acos(a / (2 * size));
        double cosB = location.x == shoulder.x ? 0 : (a2+Math.pow(location.x-shoulder.x,2)-Math.pow(location.y-shoulder.y,2))/(2*a*(location.x-shoulder.x));
        double B = Math.acos(cosB);
        double C = B - A;

        elbow.x = (float) (shoulder.x + (size * Math.cos(C)));
        elbow.y = (float) (shoulder.y - (size * Math.sin(C)));
	}

}
