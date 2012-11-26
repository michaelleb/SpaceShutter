package com.andrapp.spaceshutter.controllers;

import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import com.andrapp.spaceshutter.model.Monster;
import com.andrapp.spaceshutter.model.Player;
import com.andrapp.spaceshutter.util.Crayon;
import com.andrapp.spaceshutter.util.LineSegment2D;
import com.andrapp.spaceshutter.util.Map;
import com.andrapp.spaceshutter.util.MapCrayon;
import com.andrapp.spaceshutter.util.MonsterCrayon;
import com.andrapp.spaceshutter.util.PlayerCrayon;
import com.andrapp.spaceshutter.util.SimpleDirection;

public class PlayView extends SurfaceView implements
		SurfaceHolder.Callback {

	public static final String TAG = PlayView.class.getSimpleName();
	public Ctrl controller =  new Ctrl(this);


	
	public MainThread gameLoopThread;


	/**
	 * Constructor 1
	 * @param context
	 */
	public PlayView(Context context) {
		super(context);

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the PlayView fovusable so it can handle events
		setFocusable(true);

		gameLoopThread = new MainThread(getHolder(), this);
	}

	public void update() {

		controller.update();
	}

	@Override
	public void onDraw(Canvas canvas) {
		controller.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		controller.onTouchEvent(event);

		return true;
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
			Log.d(TAG,"surface size:" + width + " x " +height);
	}

	public void surfaceCreated(SurfaceHolder holder) {

		this.controller.init();
		
		gameLoopThread.setRunning(true);
		gameLoopThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
	}


}
