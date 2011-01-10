package ca.rokc.kittymunch.game;

import ca.rokc.kittymunch.geometry.Point;

public class Projectile extends GameObject {
	public int height;
	private Point velocity;

	public Projectile(Point location, Point velocity, int height) {
		super(location);
		this.velocity = velocity;
		this.height = height;
	}
	
	@Override
	public void doTick() {
		if (--height == 0) {
			game.remove(this);
		}
		location.moveBy(velocity);
	}

	@Override
	public void collideWith(GameObject object) {
		if (height > 1) return;
		object.hitByBrick(this);
	}
}