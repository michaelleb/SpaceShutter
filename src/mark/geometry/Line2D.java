package mark.geometry;

public class Line2D {

	Point2D start;
	Point2D end;

	public Line2D(Point2D start,Point2D end){

		this.start=start;
		this.end=end;
	}

	public float getLength(){
		return start.distance(end);
	}

	public Point2D getStart(){
		return start;
	}

	public Point2D getEnd(){
		return end;
	}

	public Vector2D getVectorWidthLen(float newLen){

		Vector2D res = getVector();

		res.setLength(newLen);

		return res;
	}

	public Vector2D getVector(){

		Vector2D res = new Vector2D(end.getx()-start.getx(),end.gety()-start.gety());

		return res;
	}








	public Point2D lineIntersection(Line2D line2,boolean left,boolean right){
		return segIntersection(
				this.getStart().getx(),this.getStart().gety(),
				this.getEnd().getx(),this.getEnd().gety(),
				line2.getStart().getx(),line2.getStart().gety(),
				line2.getEnd().getx(),line2.getEnd().gety(),
				left,right
				);
	}


	protected Point2D segIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, 
			boolean includeLeftEdge, boolean includeRightEdge) 
	{ 
		float bx = x2 - x1; 
		float by = y2 - y1; 
		float dx = x4 - x3; 
		float dy = y4 - y3;
		float b_dot_d_perp = bx * dy - by * dx;
		if(b_dot_d_perp == 0) {
			return null;
		}
		float cx = x3 - x1;
		float cy = y3 - y1;
		float t = (cx * dy - cy * dx) / b_dot_d_perp;
		
		if(
		(t < 0 || (t <= 0  && !includeLeftEdge))
		||
		(t > 1 || (t >= 1 && !includeRightEdge))
		){
			return null;
		}
		float u = (cx * by - cy * bx) / b_dot_d_perp;
		if(
		(u < 0 || (u <= 0  && !includeLeftEdge))
		||
		(u > 1 || (u >= 1 && !includeRightEdge))
		){ 
			return null;
		}
		return new Point2D(x1+t*bx, y1+t*by);
	}


	public boolean isBetween(Point2D point){

		float wholelen = getLength();

		float len1 = (new Line2D(start,point)).getLength();
		float len2 = (new Line2D(end,point)).getLength();

		return (len1+len2>=wholelen-0.1f && len1+len2<=wholelen+0.1f);
	}

}
