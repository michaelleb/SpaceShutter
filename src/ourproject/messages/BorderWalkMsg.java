package ourproject.messages;

public class BorderWalkMsg implements InterMessage{
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
