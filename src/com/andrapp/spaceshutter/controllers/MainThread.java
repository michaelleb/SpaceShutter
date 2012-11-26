package com.andrapp.spaceshutter.controllers;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	private final static  String TAG = MainThread.class.getSimpleName();

	// desired fps
	private final static int MAX_FPS = 40;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int DESIRED_FRAME_DURATION = 1000 / MAX_FPS;

	private boolean running; // flag to hold game state
	private SurfaceHolder surfaceHolder;
	private PlayView playView;

	// C-tor
	public MainThread(SurfaceHolder sh, PlayView pv) {
		super();
		this.surfaceHolder = sh;
		this.playView = pv;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		Log.d(TAG, "Starting game loop");
		Canvas canvas = null;

		long frameBeginTime;//hold time when current frame starts execution
		long frameDuration;	//hold current frame duration in ms
		int sleepTime;		//hold num of ms to sleep before next frame
		int framesSkipped;	//count frames that executed without drawing (only logic update)
		sleepTime = 0;		//hold ms to sleep in current loop round
		
//		FpsStat stat;		//for measuring real fps 
//		stat = new FpsStat(10);
		
		while (running) {
			canvas = null;

			frameBeginTime = System.currentTimeMillis();

			// update game state
			this.playView.update();

			// try locking the canvas for exclusive pixel editing on the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {

					framesSkipped = 0; // resetting the frames skipped

					// render state to the screen, draws the canvas
					this.playView.onDraw(canvas);
					frameDuration = System.currentTimeMillis() - frameBeginTime;
					// calculate sleep time
					sleepTime = (int) (DESIRED_FRAME_DURATION - frameDuration);
					if (sleepTime > 0) {
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
						}
					}

					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						// we need to catch up (skip frame rendering, do only
						// logic)
						this.playView.update(); // update without rendering

						// add frame period to check if in next frame we have
						// enough time
						sleepTime += DESIRED_FRAME_DURATION;

						framesSkipped++;
					}
					if (framesSkipped > 0) {
						Log.d(TAG, "Skipped:" + framesSkipped);
					}
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);

					// measure FPS
//					stat.addFrame();

				}
			}

		}
	}

	// Fps statistics class for measuring real number of frames per second done
	// by the game loop. to measure a loop you should create instance of this
	// class right before entering the loop and call add frame write before the
	// end of the loop, e.g after game logic and drawing accomplished for this
	// loop. the constructor of this class take integer that represent number of
	// seconds to measure before printing. after elapsed measure period, the
	// object print all the fps for last period to system standard output 
	static class FpsStat {
		private final static String TAG = FpsStat.class.getSimpleName();
		private final int SECOND_DURATION = 1000;
		private final int measurePeriod;		// num of seconds in one period before print result
		
		private long lastMeasureTime = 0L;
		private long curentSecondTime = 0L;
		private int numOfFramesInCurentSecond = 0;
		private int[] statArray;
		private int timesWeAddedStatToArray = 0;
		private long curentSysTime;
		private long frameDuration;

		public FpsStat(int mesurePeriod){
			this.measurePeriod = mesurePeriod;
			statArray = new int[mesurePeriod];
			lastMeasureTime = System.currentTimeMillis();
		}
		public void addFrame() {

			//take new time and calculate frame duration time
			curentSysTime = System.currentTimeMillis();
			frameDuration = curentSysTime - lastMeasureTime;
			lastMeasureTime = curentSysTime;

			//add frame and duration 
			numOfFramesInCurentSecond++;
			curentSecondTime += frameDuration;

			if (curentSecondTime >= SECOND_DURATION) {

				curentSecondTime = curentSecondTime - SECOND_DURATION;
				statArray[(timesWeAddedStatToArray % measurePeriod)] = numOfFramesInCurentSecond;
				numOfFramesInCurentSecond = 0;

				timesWeAddedStatToArray++;
				if ((timesWeAddedStatToArray % measurePeriod) == 0) {
					printFrameRate();
				}
			}
		}

		// print last FPS statistics to standard output
		public void printFrameRate() {

			for (int i = 0; i < measurePeriod; i++) {

				System.out.printf("[%d] ", statArray[i]);
			}
			System.out.println();
		}
	}
}
