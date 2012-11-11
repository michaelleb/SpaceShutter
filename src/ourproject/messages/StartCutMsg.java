package ourproject.messages;

import mark.geometry.*;


public class StartCutMsg {
	
	Point2D location;
	Vector2D orientation;
	
	public StartCutMsg(Point2D location,Vector2D orientation){
		
		this.location=location;
		this.orientation=orientation;
	}
	
	public StartCutMsg(byte[] bytes){
		
		float[] msgarr = (float[])MySerialization.deserialize(bytes);
		
		this.location=new Point2D(msgarr[0],msgarr[1]);
		this.orientation=new Vector2D(msgarr[2],msgarr[3]);
		
	}
	
	
	public byte[] getBytes(){
		
		float[] msgarr=new float[4];
		
		msgarr[0]=location.getx();
		msgarr[1]=location.gety();
		msgarr[2]=orientation.getVx();
		msgarr[3]=orientation.getVy();
		
		byte[] bytemsg = MySerialization.serialize(msgarr);
		
		return bytemsg;
	}
	
	public Point2D getLocation(){return location;}
	
	public Vector2D getOrientation(){return orientation;}
	
}
