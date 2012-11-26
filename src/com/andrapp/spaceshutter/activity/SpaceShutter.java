package com.andrapp.spaceshutter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.andrapp.spaceshutter.controllers.PlayView;

public class SpaceShutter extends Activity {

	private static final String TAG = SpaceShutter.class.getSimpleName();

	private PlayView pv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		pv = new PlayView(this);
		setContentView(pv);
		Log.d(TAG,"View added");
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG,"Destroing...");
		super.onDestroy();
	}
	
	
	@Override
	protected void onStop() {
		Log.d(TAG,"Stopping...");
		super.onStop();
	}
	
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		pv.onTouchEvent(ev);
		//return super.dispatchTouchEvent(ev);
		return true;
	}
	

}
