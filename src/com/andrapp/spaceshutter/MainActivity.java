package com.andrapp.spaceshutter;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.andrapp.spaceshutter.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (BuildConfig.DEBUG) {
			Log.i(Constants.MAIN_ACTIVITY, "onCreate called");
		}
		
		setContentView(new Single(this, null));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.i(Constants.MAIN_ACTIVITY, "onCreateOptionsMenu called");
		}
		
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig); 

		if (BuildConfig.DEBUG) {
			Log.e(Constants.MAIN_ACTIVITY, "onConfigurationChanged called");
		}
		
		System.out.println("onConfigurationChanged called");
	}

}
