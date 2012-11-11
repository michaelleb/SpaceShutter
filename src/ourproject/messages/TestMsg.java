package ourproject.messages;

import mark.geometry.*;
import android.util.Log;

public class TestMsg {
	
	private Point2D location;
	private Vector2D orientation;
	
	private float dir;
	
	private Point2D upoint;
	
	private float purp;
	
	
	public TestMsg(float purpose,Point2D location,Vector2D orientation,float dir,Point2D userPoint){
		
		this.location=location;
		this.orientation=new Vector2D(orientation.getVx(),orientation.getVy());
		
		//Log.e(""," +++ "+orientation.getVx()+" "+orientation.getVy());
		
		
		this.dir=dir;
		
		upoint=userPoint;
		
		purp=purpose;
	}
	
	public TestMsg(byte[] bytes){
		
		float[] msgarr = (float[])MySerialization.deserialize(bytes);
		
		this.location=new Point2D(msgarr[0],msgarr[1]);
		this.orientation=new Vector2D(msgarr[2],msgarr[3]);
		this.dir=msgarr[4];
		
		this.upoint=new Point2D(msgarr[5],msgarr[6]);
		
		this.purp=msgarr[7];
		
	}
	
	
	public byte[] getBytes(){
		
		float[] msgarr=new float[8];
		
		msgarr[0]=location.getx();
		msgarr[1]=location.gety();
		msgarr[2]=orientation.getVx();
		msgarr[3]=orientation.getVy();
		
		msgarr[4]=dir;
		msgarr[5]=upoint.getx();
		msgarr[6]=upoint.gety();
		msgarr[7]=purp;
		
		byte[] bytemsg = MySerialization.serialize(msgarr);
		
		return bytemsg;
	}
	
	public Point2D getLocation(){return location;}
	
	public Vector2D getOrientation(){return orientation;}
	
	public float getDirection(){return dir;}
	
	public Point2D getUserPoint(){return upoint;}
	
	public float getPurp(){return purp;};
}
