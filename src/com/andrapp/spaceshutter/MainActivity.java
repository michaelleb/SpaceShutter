

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


import ourproject.messages.*;

import android.app.ProgressDialog;

/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity {


	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;

	// Member object for the chat services
	private BluetoothChatService mChatService = null;


	private MyView mView;

	private String mConnectedDeviceName = null;

	private boolean isJoiningGame=false;

	private ProgressDialog dialog=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// Set up the window layout
		setContentView(R.layout.main);


	}

	@Override
	public void onStart() {
		super.onStart();

		Button btn = (Button) findViewById(R.id.main_btn_multiplayer);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// Get local Bluetooth adapter
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

				// If BT is not on, request that it be enabled.
				// setupChat() will then be called during onActivityResult
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableIntent, BlueToothDefaults.REQUEST_ENABLE_BT);
					// Otherwise, setup the chat session
				} else {
					findBluetoothHost();		
				}

			}
		});

	}


	public void findBluetoothHost(){
		Intent serverIntent = new Intent(getBaseContext(), DeviceListActivity.class);
		startActivityForResult(serverIntent, BlueToothDefaults.REQUEST_CONNECT_DEVICE);					
	}


	@Override
	public synchronized void onResume() {
		super.onResume();
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mChatService != null)
			mChatService.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		//log.e("","onAc  tivityResult");

		switch (requestCode) {
		case BlueToothDefaults.REQUEST_CONNECT_DEVICE:

			//log.e("","REQUEST_CONNECT_DEVICE");

			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {

				// Get the device MAC address
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



				if(mChatService==null){

					mChatService = new BluetoothChatService(this, mHandler);


					if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
						// Start the Bluetooth chat services
						mChatService.start();


						BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

						mChatService.connect(device);
						Log.e("",""+address);
					}

				}

			}
			break;


		case BlueToothDefaults.REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				findBluetoothHost();
			}
			else{
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}


















	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case BlueToothDefaults.MESSAGE_READ:

				byte[] readBuf = (byte[]) msg.obj;

				InterMessage incomingMsg = MessageConvertion.bytesToMessage(readBuf);

				messageProcessor.processMessage(incomingMsg);

			case BlueToothDefaults.MESSAGE_WRITE:
				break;

			case BlueToothDefaults.MESSAGE_STATE_CHANGE:

				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					Log.i("","title_connected_to");
					break;
				case BluetoothChatService.STATE_CONNECTING:
					Log.i("","title_connecting");
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					Log.i("","title_not_connected");
					break;
				}
				break;

			case BlueToothDefaults.MESSAGE_TOAST:

				Toast.makeText(getApplicationContext(), msg.getData().getString(BlueToothDefaults.TOAST),
						Toast.LENGTH_SHORT).show();
				break;

			case BlueToothDefaults.MESSAGE_DEVICE_NAME:

				// save the connected device's name
				String mConnectedDeviceName = msg.getData().getString(BlueToothDefaults.DEVICE_NAME);

				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();


				View menu = (View) findViewById(R.id.menu);
				menu.setVisibility(View.GONE);


				startGame();


				break;	

			}

			foooo(msg);
		}
	};


















	/*
############################################################################################################################
############################################################################################################################
############################################################################################################################
############################################################################################################################
############################################################################################################################
############################################################################################################################
############################################################################################################################
	 */

	MyMessageProcessing messageProcessor = new MyMessageProcessing();

	public class MyMessageProcessing extends MessageProcessing{

		@Override
		public void process(StartCutMsg msg){
			otherObject.setLocation(msg.getLocation());
			otherObject.startCuting(msg.getOrientation(), otherPath, myPoly);
		}

		@Override
		public void process(ProcCutMsg msg){
			otherObject.setLocation(msg.getLocation());
			otherObject.proceedCutting(msg.getOrientation());
		}

		@Override
		public void process(BorderWalkMsg msg){
			otherObject.setLocation(msg.getLocation());
			otherObject.setBoundMovingPhase(msg.getUserPoint(),msg.getDirection(),myPoly);
		}

		@Override
		public void process(BoundsUpdateMsg msg){
			myPoly.setPoly(msg.getPoly());

			myObject.recalcBoundMovingPhase(myPoly);
			otherObject.recalcBoundMovingPhase(myPoly);
		}
	}






	/*
############################################################################################################################
############################################################################################################################
	 */

	//--------------------------------------------------

	private DummyObject myObject;
	private DummyObject otherObject;


	private PlayPath myPath;
	private PlayPath otherPath;

	private PlayPolygon myPoly;



	//--------------------------------------------------

	public int times=0;
	public long startTime;
	public int refreshEvery=Constants.ROUND_REFRESH;
	public int someoffset=0;

	//-------------------------------------------------

	private Point2D.Short prev=null;

	private boolean firsttime=false;


	//-------------------------------------------------


	private void startGame(){
		setGameScreen();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), refreshEvery);
	}


	private void setGameScreen(){

		LinearLayout root = (LinearLayout) findViewById(R.id.playwindow);

		mView = new MyView(this,null,Constants.PROJ_HEIGHT,Constants.PROJ_WIDTH,mHandler);

		root.setVisibility(View.VISIBLE);

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

				////log.e("",mm.widthPixels+"  "+mm.heightPixels);

				//Constants.VIEW_PROPORTION

				//params.height = mm.heightPixels-statusBarHeight-titleBarHeight;

				mView.setSizes(newHeight,newWidth);

			}

		});

		myObject=new DummyObject((short)Constants.MARGIN_PADDING,(short)Constants.MARGIN_PADDING,0);

		otherObject=new DummyObject(Constants.MARGIN_PADDING,Constants.MARGIN_PADDING,1);

		myPath=new PlayPath();
		otherPath=new PlayPath();

		myPoly=new PlayPolygon();


		myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);


	}

	void logicRound(){

		myObject.behave(myPoly);
		otherObject.behave(myPoly);

		if(myPath!=null && myPath.getSize()>1){


			if(!myObject.isCutting()){
				PlayPolygon sideA = new PlayPolygon();
				PlayPolygon sideB = new PlayPolygon();

				if(!isJoiningGame && myPoly.cut(myPath, sideA, sideB)==true){

					myPoly.setPoly(sideA);


					byte[] byteMsg = MessageConvertion.messageToBytes(new BoundsUpdateMsg(myPoly));
					mChatService.write(byteMsg);

					otherObject.recalcBoundMovingPhase(myPoly);
				}

				myPath=new PlayPath();
			}
		}


		if(otherPath!=null && otherPath.getSize()>1){

			if(!otherObject.isCutting()){
				PlayPolygon sideA = new PlayPolygon();
				PlayPolygon sideB = new PlayPolygon();

				if(!isJoiningGame && myPoly.cut(otherPath, sideA, sideB)==true){

					myPoly.setPoly(sideA);

					byte[] byteMsg = MessageConvertion.messageToBytes(new BoundsUpdateMsg(myPoly));
					mChatService.write(byteMsg);

					myObject.recalcBoundMovingPhase(myPoly);
				}

				otherPath=new PlayPath();
			}

		}

	}

	public void roundDrawing(){
		mView.drawObject(myPoly);

		if(otherPath!=null && otherPath.getSize()>1)
			mView.drawObject(otherPath);

		if(myPath!=null && myPath.getSize()>1)
			mView.drawObject(myPath);

		mView.drawObject(otherObject);

		mView.drawObject(myObject);

		mView.executeDrawing();


	}

	public void updateGame(){

		if(times==0) startTime=System.currentTimeMillis();

		times++;
		int supposedTimes = (int)((float)(System.currentTimeMillis()-startTime)/(float)Constants.ROUND_REFRESH);

		int offset = supposedTimes-times;

		if(offset>0)
			;//refreshEvery--;
		else
			;//refreshEvery=Constants.ROUND_REFRESH;

		//if(someoffset!=offset){
		//someoffset=offset;
		//Log.e("",""+someoffset);
		//}

		logicRound();

		roundDrawing();

		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), refreshEvery);
	}



	public void foooo(Message msg){

		Point2D.Short point;

		switch (msg.what) {
		case MotionEvent.ACTION_DOWN:

			firsttime=true;

			point = (Point2D.Short)msg.obj;
			prev=point;

			break;
		case MotionEvent.ACTION_MOVE:

			point = (Point2D.Short)msg.obj;

			if(firsttime){



				Vector2D.Short vec = new Line2D.Short(prev,point).getVector();

				if(Math.abs(vec.getVx())>Math.abs(vec.getVy()))
					vec.setVy((short)0);
				else
					vec.setVx((short)0);

				if(myObject.intersects(point) && vec.getLength()>0){

					byte[] byteMsg = MessageConvertion.messageToBytes(new StartCutMsg(myObject.getLocation(),vec));
					mChatService.write(byteMsg);
					myObject.startCuting(vec,myPath,myPoly);

					//Log.e(" other: "," ("+messg.getLocation().getx()+","+messg.getLocation().gety()+") "
					//		+", ["+messg.getOrientation().getVx()+","+messg.getOrientation().getVy()+"]");

					firsttime=false;
				}
				else if(myObject.isCutting() && vec.getLength()>0){

					//Log.e("",""+vec.getVx()+"-"+vec.getVy());

					byte[] byteMsg = MessageConvertion.messageToBytes(new ProcCutMsg(myObject.getLocation(),vec));
					mChatService.write(byteMsg);
					myObject.startCuting(vec,myPath,myPoly);

					//Log.e(" other: "," ("+messg.getLocation().getx()+","+messg.getLocation().gety()+") "
					//		+", ["+messg.getOrientation().getVx()+","+messg.getOrientation().getVy()+"]");

					myObject.proceedCutting(vec);



					firsttime=false;
				}



			}

			prev=point;

			break;
		case MotionEvent.ACTION_UP:

			point = (Point2D.Short)msg.obj;

			if(!myObject.isCutting()){

				byte[] byteMsg = MessageConvertion.messageToBytes(new BorderWalkMsg(myObject.getLocation(),true,point));
				mChatService.write(byteMsg);

				myObject.setBoundMovingPhase(point,true,myPoly);

				//mChatService.write((new TestMsg((short)2,myObject.getLocation(),new Vector2D.Short((short)0,(short)0),(short)1,point)).getBytes());
			}

			break;

		case BlueToothDefaults.MESSAGE_READ:

			byte[] readBuf = (byte[]) msg.obj;

			InterMessage incomingMsg = MessageConvertion.bytesToMessage(readBuf);

			messageProcessor.processMessage(incomingMsg);

		case BlueToothDefaults.MESSAGE_WRITE:
			break;
		case Constants.MESSAGE_LOGIC_ROUND:
			updateGame();
			break;
		}



	}
















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
			
			myPoly=new PlayPolygon();

			myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
			myPoly.proceed(Constants.MARGIN_PADDING, (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
			myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
			myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), Constants.MARGIN_PADDING);
			myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);

			byte[] byteMsg = MessageConvertion.messageToBytes(new BoundsUpdateMsg(myPoly));
			mChatService.write(byteMsg);

			myObject.recalcBoundMovingPhase(myPoly);
			otherObject.recalcBoundMovingPhase(myPoly);

			return true;
		case R.id.discoverable:
			return true;
		}
		return false;
	}


}