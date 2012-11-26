package ourproject.messages;

import mark.geometry.*;

public class BoundsUpdateMsg implements InterMessage{
	
	private Polygon2D.Short poly;
	
	private short cnt;
	
	public short getNum(){return cnt;}
	
	public BoundsUpdateMsg(Polygon2D.Short poly,short cnt){
		this.poly=poly;
		this.cnt=cnt;
	}
	
	public Polygon2D.Short getPoly(){return poly;}
	
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
	
	public byte[] toBytes(MessageConvertion mc){
		return mc.messageToBytes(this);
	}
}
