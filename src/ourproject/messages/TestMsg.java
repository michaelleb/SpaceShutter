package ourproject.messages;

import java.nio.*;

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

		this.dir=dir;

		upoint=userPoint;

		purp=purpose;
	}

	public TestMsg(byte[] bytes){

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bytes.length);

		myBuffer.put(bytes);

		this.location=new Point2D.Short(myBuffer.getShort(0),myBuffer.getShort(2));

		this.orientation=new Vector2D.Short(myBuffer.getShort(4),myBuffer.getShort(6));
		this.dir=myBuffer.getShort(8);

		this.upoint=new Point2D.Short(myBuffer.getShort(10),myBuffer.getShort(12));

		this.purp=myBuffer.getShort(14);
	}


	public byte[] getBytes(){

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(16);

		myBuffer.putShort(0, location.getx());
		myBuffer.putShort(2, location.gety());

		myBuffer.putShort(4,orientation.getVx());
		myBuffer.putShort(6, orientation.getVy());

		myBuffer.putShort(8, dir);
		myBuffer.putShort(10, upoint.getx());
		myBuffer.putShort(12, upoint.gety());
		myBuffer.putShort(14, purp);

		byte[] res = new byte[20];

		for(int i=0;i<16;i++)
			res[i] = myBuffer.get(i);

		return res;
	}

	public Point2D.Short getLocation(){return location;}

	public Vector2D.Short getOrientation(){return orientation;}

	public short getDirection(){return dir;}

	public Point2D.Short getUserPoint(){return upoint;}

	public short getPurp(){return purp;};
}
