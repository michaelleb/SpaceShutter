package ourproject.messages;

import java.nio.*;
import java.util.ArrayList;

import mark.geometry.*;
import mark.geometry.Point2D.Short;
import android.util.Log;

public class MessageConvertion {
	
	private static int PREFIX_SIZE=3;
	
	private static byte START_CUT_MSG=0x011;
	private static byte PROCEED_CUT_MSG=0x012;
	private static byte BOUND_WALK=0x013;
	private static byte UPDATE_POLY_MSG=0x014;
	private static byte STOP_CUT_MSG=0x015;
	private static byte MONS_UPD_MSG=0x016;

	public InterMessage bytesToMessage(byte[] bytes){
		
		if(bytes.length<PREFIX_SIZE)
			return null;
		
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bytes.length);
		myBuffer.put(bytes);

		InterMessage message=null;

		//Log.e("","######"+myBuffer.getChar(0));

		if(myBuffer.get(0)==START_CUT_MSG){

			//Log.e("","START_CUT_MSG");

			message=new StartCutMsg(
					new Point2D.Short(myBuffer.getShort(3),myBuffer.getShort(5)),
					new Vector2D.Short(myBuffer.getShort(7), myBuffer.getShort(9))
					);
		}
		if(myBuffer.get(0)==PROCEED_CUT_MSG){

			//Log.e("","PROCEED_CUT_MSG");

			message=new ProcCutMsg(
					new Point2D.Short(myBuffer.getShort(3),myBuffer.getShort(5)),
					new Vector2D.Short(myBuffer.getShort(7), myBuffer.getShort(9))
					);
		}
		if(myBuffer.get(0)==BOUND_WALK){

			//Log.e("","BOUND_WALK");

			message=new BorderWalkMsg(
					new Point2D.Short(myBuffer.getShort(3),myBuffer.getShort(5)),
					(myBuffer.getChar(11)=='1'),
					new Point2D.Short(myBuffer.getShort(7), myBuffer.getShort(9))
					);
		}
		if(myBuffer.get(0)==UPDATE_POLY_MSG){

			Polygon2D.Short poly = new Polygon2D.Short();

			int num = (myBuffer.getShort(1)-2)/4;

			short cnt = myBuffer.getShort(3);

			for(int i=0;i<num;i++){

				short arg1 = myBuffer.getShort(i*4+5);
				short arg2 = myBuffer.getShort(i*4+7);

				if(i==0)
					poly.start(arg1, arg2);
				else
					poly.proceed(arg1, arg2);

			}

			message = new BoundsUpdateMsg(poly,cnt);
		}
		if(myBuffer.get(0)==STOP_CUT_MSG){
			message=new StopCutMsg(
					new Point2D.Short(myBuffer.getShort(3),myBuffer.getShort(5))
					);
		}
		if(myBuffer.get(0)==MONS_UPD_MSG){

			int num = myBuffer.getShort(1)/8;
			
			message=new MonsterUpdateMsg();

			for(int i=0;i<num;i++){

				short arg1 = myBuffer.getShort(i*8+3);
				short arg2 = myBuffer.getShort(i*8+5);
				short arg3 = myBuffer.getShort(i*8+7);
				short arg4 = myBuffer.getShort(i*8+9);

				((MonsterUpdateMsg)message).positions.add(new Point2D.Short(arg1,arg2));
				((MonsterUpdateMsg)message).orientations.add(new Vector2D.Short(arg3,arg4));

			}


		}

		return message;
	}




	public byte[] messageToBytes(MonsterUpdateMsg Message){

		short length=(short)(Message.positions.size()*8);
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);
		myBuffer.put(MONS_UPD_MSG);

		myBuffer.putShort(1, length);

		for(int i=0;i<Message.positions.size();i++){
			myBuffer.putShort(3+i*8, Message.positions.get(i).getx());
			myBuffer.putShort(5+i*8, Message.positions.get(i).gety());
			myBuffer.putShort(7+i*8, Message.orientations.get(i).getVx());
			myBuffer.putShort(9+i*8, Message.orientations.get(i).getVy());
		}

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}








	public byte[] messageToBytes(BoundsUpdateMsg Message){

		Polygon2D.Short poly = Message.getPoly();

		short length=(short)(poly.getSize()*4+2);
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);
		myBuffer.put(UPDATE_POLY_MSG);

		myBuffer.putShort(1, length);

		myBuffer.putShort(3, Message.getNum());

		for(int i=0;i<poly.getSize();i++){
			myBuffer.putShort(5+i*4, poly.getPoint(i).getx());
			myBuffer.putShort(7+i*4, poly.getPoint(i).gety());
		}

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}




	public byte[] messageToBytes(StartCutMsg Message){

		short length=8;
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);

		myBuffer.put(START_CUT_MSG);

		myBuffer.putShort(1, length);

		myBuffer.putShort(3, Message.getLocation().getx());
		myBuffer.putShort(5, Message.getLocation().gety());
		myBuffer.putShort(7, Message.getOrientation().getVx());
		myBuffer.putShort(9, Message.getOrientation().getVy());

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}

	public byte[] messageToBytes(ProcCutMsg Message){


		short length=8;
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);

		myBuffer.put(PROCEED_CUT_MSG);

		myBuffer.putShort(1, length);

		myBuffer.putShort(3, Message.getLocation().getx());
		myBuffer.putShort(5, Message.getLocation().gety());
		myBuffer.putShort(7, Message.getOrientation().getVx());
		myBuffer.putShort(9, Message.getOrientation().getVy());

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}

	public byte[] messageToBytes(BorderWalkMsg Message){

		short length=10;
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);

		myBuffer.put(BOUND_WALK);

		myBuffer.putShort(1, length);

		myBuffer.putShort(3, Message.getLocation().getx());
		myBuffer.putShort(5, Message.getLocation().gety());
		myBuffer.putShort(7, Message.getUserPoint().getx());
		myBuffer.putShort(9, Message.getUserPoint().gety());
		myBuffer.putChar(11, (Message.getDirection()==true?'1':'0'));

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}

	public byte[] messageToBytes(StopCutMsg Message){

		short length=4;
		short bufflength=(short)(length+PREFIX_SIZE);

		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bufflength);

		myBuffer.put(STOP_CUT_MSG);

		myBuffer.putShort(1, length);

		myBuffer.putShort(3, Message.getLocation().getx());
		myBuffer.putShort(5, Message.getLocation().gety());

		byte[] res = new byte[bufflength];

		for(int i=0;i<bufflength;i++) res[i] = myBuffer.get(i);

		return res;
	}

	
	
	
	
	
	
	
	
	
	public byte[] messagesToBytes(ArrayList<InterMessage> messages){

		byte[] res= new byte[0];

		for(int i=0;i<messages.size();i++){

			byte[] bytes = messages.get(i).toBytes(this);


			byte[] res2=new byte[res.length+bytes.length];

			for(int j=0;j<res.length;j++)
				res2[j]=res[j];

			for(int j=0;j<bytes.length;j++)
				res2[res.length+j]=bytes[j];

			res=res2;
		}

		return res;

	}



	public InterMessage[] bytesToMessages(byte[] bytes){
		
		
		
		InterMessage[] res = new InterMessage[0];
		
		
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bytes.length);
		myBuffer.put(bytes);

		int i=0;

		while(true){
			
			if(i>=bytes.length-2 || bytes[i] <0x011 || bytes[i]>0x016)
				break;
			
			short len = myBuffer.getShort(i+1);

			byte[] btmsg = new byte[len+PREFIX_SIZE];
			
			for(int j=0;j<len+PREFIX_SIZE;j++){
				
				if(i+j<bytes.length)
					btmsg[j]=bytes[i+j];
				else
					break;
				
			}
			
			InterMessage message = bytesToMessage(btmsg);
			
			InterMessage[] resExtended = new InterMessage[res.length+1];
			
			for(int j=0;j<res.length;j++)
				resExtended[j]=res[j];
			
			resExtended[res.length]=message;
			
			res=resExtended;
			
			i+=len+PREFIX_SIZE;
			
		}
		
		
		return res;
	}

}
