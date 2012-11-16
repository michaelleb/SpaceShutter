package ourproject.messages;

import mark.geometry.*;

public class BoundsUpdateMsg implements InterMessage{
	
	private Polygon2D.Short poly;
	private short num;
	
	public BoundsUpdateMsg(Polygon2D.Short poly,short num){
		this.poly=poly;
		this.num=num;
	}
	
	public Polygon2D.Short getPoly(){return poly;}
	public short getNum(){return num;}
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
}
