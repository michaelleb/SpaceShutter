package ourproject.messages;

import java.nio.*;
import mark.geometry.*;
import android.util.Log;

public class MessageConvertion {

	private static byte START_CUT_MSG=0x01;
	private static byte PROCEED_CUT_MSG=0x02;
	private static byte BOUND_WALK=0x03;
	private static byte UPDATE_POLY_MSG=0x04;
	private static byte STOP_CUT_MSG=0x05;
	
	public static InterMessage bytesToMessage(byte[] bytes){
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bytes.length);
		myBuffer.put(bytes);
		
		InterMessage message=null;
		
		//Log.e("","######"+myBuffer.getChar(0));
		
		if(myBuffer.get(0)==START_CUT_MSG){
			
			//Log.e("","START_CUT_MSG");
			
			message=new StartCutMsg(
					new Point2D.Short(myBuffer.getShort(1),myBuffer.getShort(3)),
					new Vector2D.Short(myBuffer.getShort(5), myBuffer.getShort(7))
					);
		}
		if(myBuffer.get(0)==PROCEED_CUT_MSG){
			
			//Log.e("","PROCEED_CUT_MSG");
			
			message=new ProcCutMsg(
					new Point2D.Short(myBuffer.getShort(1),myBuffer.getShort(3)),
					new Vector2D.Short(myBuffer.getShort(5), myBuffer.getShort(7))
					);
		}
		if(myBuffer.get(0)==BOUND_WALK){
			
			//Log.e("","BOUND_WALK");
			
			message=new BorderWalkMsg(
					new Point2D.Short(myBuffer.getShort(1),myBuffer.getShort(3)),
					(myBuffer.getChar(9)=='1'),
					new Point2D.Short(myBuffer.getShort(5), myBuffer.getShort(7))
					);
		}
		if(myBuffer.get(0)==UPDATE_POLY_MSG){
			
			Polygon2D.Short poly = new Polygon2D.Short();
			
			//int verCnt = (bytes.length-1)/4+2;
			
			//Log.e("","verCnt: "+verCnt);
			
			short num = myBuffer.getShort(1);
			
			for(int i=0;i<1000;i++){
				
				short arg1 = myBuffer.getShort(i*4+3);
				short arg2 = myBuffer.getShort(i*4+5);
				
				if(i==0)
					poly.start(arg1, arg2);
				else{
					poly.proceed(arg1, arg2);
					
					if(arg1==myBuffer.getShort(3) && arg2==myBuffer.getShort(5))
						break;
				}
				
			}
			
			message = new BoundsUpdateMsg(poly,num);
		}
		if(myBuffer.get(0)==STOP_CUT_MSG){
			message=new StopCutMsg(
					new Point2D.Short(myBuffer.getShort(1),myBuffer.getShort(3))
					);
		}
		
		return message;
	}
	
	public static byte[] messageToBytes(BoundsUpdateMsg Message){
		
		Polygon2D.Short poly = Message.getPoly();
		
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(poly.getSize()*4+3);
		myBuffer.put(UPDATE_POLY_MSG);
		
		//Log.e("","messageToBytes: "+poly.getSize());
		
		myBuffer.putShort(1,Message.getNum());
		
		for(int i=0;i<poly.getSize();i++){
			myBuffer.putShort(3+i*4, poly.getPoint(i).getx());
			myBuffer.putShort(5+i*4, poly.getPoint(i).gety());
		}
		
		byte[] res = new byte[poly.getSize()*4+3];

		for(int i=0;i<res.length;i++) res[i] = myBuffer.get(i);
		
		return res;
	}
	
	
	
	
	public static byte[] messageToBytes(StartCutMsg Message){
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(9);

		myBuffer.put(START_CUT_MSG);
		myBuffer.putShort(1, Message.getLocation().getx());
		myBuffer.putShort(3, Message.getLocation().gety());
		myBuffer.putShort(5, Message.getOrientation().getVx());
		myBuffer.putShort(7, Message.getOrientation().getVy());

		byte[] res = new byte[9];

		for(int i=0;i<res.length;i++) res[i] = myBuffer.get(i);
		
		return res;
	}

	public static byte[] messageToBytes(ProcCutMsg Message){
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(9);

		myBuffer.put(PROCEED_CUT_MSG);
		myBuffer.putShort(1, Message.getLocation().getx());
		myBuffer.putShort(3, Message.getLocation().gety());
		myBuffer.putShort(5, Message.getOrientation().getVx());
		myBuffer.putShort(7, Message.getOrientation().getVy());

		byte[] res = new byte[9];

		for(int i=0;i<res.length;i++) res[i] = myBuffer.get(i);
		
		return res;
	}

	public static byte[] messageToBytes(BorderWalkMsg Message){
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(11);

		myBuffer.put(BOUND_WALK);
		myBuffer.putShort(1, Message.getLocation().getx());
		myBuffer.putShort(3, Message.getLocation().gety());
		myBuffer.putShort(5, Message.getUserPoint().getx());
		myBuffer.putShort(7, Message.getUserPoint().gety());
		myBuffer.putChar(9, (Message.getDirection()==true?'1':'0'));

		byte[] res = new byte[11];

		for(int i=0;i<res.length;i++) res[i] = myBuffer.get(i);
		
		return res;
	}
	
	public static byte[] messageToBytes(StopCutMsg Message){
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(5);

		myBuffer.put(STOP_CUT_MSG);
		myBuffer.putShort(1, Message.getLocation().getx());
		myBuffer.putShort(3, Message.getLocation().gety());

		byte[] res = new byte[5];

		for(int i=0;i<res.length;i++) res[i] = myBuffer.get(i);
		
		return res;
	}
	
}
