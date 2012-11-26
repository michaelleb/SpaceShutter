package ourproject.messages;

import com.andrapp.spaceshutter.*;

public interface InterMessage {
	
	
	public void getProcessed(MessageProcessing msgprcs);
	
	public byte[] toBytes(MessageConvertion mc);
}
