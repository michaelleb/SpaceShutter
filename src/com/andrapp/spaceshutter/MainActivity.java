


package com.andrapp.spaceshutter;

import ourproject.playables.*;


import com.andrapp.spaceshutter.BlueToothDefaults;
import com.andrapp.spaceshutter.Constants;
import com.andrapp.spaceshutter.MyView;
import com.andrapp.spaceshutter.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.view.*;

import android.util.DisplayMetrics;

import android.graphics.Rect;
import android.view.MotionEvent;

import mark.geometry.*;


/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity {

	// Name of the other connected device
	private String mConnectedDeviceName = null;


	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;

	// Member object for the chat services
	private BluetoothService mBluetoothService = null;


	private MyView mView;


	private DummyObject myObject;


	private PlayPath myPath;

	private PlayPolygon myPoly;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("", "onCreate");

		// Set up the window layout
		setContentView(R.layout.main);

		mBluetoothService = new BluetoothService(this, mHandler);


		myObject=new DummyObject(50,50);

		myPath=new PlayPath();
		myPoly=new PlayPolygon();


		myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING, Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);

		mHandler.sendEmptyMessage(Constants.MESSAGE_LOGIC_ROUND);
	}


	public void updateGame(){


		myObject.behave();

		mView.drawObject(myObject);

		mView.drawObject(myPath);

		mView.drawObject(myPoly);

		mView.execureDrawing();

		mHandler.removeMessages(Constants.MESSAGE_LOGIC_ROUND);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), Constants.ROUND_REFRESH);
	}


	@Override
	public void onStart() {
		super.onStart();

		setBT();
		setGameScreen();
	}

	private void setGameScreen(){

		LinearLayout root = (LinearLayout) findViewById(R.id.mainroot);

		mView = new MyView(this,null,Constants.PROJ_HEIGHT,Constants.PROJ_WIDTH,mHandler);

		root.addView(mView);

		mView.post(new Runnable() { 
			public void run() { 
				Rect rect = new Rect(); 
				Window win = getWindow();  // Get the Window
				win.getDecorView().getWindowVisibleDisplayFrame(rect); 

				int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop(); 

				int statusBarHeight = rect.top; 
				int titleBarHeight = contentViewTop - statusBarHeight;

				Display display = getWindowManager().getDefaultDisplay();

				DisplayMetrics mm=new DisplayMetrics();

				display.getMetrics(mm);


				int newWidth=mm.widthPixels;

				int newHeight=(int)(new Float(newWidth)*(new Float(Constants.PROJ_HEIGHT)/new Float(Constants.PROJ_WIDTH)));

				Log.e("",mm.widthPixels+"  "+mm.heightPixels);

				//Constants.VIEW_PROPORTION

				//params.height = mm.heightPixels-statusBarHeight-titleBarHeight;

				mView.setSizes(newHeight,newWidth);

			}

		});


	}

	private void setBT(){
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}



		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, BlueToothDefaults.REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		Log.e("", "onResume");



		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mBluetoothService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mBluetoothService.start();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		Log.e("", "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e("", "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.e("", "onDestroy");

		// Stop the Bluetooth chat services
		if (mBluetoothService != null)
			mBluetoothService.stop();

	}




	/*
==========================================================================================================
    BLUETOOTH INCOMING EVENTS HANDLER
==========================================================================================================
	 */

	private Point2D prev=null;

	private boolean firsttime=false;

	private final MyHandler mHandler = new MyHandler();

	public class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {

			//Log.e("","main: handleMessage "+msg.what);

			Point2D point;

			switch (msg.what) {

			case MotionEvent.ACTION_DOWN:

				firsttime=true;

				/*
				myPath.clear();

				point = (Point2D)msg.obj;

				if(myObject.intersects(point)){

					//Log.e("",""+point.getx()+"-"+point.gety());

					myPath.start(myObject.getCenterX(),myObject.getCenterY());

					//Log.e("",""+point.getx()+"-"+point.gety());


				}
				 */
				point = (Point2D)msg.obj;
				prev=point;

				break;
			case MotionEvent.ACTION_MOVE:



				/*
		Vector2D speed = myObject.getOrientation();

		Point2D loc = myObject.getLocation();

		Point2D dest = myPath.getPoint(pathDestIndex);


		Point2D loc2 = new Point2D(loc);

		loc2.add(speed);



				 */

				point = (Point2D)msg.obj;





				//Log.e("",""+point.getx()+"-"+point.gety());

				//if( prev.distance(point)>10){
				//	myPath.proceed(point.getx(), point.gety());

				//Log.e("",""+point.getx()+"-"+point.gety());



				if(firsttime){

					Vector2D vec = new Line2D(prev,point).getVectorWidthLen(1);

					if(vec.getLength()>0){

						Log.e("",""+vec.getVx()+"-"+vec.getVy());

						myObject.setOrientation(vec);
						firsttime=false;
					}
				}

				prev=point;

				//}

				break;
			case MotionEvent.ACTION_UP:

				/*
				point = (Point2D)msg.obj;
				//Log.e("",""+point.getx()+"-"+point.gety());

				myPath.proceed(point.getx(), point.gety());


				PlayPolygon h1=new PlayPolygon();
				PlayPolygon h2=new PlayPolygon();

				if(myPath.getSize()>0 && myPoly.cut(myPath, h1, h2)){

					if(h1.getArea()>h2.getArea())
						myPoly=h1;
					else
						myPoly=h2;
				}

				 */

				break;
			case Constants.MESSAGE_LOGIC_ROUND:
				updateGame();
				break;

			case BlueToothDefaults.MESSAGE_STATE_CHANGE:

				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					Log.i("","title_connected_to");
					break;
				case BluetoothService.STATE_CONNECTING:
					Log.i("","title_connecting");
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					Log.i("","title_not_connected");
					break;
				}
				break;

			case BlueToothDefaults.MESSAGE_DEVICE_NAME:

				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(BlueToothDefaults.DEVICE_NAME);

				Log.e("", "Connected to " + mConnectedDeviceName);

				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();


				//((MyView) findViewById(R.id.mysingle)).setBTService(mBluetoothService);

				break;
			case BlueToothDefaults.MESSAGE_TOAST:

				Log.e("", msg.getData().getString(BlueToothDefaults.TOAST));

				Toast.makeText(getApplicationContext(), msg.getData().getString(BlueToothDefaults.TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}



	};


	/*
==========================================================================================================
    ACTIVITY EVENT HANDLER
==========================================================================================================
	 */

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("","onActivityResult");

		switch (requestCode) {
		case BlueToothDefaults.REQUEST_CONNECT_DEVICE:

			Log.e("","REQUEST_CONNECT_DEVICE");

			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras()
						.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				// Attempt to connect to the device
				mBluetoothService.connect(device);
				//setBTService



			}
			break;
		case BlueToothDefaults.REQUEST_ENABLE_BT:

			Log.e("","REQUEST_ENABLE_BT");


			if (resultCode != Activity.RESULT_OK) {
				// User did not enable Bluetooth or an error occured

				Log.e("", "BT not enabled");

				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	/*
==========================================================================================================
OPTIONS MENU BUTTONS FOR BLUETOOTH OPERATIONS
==========================================================================================================
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, BlueToothDefaults.REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	private void ensureDiscoverable() {
		Log.e("", "ensureDiscoverable");
		if (mBluetoothAdapter.getScanMode() !=
				BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

}