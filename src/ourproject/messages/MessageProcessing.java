package ourproject.messages;

import android.util.Log;

public class MessageProcessing{

	public void processMessage(InterMessage msg){
		
		if(msg!=null)
			msg.getProcessed(this);
	}
	
	
	public void process(StartCutMsg msg){}
	public void process(StopCutMsg msg){}
	
	public void process(ProcCutMsg msg){}
	
	public void process(BorderWalkMsg msg){}
	
	public void process(BoundsUpdateMsg msg){}
	
	public void process(MonsterUpdateMsg msg){}
}
