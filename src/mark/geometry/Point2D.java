package mark.geometry;


public class Point2D{
	private float x;
	private float y;

	public Point2D(float x,float y){
		this.x=x;
		this.y=y;
	}
	
	public Point2D(Point2D other){
		this.x=other.x;
		this.y=other.y;
	}

	public float getx(){return x;}
	public float gety(){return y;}

	public void setx(float xx){
		this.x=xx;
	}
	public void sety(float yy){
		this.y=yy;
	}

	public float distance(Point2D other){
		return (float)Math.sqrt(
				(other.x - this.x)*(other.x-this.x)
				+
				(other.y - this.y)*(other.y-this.y)
				);
	}
	
	public void add(Vector2D other){
		this.x+=other.getVx();
		this.y+=other.getVy();
	}
}
