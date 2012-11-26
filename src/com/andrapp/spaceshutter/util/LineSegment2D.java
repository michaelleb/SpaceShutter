package com.andrapp.spaceshutter.util;

public class LineSegment2D {

	Point2D start;
	Point2D end;
	Vector2D vec;

	public LineSegment2D(Point2D start, Point2D end) {
		super();
		init(start, end);

	}

	public LineSegment2D(int startX, int startY, int endX, int endY) {
		this.init(new Point2D(startX, startY), new Point2D(endX, endY));

	}

	/**
	 * construct default line segment of [0,0]
	 */
	public LineSegment2D() {
		start = new Point2D(0, 0);
		end = new Point2D(0, 0);
		vec = new Vector2D(0, 0);
	}

	private void init(Point2D start, Point2D end) {
		this.start = start;
		this.end = end;

		vec = new Vector2D(start, end);
	}

	/**
	 * return true if given point (x,y) is on this lineSection
	 * 
	 * @param pointX
	 * @param pointY
	 * @return
	 */
	public boolean hasPoint(int pointX, int pointY) {

		// FIXME the solution of MovePath.getDistanceTo(..) is better
		if(this.start.equals(pointX, pointY)){
			return true;
		}
		int v1 = pointX - this.start.x;
		int v2 = pointY - this.start.y;
		Vector2D vectorFromStartToGivenPoint = new Vector2D(v1, v2);

		if (this.vec.isHaveSameDirectionAs(vectorFromStartToGivenPoint)) {
			return this.vec.dot(this.vec) >= vectorFromStartToGivenPoint
					.dot(vectorFromStartToGivenPoint);
		}
		return false;

	}

	public Point2D getStart() {
		return start;
	}

	public Point2D getEnd() {
		return end;
	}

	public void setStart(Point2D point) {
		this.start.set(point);
	}

	public void setEnd(Point2D end) {
		this.end.set(end);
	}

}
