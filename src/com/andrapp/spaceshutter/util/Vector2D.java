package com.andrapp.spaceshutter.util;

/**
 * @author Misha
 * 
 */
public class Vector2D {

	protected int x;
	protected int y;

	/**
	 * create default [0,0] directioVector
	 */
	public Vector2D() {
		super();
		this.x = 0;
		this.y = 0;
	}

	/**
	 * crate directioVector from given points by calculating directioVector =
	 * end - start
	 * 
	 * @param a
	 * @param b
	 */
	public Vector2D(Point2D start, Point2D end) {

		this.set(start, end);
	}

	/**
	 * create directioVector with given coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * create directioVector from other directioVector
	 * 
	 * @param other
	 */
	public Vector2D(Vector2D other) {
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * set this directioVector to result of calculation: this.vector = end -
	 * start
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Vector2D set(Point2D start, Point2D end) {

		this.x = end.x - start.x;
		this.y = end.y - start.y;

		return this;
	}

	/**
	 * Computes the norm of the directioVector
	 * 
	 * @return norm of this directioVector in double precision
	 */
	public double norm(int dummy) {

		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Computes the rounded to nearest integer norm of the directioVector
	 * 
	 * @return norm of this directioVector rounded to nearest integer
	 */
	public int norm() {
		return (int) Math.round(Math.sqrt(x * x + y * y));
	}

	/**
	 * Returns the sum of current directioVector with directioVector given as
	 * parameter.
	 * 
	 * @param v
	 * @return this
	 */
	public Vector2D plus(Vector2D v) {

		this.x += v.x;
		this.y += v.y;

		return this;
	}

	/**
	 * sum given directioVector to this directioVector and return this one
	 * 
	 * @param v1
	 * @param v2
	 * @return this
	 */
	public Vector2D plus(int v1, int v2) {
		this.x += v1;
		this.y += v2;

		return this;

	}

	/**
	 * Returns the subtraction of current directioVector with directioVector
	 * given as parameter.
	 * 
	 * @param v
	 * @return this
	 */
	public Vector2D minus(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;

		return this;
	}

	/**
	 * Multiplies the directioVector by a scalar amount. then round the value to
	 * nearest integer
	 * 
	 * @param k
	 * @return this
	 */
	public Vector2D times(float k) {

		this.x = Math.round(this.x * k);
		this.y = Math.round(this.y * k);

		return this;
	}

	/**
	 * @param v
	 * @return dot product of this directioVector
	 */
	public int dot(Vector2D v) {

		return this.x * v.x + this.y * v.y;

	}

	public void set(int v1, int v2) {
		this.x = v1;
		this.y = v2;

	}

	/**
	 * Convenience method that delegate to isLinearlyDependentWith(int v1, int
	 * v2).
	 * 
	 * @param otherVector
	 * @return
	 */
	public boolean isLinearlyDependentWith(Vector2D otherVector) {

		return isLinearlyDependentWith(otherVector.x, otherVector.y);
	}

	public boolean isLinearlyDependentWith(int v1, int v2) {

		// if the determinant is 0 then they are linearly dependent
		if (this.x * v2 - v1 * this.y == 0)
			return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vector2D)) {
			return false;
		}
		Vector2D other = (Vector2D) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {

		return this.y;
	}

	public void setX(int x) {
		this.x = x;

	}

	public void setY(int y) {
		this.y = y;

	}

	/**
	 * @param otherVector
	 * @return true if the other is same vector multiplied by positive value
	 */
	public boolean isHaveSameDirectionAs(Vector2D other) {

		if (!this.isLinearlyDependentWith(other.x, other.y)) {
			return false;
		}
		//get the sign of multiplication
		int xMult = this.x * other.x;	
		int yMult = this.y * other.y;

		
		if (this.x != other.x && xMult == 0 || xMult < 0) {
			return false;
		}
		if (this.y != other.y && yMult == 0 || yMult < 0) {
			return false;
		}

		return true;
	}

}
