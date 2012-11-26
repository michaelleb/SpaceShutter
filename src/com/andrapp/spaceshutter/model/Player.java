package com.andrapp.spaceshutter.model;

import android.util.Log;

import com.andrapp.spaceshutter.controllers.Ctrl;
import com.andrapp.spaceshutter.util.DragListener;
import com.andrapp.spaceshutter.util.FlingListener;
import com.andrapp.spaceshutter.util.Map;
import com.andrapp.spaceshutter.util.MovePath;
import com.andrapp.spaceshutter.util.PlayerCrayon;
import com.andrapp.spaceshutter.util.Point2D;
import com.andrapp.spaceshutter.util.PointS;
import com.andrapp.spaceshutter.util.SimpleDirection;
import com.andrapp.spaceshutter.util.TouchDownListener;
import com.andrapp.spaceshutter.util.TouchUpListener;
import com.andrapp.spaceshutter.util.Updatable;

//the player has one of those strategy: the differ on their notifications and observations
//Human(player on this device) :  get events on: touch,onMapChange. notify others on:onClicked(vibe if this opt is enabled), onChangeMoveDirection, on change state to safe he must notify the map with cut path
//Ai	: get events :onMapChange, has ref to other Players/Monsters. notify others on:  onChangeMoveDirection, on change state to safe he must notify the map with cut path
//manipulated (from other devise): receive events of self position + path/direction + Death. Responsible to decode posted (received) action from remote client and update self accordingly.notify others on: on change state to safe he must notify the map with cut path
//the player must be notified about touch evens:
//touch down
//touch up
//on flip or on long hold
//the player has those states: 
//safe: when he on map border
//cut : when he cutting the map
//the player must expose collision functionality: collide with Monster, collide whit bullet and so on... may also be in separate logic object to apply some modifications during the game like gifts of temporal immortality 
//to draw the player we should provide him with crayon that responsible for all drawing and thus decouple player logic from paint infrastructure.

//each time player born/reborn he request map position nearest to its default position and set the result as his position.
public class Player implements FlingListener,DragListener,TouchDownListener , TouchUpListener,Updatable {
	private final static String TAG = Player.class.getSimpleName();
	
	//selection state options
	private final static int STATE_SELECTED = 1;
	private final static int STATE_NOT_SELECTED = 0;
	private int selectionState = Player.STATE_NOT_SELECTED;
	
	//safe state options
	private final static int STATE_SAFE = 1;
	private final static int STATE_VULNERABLE = 0;
	private int safeState	 = Player.STATE_SAFE;
	
	// player position
	private int x;
	private int y;
	
	private int moveSpeed;
	private MovePath movePath;
	
	//vibration pattern to possibly vibrate on player selection
	private long[] vibPattern = new long[] { 0L, 30L };
	
	// reference to controller of the game
	private Ctrl ctrl;
	// game map
	private Map map; 

	// drawing crayon
	PlayerCrayon crayon; 
	
	//player body is a circle
	private int bodyRadius = 30;
	private int touchRadius = 50;

	/**
	 * constructor
	 * @param pc
	 * @param ct
	 */
	public Player(PlayerCrayon pc, Ctrl ct) {
		crayon = pc;
		ctrl = ct;
		map = ctrl.getMap();
		

		//register as listener to motion events
		ctrl.addOnTouchDownListener(this);
		ctrl.addOnDragListener(this);
		ctrl.addOnFlingListener(this);
		
		//set self position
		Point2D startPoint = new Point2D();
		map.getPos(0.1F, startPoint);
		this.x = startPoint.getX();
		this.y = startPoint.getY();
		
		//set self moveSpeed
		this.moveSpeed = 10;
		this.movePath = new MovePath(this.x, this.y);
		
		Log.d(TAG,"player obtain coordinate: (" + x +"," + y + ")");
		

		crayon.setHost(this);
		crayon.init();
		ctrl.registerCrayon(crayon);
	}

	public Player(int posX, int posY,int moveSpeed, MovePath movePaht) {
		this.x = posX;
		this.x = posY;
		this.moveSpeed = moveSpeed;
		this.movePath = movePaht;
	}

	public void collideWith(Map m) {

	}

	public int getLocationX() {
		return x;
	}

	public int getLocationY() {
		return y;
	}


	public void cut(int simpleDirection){
		Log.d(TAG,"cut in directio:" + simpleDirection);
	}

	public void onTouchDown(int posX, int posY) {
		if(distanceBetweenTwoPoints(this.x,this.y,posX,posY) <= this.touchRadius ){
			this.selectionState = Player.STATE_SELECTED;
			
			this.ctrl.vibrate(vibPattern);
		}	
	}

	public void onDrag(int posX, int posY) {
		if(this.selectionState == Player.STATE_NOT_SELECTED)
			return;
		
		Log.d(TAG,"recieved drag");
		// TODO Auto-generated method stub
		
	}

	public void onFling(int simpleDirection) {
		if(this.selectionState == Player.STATE_NOT_SELECTED)
			return;
		
		//get map point - it should be strait point in my direction.
		//add this line to my path
		Log.d(TAG,"recieved fling");
		// TODO Auto-generated method stub
		
	}

	public void update() {
		
		this.movePath.chopFront(this.moveSpeed);
		this.x = this.movePath.getStartPoint().getX();
		this.y = this.movePath.getStartPoint().getY();
		// TODO Auto-generated method stub
		
	}

	public double distanceBetweenTwoPoints(int x0, int y0, int x1, int y1){
		
		int dx = x1 - x0;
		int dy = y1 - y0;
		
		return Math.sqrt(dx*dx + dy*dy);
	}

	public void onTouchUp() {
		this.selectionState = Player.STATE_NOT_SELECTED;
	}
}
