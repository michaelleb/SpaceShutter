package ourproject.messages;

public class StartCutMsg implements InterMessage{
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
