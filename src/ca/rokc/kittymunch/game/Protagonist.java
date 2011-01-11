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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import ca.rokc.kittymunch.R;
import ca.rokc.kittymunch.geometry.Point;
import ca.rokc.kittymunch.geometry.Rectangle;

public class Protagonist extends GameObject {

	public Rectangle movementBounds;
	public Rectangle dropBounds;
	
	private Point targetLocation;

	private final Bitmap bitmap;
	
	public Protagonist(Context context, Rectangle movementBounds, Rectangle dropBounds, Point location) {
		super(location);
		this.movementBounds = movementBounds;
		this.dropBounds = dropBounds;
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand);
		
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
		location.moveInside(movementBounds);
	}
	
	@Override
	public void moveTo(Point target) {
		super.moveTo(target);
		location.moveInside(movementBounds);
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
		
		// TODO This may be causing threading issues.
		if (dropBounds.contains(location)) {
			// We have to copy the location, since the receiver;s
			// location is a mutable object that might change.
			game.addBrick(new Point(location));
			
		}
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
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.FILL_AND_STROKE);
//		paint.setColor(Color.RED);
//		canvas.drawCircle(protagonist.location.x, protagonist.location.y, 15, paint);
		
		canvas.drawBitmap(bitmap, (int)location.x - 35, (int)location.y - 45, new Paint());

//		drawRectangle(canvas, movementBounds, Color.GREEN);
//		drawRectangle(canvas, dropBounds, Color.RED);
	}
}
