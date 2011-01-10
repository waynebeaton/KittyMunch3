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
import ca.rokc.kittymunch.geometry.Point;

public abstract class GameObject {
	protected KittyMunch game;
	public final Point location;

	public GameObject(Point location) {
		this.location = location;
	}

	public void setGame(KittyMunch game) {
		this.game = game;
	}
	
	public void tick() {
		doTick();
		resolveCollisions();
	}

	public abstract void doTick();

	public void moveTo(Point target) {
		location.x = target.x;
		location.y = target.y;
	}
	
	public void moveBy(Point delta) {
		location.x += delta.x;
		location.y += delta.y;
	}
	
	private void resolveCollisions() {
		for(GameObject object : game.targets) {
			if (object == this) continue;
			int distance = (int)location.distanceTo(object.location);
			// TODO Magic number
			if (distance > 20) continue; // No collision
			collideWith(object);
		}
	}
	
	public abstract void collideWith(GameObject object);
	
	public void hitByKitty(Kitty object) {}

	public void hitByBrick(Projectile projectile) {}

	public void drawOn(Canvas canvas) {}
}
