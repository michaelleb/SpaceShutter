package ourproject.messages;

public class MessageProcessing{

	public void processMessage(InterMessage msg){

		msg.getProcessed(this);
	}
	
	public void process(StartCutMsg msg){
		
	}
	
	public void process(ProcCutMsg msg){
		
	}
	
	public void process(BorderWalkMsg msg){
		
	}
}
