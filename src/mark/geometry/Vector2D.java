package mark.geometry;

public class Vector2D {
	
	private float vx;
	private float vy;
	
	
	public Vector2D(float vvx,float vvy){
		this.vx=vvx;
		this.vy=vvy;
	}
	
	public void setLength(float newLength){
		
		float len=getLength();
		
		vx=vx/len*newLength;
		vy=vy/len*newLength;
		
	}
	
	public float getLength(){
		
		return (new Line2D(new Point2D(0,0),new Point2D(vx,vy))).getLength();
	}
	
	public float getVx(){return this.vx;}
	public float getVy(){return this.vy;}
	
	public void setVx(float vx){this.vx=vx;}
	public void setVy(float vy){this.vy=vy;}
	
}
