/**
 * 
 */
package com.andrapp.spaceshutter.util;

/**
 * @author Misha
 * parametric representation of line
 */
public class Line2D {
	
	
	public final static int RESULT_NO_POINT	= -1;
	public final static int RESULT_ONE_POINT = 0;
	public final static int RESULT_ANY_POINT = 1;
	
	private final static String CAN_NOT_CALCULATE_VALUE_ON_VERTICAL_LINE =	 "can not calculate value of vertical line";
	private final static String DIRECTION_VECTOR_CAN_NOT_BE_ZERO 	= "Line direction vector cannot be zero vector";
	protected int x;//known point of line
	protected int y;
	
	private Vector2D directionVector = null;

	
	/**
	 * create default line whit point (0,0) and  directioVector [1,0]
	 */
	public Line2D() {
		super();
		this.x = 0;
		this.y = 0;
		this.setDirectionVector(new Vector2D(1,0)); // [1,0] is default direction
	
	}

	/**
	 * direction Vector parameters cannot specify zero vector 
	 */
	public Line2D(int x, int y, int v1, int v2) {
		this.x = x;
		this.y = y;
		
		this.setDirectionVector(new Vector2D(v1, v2));
	}

	public void setPoint(int x, int y) {
		this.x = x;
		this.y = y;	
	}
	
	public void setPoint(Point2D point) {
		this.x = point.x;
		this.y = point.y;	
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


	public void findCommonPointWith(Line2D otherLine,Integer resultX,Integer resultFlag ) {
		

		//if this is same line then return self anchor point or throw exception
		if(this.isCollineareWith(otherLine)){
			resultX = this.x;
			resultFlag = Line2D.RESULT_ANY_POINT;
		}
		
	}
	
	/**
	 * 
	 * @param other line
	 * @return true if other line represent same line in 2D. 
	 */
	public boolean isCollineareWith(Line2D other) {

		//the vectors of line should be linearly dependent
		if(!this.getDirectionVector().isLinearlyDependentWith(other.getDirectionVector()))
			return false;
		if(! this.passingThoughPoint(other.x,other.y) )
			return false;
		
		return true;
	}
	
	public boolean passingThoughPoint(int x, int y){
			
		//calculate vector between this line anchor point and given point
		int v1 = x - this.x;
		int v2 = y - this.y;
		
		//if new vector is dependent with this line direction vector 
		//then the point is on this line
		return this.getDirectionVector().isLinearlyDependentWith(v1,v2);
	}



	public Vector2D getDirectionVector() {
		return directionVector;
	}



	public void setDirectionVector(Vector2D directionVector)throws IllegalArgumentException {
		
		if(directionVector.norm() == 0)
			throw new IllegalArgumentException(DIRECTION_VECTOR_CAN_NOT_BE_ZERO);
		this.directionVector = directionVector;
	}

	public Point2D findNearestPointTo(Point2D point) {
		
		Point2D resultPoint = null;
		if(this.passingThoughPoint(point.x, point.y)){
			resultPoint = new Point2D(point);
		}
		else if(this.isHorizintal()){
			//horizontal line have same y for each x.
			resultPoint = new Point2D(point.x,this.y);
		}
		else if(this.isVertical()){
			//vertical line have same x for each y.
			resultPoint = new Point2D(this.x, point.y);
		}
		else{
			//L1 | x = x0 + k*u1
			//	 | y = y0 + k*u2
			
			//L2 | x = x1 + s*v1
			//   | y = y1 + s*v2

			int x0 = this.x;
			int y0 = this.y;
			int u1 = this.getDirectionVector().x;
			int u2 = this.getDirectionVector().y;
			int k; //unknown
			
			int x1 = point.x;
			int y1 = point.y;
			int v1 = -u2;	//Orthogonal vector to this line
			int v2 = u1;
			double s; //unknown
			
			double a = v2/u1 - v1/u1;
			double b = -(y1 - y0)/u2 + (x1 - x0)/u1;
			
			s = b/a;
			
			int resultX = (int)Math.round(x1 + s*v1);
			int resultY = (int)Math.round(y1 + s*v2);
			
			resultPoint = new Point2D(resultX, resultY);

			
		}
		return resultPoint;
			
	}

	public int getValuetOfCoordinate(int x) {

		if(this.isVertical()){
			throw new IllegalArgumentException("CAN_NOT_CALCULATE_VALUE_ON_VERTICAL_LINE");
		}
		
		double times = (x - this.x) / this.directionVector.x;
		int result  = (int)Math.round(this.y + times * this.directionVector.y);
		return result;
	}

	public boolean isHorizintal() {
		
		return this.directionVector.y == 0; 
	}

	public boolean isVertical() {

		return this.directionVector.x == 0;
	}
	
	




	

	

}
