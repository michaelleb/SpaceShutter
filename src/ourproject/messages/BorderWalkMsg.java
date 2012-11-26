package ourproject.messages;

import mark.geometry.Point2D;
import mark.geometry.Vector2D;

public class BorderWalkMsg implements InterMessage{
	
	
	
	private Point2D.Short location;
	private boolean direction;
	private Point2D.Short userPoint;
	
	
	public BorderWalkMsg(Point2D.Short location,boolean direction,Point2D.Short userPoint){
		this.direction=direction;
		this.location=location;
		this.userPoint=userPoint;
	}
	
	public Point2D.Short getLocation(){return location;}
	public boolean getDirection(){return direction;}
	public Point2D.Short getUserPoint(){return userPoint;}
	
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
	
	public byte[] toBytes(MessageConvertion mc){
		return mc.messageToBytes(this);
	}
}
