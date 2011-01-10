package ca.rokc.kittymunch.geometry;

public class Rectangle {

	public int left;
	public int top;
	public int right;
	public int bottom;

	public Rectangle(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public boolean contains(Point point) {
		if (point.x < left) return false;
		else if (point.x > right) return false;
		else if (point.y < top) return false;
		else if (point.y > bottom) return false;
		else
			return true;
	}

	public int getWidth() {
		return right-left;
	}

	public int getMiddle() {
		return left + (right-left) / 2;
	}
}
