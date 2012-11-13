package ourproject.messages;

import mark.geometry.*;

public class ProcCutMsg implements InterMessage{

	
	private Point2D.Short location;
	private Vector2D.Short orientation;
	
	public ProcCutMsg(Point2D.Short location,Vector2D.Short orientation){
		this.location=location;
		this.orientation=orientation;
	}
	
	public Point2D.Short getLocation(){return location;}
	public Vector2D.Short getOrientation(){return orientation;}
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
