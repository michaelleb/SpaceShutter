package ourproject.messages;

public class ProcCutMsg implements InterMessage{
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
