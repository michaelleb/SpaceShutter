
package com.andrapp.spaceshutter.util;

public class Point2D {
	
	
	protected int x;
	protected int y;
	
	
	/**
	 * create default (0,0) point
	 */
	public Point2D() {
		super();
		this.x = 0;
		this.y = 0;
	}


	public Point2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	

	public Point2D(Point2D other) {
		this.x = other.x;
		this.y = other.y;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Point2D)) {
			return false;
		}
		Point2D other = (Point2D) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}


	public void set(Point2D other) {
		this.x = other.x;
		this.y = other.y;
	}


	public void add(Vector2D vector) {
		this.x += vector.getX();
		this.y += vector.getY();
		
	}


	public void add(int x, int y) {
		this.x += x;
		this.y += y;
		
	}


	public boolean equals(int x, int y) {
		if(this.x == x && this.y == y ){
			return true;
		}
		return false;
	}
	
	
	
	
	
	

}
