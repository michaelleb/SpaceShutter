package ourproject.messages;

import java.nio.*;

public class MessageConvertion {
	
	public static byte START_CUT_MSG=0x40;
	public static byte PROCEED_CUT_MSG=0x41;
	public static byte BOUND_WALK=0x42;
	
	public MessageConvertion(byte[] bytes){
		
		
		
		ByteBuffer myBuffer = ByteBuffer.allocateDirect(bytes.length);
		
		myBuffer.put(bytes);
		
		
		
		
	}

	public MessageConvertion(InterMessage Message){

	}

	public InterMessage getMessage(){		
		return null;
	}
	
	public byte[] getBytes(){
		return null;
	}

}
