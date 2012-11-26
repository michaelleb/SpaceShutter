package ourproject.messages;

import mark.geometry.*;

public class StopCutMsg implements InterMessage{
	
	
	private Point2D.Short location;
	
	public StopCutMsg(Point2D.Short location){
		this.location=location;
	}
	
	public Point2D.Short getLocation(){return location;}
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
	
	public byte[] toBytes(MessageConvertion mc){
		return mc.messageToBytes(this);
	}
}
