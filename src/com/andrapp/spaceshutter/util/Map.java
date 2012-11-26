package com.andrapp.spaceshutter.util;

import java.util.LinkedList;

import com.andrapp.spaceshutter.controllers.Ctrl;




//This class represent the map of the game
//it should be initiated with:
	//logic dimensions,

//This class should support capabilities:
	//1.accept cutting path and return true if this path cut the map,
	// then split the map to two parts with given path and remove the part that without General monster.
	//2.allow to test if some point is within map bounds ( not sure if that include the points of bordering path )
	//3.notify Ctrl when map is changed.
	//4.return path from point with direction to map border; 

//Development prescription 
//the map has its own movePaht that is always closed line.
//also map has incisionPathPool with  two incisionPahts, each time one of the players
//start cutting, he binds incision path and start to make incision by adding his path to incision path
//once incision reach other side of the map, the map is automatically remove one section
//if player dye, the incision restored to pool

public class Map{
	
	//map dimensions
	private final PointS leftUpPoint = new PointS(0,0);
	private final PointS rightDownPoint	= new PointS(600,1000);//3:5
	
	//map path
	public MovePath mapPath = new MovePath();
	
	//players incision paths
	private LinkedList<MovePath> incisionPaths = new LinkedList<MovePath>();
	
	//Drawing crayon
	private MapCrayon crayon;
	private Ctrl ctrl; // reference to controller of the game
	

	//C-tor
	public Map(MapCrayon mapCryon,Ctrl ct) {
		super();
		this.crayon = mapCryon;
		this.ctrl	= ct;
		this.crayon.setHost(this);
		this.crayon.init();
		
		//register mapCrayon in ctrl to be drawn
		ctrl.registerCrayon(crayon);
		
		//build initial map path:
		mapPath.addRect(leftUpPoint, rightDownPoint);
		
		
	}

	public boolean contains(int x, int y) { return false; };
	
	//params: path that cross this map. return cut area in cm2 (zero mean the path didn't separate the map to two parts
	public int clipLeastSignificantPart(MovePath p){ return 0;};
	
	public MovePath getPath(int x, int y, int direction){
		return null;
	}
	
	public MovePath getMapPath(){
		return mapPath;
	}

	
	public LinkedList<MovePath> getIncisionPaths() {
		return incisionPaths;
	}

	public MovePath bindIncisionPaht(){
		
		incisionPaths.addLast(new MovePath());
		
		return incisionPaths.getLast();
	}
	
	
	public void getPos(float relativeDistanceFromBeginning,Point2D result){
		
		mapPath.getRemotePoint(relativeDistanceFromBeginning, result);
		
	}
	

}
