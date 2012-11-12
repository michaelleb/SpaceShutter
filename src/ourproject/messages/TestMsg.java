package ourproject.messages;

import mark.geometry.*;
import android.util.Log;

public class TestMsg {
	
	private Point2D.Short location;
	private Vector2D.Short orientation;
	
	private short dir;
	
	private Point2D.Short upoint;
	
	private short purp;
	
	
	public TestMsg(short purpose,Point2D.Short location,Vector2D.Short orientation,short dir,Point2D.Short userPoint){
		
		this.location=location;
		this.orientation=new Vector2D.Short(orientation.getVx(),orientation.getVy());
		
		//Log.e(""," +++ "+orientation.getVx()+" "+orientation.getVy());
		
		
		this.dir=dir;
		
		upoint=userPoint;
		
		purp=purpose;
	}
	
	public TestMsg(byte[] bytes){
		
		short[] msgarr = (short[])MySerialization.deserialize(bytes);
		
		this.location=new Point2D.Short(msgarr[0],msgarr[1]);
		this.orientation=new Vector2D.Short(msgarr[2],msgarr[3]);
		this.dir=msgarr[4];
		
		this.upoint=new Point2D.Short(msgarr[5],msgarr[6]);
		
		this.purp=msgarr[7];
		
	}
	
	
	public byte[] getBytes(){
		
		short[] msgarr=new short[8];
		
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
	
	public Point2D.Short getLocation(){return location;}
	
	public Vector2D.Short getOrientation(){return orientation;}
	
	public short getDirection(){return dir;}
	
	public Point2D.Short getUserPoint(){return upoint;}
	
	public short getPurp(){return purp;};
}
