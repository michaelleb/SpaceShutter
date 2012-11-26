/**
 * 
 */
package com.andrapp.spaceshutter.util;

/**
 * @author Misha
 *Class that represent point (x,y) of short type coordinates.
 */
public class PointS{

	public short x;
	public short y;
	
	

	public PointS() {
		super();
	}
	public PointS(PointS other) {
		super();
		this.x = other.getX();
		this.y = other.getY();
	}
	
	public PointS(short x, short y) {
		super();
		this.x = x;
		this.y = y;
	}
	public PointS(int x, int y) {
		super();
		this.x = (short)x;
		this.y = (short)y;
	}
	
	public void add(PointS other){
		this.x += other.x;
		this.y += other.y;
	}
	
	public void add(Vector2D vec){
		this.x += vec.x;
		this.y += vec.y;
	}
	
	public void add(int x, int y){
		this.x += x;
		this.y += y;

	}

	public int getSum(){
		return this.x + this.y;
	}
	@Override
	public String toString() {
		return "PointS [x=" + x + ", y=" + y + "]";
	}
	
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	
	public boolean equals(PointS other) {
		
		if (other == null)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		
		return true;
	}
	
	
	//getters and setters
	public void set(PointS other){
		this.x = other.x;
		this.y = other.y;
	}
	public short getX() {
		return x;
	}
	public void setX(int x) {
		this.x = (short)x;
	}
	public void setX(short x) {
		this.x = x;
	}
	public short getY() {
		return y;
	}
	public void setY(int y) {
		this.y = (short)y;
	}
	public void setY(short y) {
		this.y = y;
	}
	
	
	
	
	
	
	
	

}
