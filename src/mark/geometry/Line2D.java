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
		
		Vector2D res = new Vector2D(end.getx()-start.getx(),end.gety()-start.gety());
		
		res.setLength(newLen);
		
		return res;
	}
	
}
