package mark.geometry;

public class Vector2D {
	
	float vx;
	float vy;
	
	
	public Vector2D(float vx,float vy){
		this.vx=vx;
		this.vy=vy;
	}
	
	public void setLength(float newLength){
		
		float len=getLength();
		
		vx=vx/len*newLength;
		vy=vy/len*newLength;
		
	}
	
	public float getLength(){
		
		return (new Line2D(new Point2D(0,0),new Point2D(vx,vy))).getLength();
	}
	
	public float getVx(){return vx;}
	public float getVy(){return vy;}
	
}
