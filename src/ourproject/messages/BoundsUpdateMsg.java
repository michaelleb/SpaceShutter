package ourproject.messages;

import mark.geometry.*;

public class BoundsUpdateMsg implements InterMessage{
	
	private Polygon2D.Short poly;
	
	public BoundsUpdateMsg(Polygon2D.Short poly){
		this.poly=poly;
	}
	
	public Polygon2D.Short getPoly(){return poly;}
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
