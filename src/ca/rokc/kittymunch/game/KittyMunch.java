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

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ca.rokc.kittymunch.R;
import ca.rokc.kittymunch.geometry.Point;
import ca.rokc.kittymunch.geometry.Rectangle;
import ca.rokc.kittymunch.util.DelayedChangeList;

/**
 * This implementation purposefully avoids dealing with any 
 * concurrency issues. We assume that the game itself is 
 * running in a single thread.
 */
public class KittyMunch {

	private Context context;
	
	public DelayedChangeList<GameObject> background = new DelayedChangeList<GameObject>();
	public DelayedChangeList<GameObject> targets = new DelayedChangeList<GameObject>();
	public DelayedChangeList<GameObject> projectiles = new DelayedChangeList<GameObject>();
	public Protagonist protagonist;
	
	private final Rectangle bounds;
	
	public final int ledge = 50;
	public final int half_building_top = 300;
	public final int half_building_bottom = 200;
	public final int facade = 200;
	public final int sidewalk = 50;
	
	public final int drop_speed = 5;
	public final int protagonist_speed = 50;
	
	private int targetCreateCountdown = 0;
	private final Bitmap[] kittyImages1;
	
	public KittyMunch(Context context, Rectangle bounds) {
		this.context = context;
		this.bounds = bounds;
		kittyImages1 = loadKittyImages();
		initialize();
	}

	private Bitmap[] loadKittyImages() {
		Bitmap kitty1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitty1);
		Bitmap kitty2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitty2);
		Bitmap kitty3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitty3);
		Bitmap kitty4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.kitty4);
		
		return new Bitmap[]{kitty1, kitty2, kitty3, kitty4, kitty3, kitty2};
	}

	public void addBackground(GameObject object) {
		object.setGame(this);
		background.add(object);
	}
	
	public void addTarget(GameObject target) {
		target.setGame(this);
		targets.add(target);
	}

	public void addProjectile(Projectile projectile) {
		projectile.setGame(this);
		projectiles.add(projectile);
	}
	
	public void remove(GameObject object) {
		background.remove(object);
		targets.remove(object);
		projectiles.remove(object);
	}	
	
	public void tick() {
		randomlyCreateNewTargetsOrDont();
		for(GameObject object : background) {
			object.tick();
		}
		background.update();
		
		for(GameObject object : targets) {
			object.tick();
		}
		targets.update();
		
		for(GameObject object : projectiles) {
			object.tick();
		}
		projectiles.update();
		
		protagonist.tick();
	}

	Random generator = new Random();

	private void randomlyCreateNewTargetsOrDont() {
		if (targetCreateCountdown-- > 0) return;
		
		if (targets.size() > 4) return;
		if (generator.nextInt(10) > 2) return;

		int direction = generator.nextInt(2) == 0 ? 1 : -1;
		int x = direction == 1 ? bounds.left : bounds.right;
		int speed = generateKittySpeed();
		
		addTarget(new Kitty(kittyImages1, new Point(x, bounds.bottom-(ledge+facade+5 + generator.nextInt(sidewalk-10))), direction, speed));
		
		targetCreateCountdown = 10 + generator.nextInt(5);
	}

	public int generateKittySpeed() {
		return generator.nextInt(5) + 3;
	}

	public boolean isVisible(GameObject object) {
		return bounds.contains(object.location);
	}

	protected void initialize() {
		int middle = bounds.getMiddle();
		setProtagonist(new Protagonist(context, new Point(middle, bounds.bottom)));
	}

	public void setProtagonist(Protagonist protagonist) {
		protagonist.setGame(this);
		this.protagonist = protagonist;
	}

	public void addBrick(Point location) {
		float p = (bounds.getMiddle() - location.x) / half_building_top;
		float q = (half_building_top - half_building_bottom) / (facade / drop_speed);
		addProjectile(new Brick(location, new Point(q*p, -drop_speed), facade / drop_speed));
	}
}