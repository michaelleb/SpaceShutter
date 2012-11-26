package com.andrapp.spaceshutter.controllers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.andrapp.spaceshutter.model.Monster;
import com.andrapp.spaceshutter.model.Player;
import com.andrapp.spaceshutter.util.Crayon;
import com.andrapp.spaceshutter.util.DragListener;
import com.andrapp.spaceshutter.util.EventDispatcher;
import com.andrapp.spaceshutter.util.FlingListener;
import com.andrapp.spaceshutter.util.Map;
import com.andrapp.spaceshutter.util.MapCrayon;
import com.andrapp.spaceshutter.util.MonsterCrayon;
import com.andrapp.spaceshutter.util.PlayerCrayon;
import com.andrapp.spaceshutter.util.Point2D;
import com.andrapp.spaceshutter.util.SimpleDirection;
import com.andrapp.spaceshutter.util.TouchDownListener;
import com.andrapp.spaceshutter.util.TouchUpListener;
import com.andrapp.spaceshutter.util.Updatable;

public class Ctrl implements EventDispatcher, Updatable {
	public static final String TAG = Ctrl.class.getSimpleName();
	public static final int VIBRATOR_NO_REPEATE = -1;

	private Vibrator vibrator;
	
	// vibration active option
	private boolean vibrationEnabled = true; 


	private int direction = SimpleDirection.DIRECTION_UP;

	private Player hostPlayer;
	private Player remotePlayer;
	private Monster monster;
	private Map map;
	private VelocityTracker vTracker = null;

	// list of objects that want to be drawn.
	private LinkedList<Crayon> crayons = new LinkedList<Crayon>();

	// the view that hold this controller
	private PlayView playVew;

	// Listeners
	private LinkedList<TouchDownListener> touchDownListeners = new LinkedList<TouchDownListener>();
	private LinkedList<FlingListener> onFlingListeners = new LinkedList<FlingListener>();
	private LinkedList<DragListener> onDragListeners = new LinkedList<DragListener>();
	private LinkedList<TouchUpListener> touchUpListeners = new LinkedList<TouchUpListener>();
	
	//scaling parameters
	protected static int xOffset = 50;	//offset of leftUp corner of the map.
	protected static int yOffset = 50;
	protected static float scaling = 1;	//size scaling on screen
	protected Point2D logicPoint	 = new Point2D();
	
	
	//touch detection 
	private final static int VELOCITY_FLING_THRESHOLD = 2000;
	private final static int FLING_TIME_THRESHOLD = 200;
	private final static int STATE_COMPUTE_FLING = 1;
	private final static int STATE_COMPUTE_DRAG = 2;
	private long timer;
	private long duration;
	private long eventStartTime;
	private int numOfTouchEvents;
	private int touchIdentificationState;
	private long[] velosityTrack = new long[200];

	
	public Ctrl(PlayView playView) {
		this.playVew = playView;

	}

	public void init() {
		this.vibrator = (Vibrator) playVew.getContext().getSystemService(
				Context.VIBRATOR_SERVICE);

		this.map = new Map(new MapCrayon(playVew.getResources()), this);
		Log.d(TAG, "map created");

		this.hostPlayer = new Player(new PlayerCrayon(playVew.getResources()),
				this);
		Log.d(TAG, "Player created");

		this.monster = new Monster(new MonsterCrayon(playVew.getResources()),
				this);
		Log.d(TAG, "monster created");

	}

	public void vibrate(long[] pattern, int repeat) {
		if (this.vibrationEnabled)
			vibrator.vibrate(pattern, repeat);

	}

	public void vibrate(long[] pattern) {
		vibrate(pattern, Ctrl.VIBRATOR_NO_REPEATE);
	}

	/**
	 * set false to disable vibration
	 * 
	 * @param enable
	 */
	public void setVibrationOption(boolean enable) {
		this.vibrationEnabled = enable;
	}

	/**
	 * when map is Clipped/expanded ctrl will notify players of this event
	 */
	public void onMapChange() {

	}

	/**
	 * when player/Monster change direction Ctrl will notify other device about
	 * this fact
	 */
	public void onPlayerChangeDirection() {
	}

	/**
	 * 
	 */
	public void onCutSubmit() {
	} // player inform ctrl when he finished cut path so that ctrl will submit
		// this to map



	/**
	 * @param crayon
	 */
	public void registerCrayon(Crayon crayon) {
		crayons.add(crayon);
	}

	/**
	 * @return
	 */
	public Map getMap() {
		return this.map;
	}

	public void onDraw(Canvas canvas) {

		Iterator<Crayon> it = crayons.iterator();

		while (it.hasNext()) {
			it.next().draw(canvas);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		float yVelocity;
		float xVelocity;
		
		getLogicPoint(event.getX(),event.getY());
		
		int touchPosX = this.logicPoint.getX();
		int touchPosY = this.logicPoint.getY();

		switch (action) {

		case MotionEvent.ACTION_DOWN:
			
			touchIdentificationState = Ctrl.STATE_COMPUTE_FLING;
			numOfTouchEvents = 0;
			eventStartTime = System.currentTimeMillis();
			if (vTracker == null) {
				vTracker = VelocityTracker.obtain();
			} else {
				vTracker.clear();
			}
			vTracker.addMovement(event);
			//dispatch touchDown
			this.dispatchTouchDown(touchPosX, touchPosY);


			
			//shot down the game if user touch the bottom of the screen ( test purpus)
			if (event.getY() > playVew.getHeight() - 200) {
				playVew.gameLoopThread.setRunning(false);
				((Activity) playVew.getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());

			}

			break;
		case MotionEvent.ACTION_MOVE:
			
			vTracker.addMovement(event);
			if(touchIdentificationState == Ctrl.STATE_COMPUTE_DRAG){
				//dispatch drag
				this.dispatchDrag(touchPosX, touchPosY);
			}
			else{
				this.duration =  System.currentTimeMillis() - this.eventStartTime;
				if(this.duration > Ctrl.FLING_TIME_THRESHOLD ){
					touchIdentificationState = Ctrl.STATE_COMPUTE_DRAG;
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			
			if(touchIdentificationState == Ctrl.STATE_COMPUTE_FLING){
				//compute fling 
				Log.d(TAG,"num of events: " + numOfTouchEvents);
				vTracker.computeCurrentVelocity(1000);
				xVelocity = Math.abs(vTracker.getXVelocity());
				yVelocity = Math.abs(vTracker.getYVelocity());
				if (xVelocity >= Ctrl.VELOCITY_FLING_THRESHOLD
						&& xVelocity > yVelocity) {
					// direction left or right
					if (vTracker.getXVelocity() > 0) {
						direction = SimpleDirection.DIRECTION_RIGHT;
					} else {
						direction = SimpleDirection.DIRECTION_LEFT;
					}
					this.dispatchFling(direction);
					Log.d(TAG,"fling detected");
				} else if (yVelocity > Ctrl.VELOCITY_FLING_THRESHOLD) {
					// direction up or down
					if (vTracker.getYVelocity() > 0) {
						direction = SimpleDirection.DIRECTION_DOWN;
					} else {
						direction = SimpleDirection.DIRECTION_UP;
					}
					this.dispatchFling(direction);
				}
			}

			vTracker.recycle();
			break;
		}
		
		numOfTouchEvents++;

		return true;
	}
	
	private void getLogicPoint(float x, float y) {
		
		this.logicPoint.setX( Math.round((x - xOffset)/scaling) );
		this.logicPoint.setY(Math.round((y - yOffset)/scaling));
		
	}

	public void update() {
		this.hostPlayer.update();
		this.monster.update();
	}

	public void dispatchTouchDown(int posX, int posY) {
		// currently only the player will listen here so i get him fast
		this.touchDownListeners.getFirst().onTouchDown(posX, posY);

	}

	public void dispatchFling(int simpleDirection) {
		// currently only the player will listen here so i get him fast
		this.onFlingListeners.getFirst().onFling(simpleDirection);

	}

	public void dispatchDrag(int posX, int posY) {
		// currently only the player will listen here so i get him fast
		this.onDragListeners.getFirst().onDrag(posX, posY);

	}

	public void addOnTouchDownListener(TouchDownListener listener) {
		this.touchDownListeners.add(listener);

	}

	public void addOnFlingListener(FlingListener listener) {
		this.onFlingListeners.add(listener);

	}

	public void addOnDragListener(DragListener listener) {
		this.onDragListeners.add(listener);

	}

	public void removeOnTouchDownListener(TouchDownListener listener) {
		this.touchDownListeners.remove(listener);

	}

	public void removeOnFlingListener(FlingListener listener) {
		this.onFlingListeners.remove(listener);

	}

	public void removeOnDragListener(DragListener listener) {
		this.onDragListeners.remove(listener);

	}


	public void dispatchTouchUP() {
		// currently only the player will listen here so i get him fast
		this.touchUpListeners.getFirst().onTouchUp();
		
	}

	public void addOnTouchUpListener(TouchUpListener listener) {
		this.touchUpListeners.add(listener);
		
	}

	public void removeOnTouchUpListener(TouchDownListener listener) {
		this.touchUpListeners .remove(listener);
		
	}

}
