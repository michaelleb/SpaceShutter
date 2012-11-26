package ourproject.messages;

import java.util.ArrayList;

import mark.geometry.*;

public class MonsterUpdateMsg implements InterMessage{
	
	public ArrayList<Point2D.Short> positions;
	public ArrayList<Vector2D.Short> orientations;
	
	public MonsterUpdateMsg(){
		positions=new ArrayList<Point2D.Short>();
		orientations=new ArrayList<Vector2D.Short>();
	}
	
	public ArrayList<Point2D.Short> getPositions(){return positions;}
	public ArrayList<Vector2D.Short> getOrientations(){return orientations;}
		
	public void getProcessed(MessageProcessing msgprcs){
		msgprcs.process(this);
	}
	
	public byte[] toBytes(MessageConvertion mc){
		return mc.messageToBytes(this);
	}
}
