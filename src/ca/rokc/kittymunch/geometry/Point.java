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
package ca.rokc.kittymunch.geometry;

/**
 * Instances of this class represent a point in two-dimensional space.
 * The implementation is based on integer (int) values, so some calculations
 * are approximate. Note that instances are intended to be mutable. A point
 * can be moved.
 */
public class Point {

	public double x;
	public double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point location) {
		this(location.x, location.y);
	}

	/**
	 * Since we use int values to represent everything, it's really not possible
	 * represent an actual unit vector. Instead, as part of the request, we
	 * allow a flexible definition of "unit", returning a vector of
	 * approximately the given length (we are working with ints after all) in
	 * the direction of the given point.
	 * 
	 * @param point
	 *            An instance of {@link Point}. Must not be <code>null</code>.
	 * @param unit
	 *            The definition of unit for the vector; the resulting vector
	 *            will be approximately this long.
	 * @return A vector of the requested approximate length
	 */
	public Point unitVectorTo(Point point, int unit) {
		double length = distanceTo(point);
		double h = ((point.x - x) * unit / length);
		double v = ((point.y - y) * unit / length);
		return new Point(h, v);
	}

	/**
	 * Answers true if the receiver is within a certain distance of a point.
	 * 
	 * @param point
	 *            The point we're testing against. Cannot be <code>null</code>.
	 * @param distance
	 *            The distance threshold
	 * @return <code>true</code> or <code>false</code>.
	 */
	public boolean isProximateTo(Point point, int distance) {
		return distanceTo(point) < distance;
	}
	
	public double distanceTo(Point point) {
		return Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
	}

	/**
	 * Update the state of the receiver such that it is located within
	 * the rectangle.
	 * 
	 * @param rectangle Must not be <code>null</code>.
	 */
	public void moveInside(Rectangle rectangle) {
		x = Math.min(Math.max(x, rectangle.left), rectangle.right);
		y = Math.min(Math.max(y, rectangle.top), rectangle.bottom);
	}

	public void moveBy(Point velocity) {
		x += velocity.x;
		y += velocity.y;
	}
}
