
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
import mark.geometry.Vector2D.Short;


import ourproject.messages.*;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.Random;


import android.content.DialogInterface;

import android.app.AlertDialog;

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

	private AlertDialog dialog=null;

	private boolean isSinglePlayer=true;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("","++++ ONCREATE ++++");

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// Set up the window layout
		setContentView(R.layout.main);
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.e("","++++ ONSTART ++++");

		Button btn2 = (Button) findViewById(R.id.main_btn_singleplayer);
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				isSinglePlayer=true;

				View menu = (View) findViewById(R.id.menu);
				menu.setVisibility(View.GONE);



				startGame();

			}
		});

		Button btn = (Button) findViewById(R.id.main_btn_multiplayer);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				startBTAndFindHosts();
			}
		});

























	}

	public void startBTAndFindHosts(){

		if(mBluetoothAdapter==null)
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, BlueToothDefaults.REQUEST_ENABLE_BT);
		} else {
			startBTService();

			findBluetoothHost();
		}
	}

	public void startBTService(){
		if (mChatService == null)
			mChatService = new BluetoothChatService(this, mHandler);

		if (mChatService.getState() == BluetoothChatService.STATE_NONE)
			mChatService.start();
	}


	public void findBluetoothHost(){
		Intent serverIntent = new Intent(getBaseContext(), DeviceListActivity.class);
		startActivityForResult(serverIntent, BlueToothDefaults.REQUEST_CONNECT_DEVICE);
	}


	@Override
	public synchronized void onResume() {
		super.onResume();
		Log.e("","++++ ONRESUME ++++");

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

				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
				
				waitForInviteChoice();
				
				mChatService.connect(device);

				isJoiningGame=true;
				
			}
			break;


		case BlueToothDefaults.REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {

				startBTService();

				findBluetoothHost();

			}
			else{
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		Log.e("","++++ ONPAUSE ++++");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mChatService != null) mChatService.stop();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case BlueToothDefaults.MESSAGE_READ:
				
				byte[] reply = (byte[])msg.obj;
				
				if(dialog.isShowing() && reply[0]==Constants.BLUETOOTH_CONTROL_START_GAME){
					dialog.dismiss();
					
					showGameScreen();
				}
				
				break;

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

			case BlueToothDefaults.MESSAGE_CONN_FAILED:

				isJoiningGame=false;
				
				if(dialog!=null && dialog.isShowing()) dialog.dismiss();

				Toast.makeText(getApplicationContext(), msg.getData().getString(BlueToothDefaults.TOAST),
						Toast.LENGTH_SHORT).show();
				break;

			case BlueToothDefaults.MESSAGE_CONN_LOST:

				isJoiningGame=false;
				
				if(dialog!=null && dialog.isShowing()) dialog.dismiss();
				
				Toast.makeText(getApplicationContext(), msg.getData().getString(BlueToothDefaults.TOAST),
						Toast.LENGTH_SHORT).show();
				break;

			case BlueToothDefaults.MESSAGE_DEVICE_NAME:

				finishActivity(BlueToothDefaults.REQUEST_CONNECT_DEVICE);

				// save the connected device's name
				String mConnectedDeviceName = msg.getData().getString(BlueToothDefaults.DEVICE_NAME);

				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();


				if(!isJoiningGame){
					promptGameInvitation(mConnectedDeviceName);
				}

				break;	

			}

			notifyGame(msg);
		}
	};


	public void promptGameInvitation(String mConnectedDeviceName){


		finishActivity(BlueToothDefaults.REQUEST_CONNECT_DEVICE);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					
					byte[] messg = new byte[1];
					messg[0]=Constants.BLUETOOTH_CONTROL_START_GAME;
					
					mChatService.write(messg);
					
					showGameScreen();
					
					break;

				case DialogInterface.BUTTON_NEGATIVE:

					mChatService.stop();

					mChatService.start();

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		dialog = builder.setMessage(mConnectedDeviceName+" wants to invite you to the game, would you like to start?").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).create();
		
		dialog.show();
	}
	
	
	
	
	
	public void waitForInviteChoice(){


		finishActivity(BlueToothDefaults.REQUEST_CONNECT_DEVICE);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				
				case DialogInterface.BUTTON_NEGATIVE:

					mChatService.stop();

					mChatService.start();

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		dialog = builder.setMessage("Waiting for response..").setNegativeButton("Cancel", dialogClickListener).create();
		
		dialog.show();
	}
	
	public void showGameScreen(){
		View menu = (View) findViewById(R.id.menu);
		menu.setVisibility(View.GONE);


		isSinglePlayer=false;

		startGame();

	}

	
	
	
	

	/*
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###################################################################################################################################################
	 */

	MyEnvironment env=null;

	//--------------------------------------------------

	public int roundsSinceStart=0;						//how much times was logic round made since start
	public long startTime;								//time of game start
	public int refreshRate=Constants.INIT_ROUND_REFRESH;	//logic round refresh time

	//-------------------------------------------------

	private Point2D.Short prev=null;

	private boolean firsttime=false;


	//-------------------------------------------------

	private short myBorderMsgCount=0;		//count of "BoundsUpdateMessage" messages sent to other player
	private short otherBorderMsgCount=0;	//count of "BoundsUpdateMessage" messages sent to me

	//-------------------------------------------------

	ArrayList<InterMessage> messageQueue = new ArrayList<InterMessage>();	//queue for outgoing messages

	MyMessageProcessing messageProc = new MyMessageProcessing();	//game custom message processor

	MessageConvertion messConv = new MessageConvertion();

	//-------------------------------------------------

	private void startGame(){

		setGameScreen();	//create game view

		initVars();	//init game variables

		//start doing rounds
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), refreshRate);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_DRAW_ROUND), Constants.DRAW_REFRESH);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_SEND_BT_MESSAGE_ROUND), 0);
	}

	public void initVars(){


		env = new MyEnvironment();
	}

	private void setGameScreen(){

		LinearLayout root = (LinearLayout) findViewById(R.id.playwindow);

		mView = new MyView(this,null,Constants.PROJ_HEIGHT,Constants.PROJ_WIDTH,mHandler);

		root.setVisibility(View.VISIBLE);

		root.addView(mView);

		//set view width/height when ready
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

	}


	/*
	 * border polygon cutting with path logic
	 */
	public void TryCutBorder(PlayPath path){

		if(path!=null && path.getSize()>1){

			PlayPolygon sideA = new PlayPolygon();
			PlayPolygon sideB = new PlayPolygon();

			if(env.myPoly.cut(path, sideA, sideB)==true){

				env.myPoly.setPoly(sideA);

				myBorderMsgCount++;

				if(!isSinglePlayer) messageQueue.add(new BoundsUpdateMsg(env.myPoly,myBorderMsgCount));

				if(!isSinglePlayer) updateMonsterToOtherPlayer();

				env.myObject.recalcBoundMovingPhase(env.myPoly);
				env.otherObject.recalcBoundMovingPhase(env.myPoly);

			}
		}

	}

	public void updateMonsterToOtherPlayer(){
		MonsterUpdateMsg msg = new MonsterUpdateMsg();

		for(int i=0;i<env.numOfMonsters;i++){

			msg.positions.add(env.enemy[i].getLocation());
			msg.orientations.add(env.enemy[i].getOrientation());

		}

		messageQueue.add(msg);
	}

	Vector2D.Short previous=null;

	void doLogic(){

		//------------------------------

		env.myObject.behave(env);

		//if my object in cutting mode but collided with boundary
		if(env.myObject.isCutting() && env.myPoly.getClosestPointIndex(env.myObject.getLocation())>=0){

			env.myObject.stopCutting();

			env.chasingPath.clear();

			previous=null;

			if(!isJoiningGame) TryCutBorder(env.myPath);

			if(!isSinglePlayer) messageQueue.add(new StopCutMsg(env.myObject.getLocation()));	//stop cut msg comes after potential update border msg !!!

			if(env.myPath!=null) env.myPath.clear();
		}

		//------------------------------

		env.otherObject.behave(env);

		if(!env.otherObject.isCutting()){

			if(!isJoiningGame) TryCutBorder(env.otherPath);

			if(env.otherPath!=null) env.otherPath.clear();
		}

		//------------------------------
		for(int i=0;i<env.numOfMonsters;i++)
			env.enemy[i].behave(env);

		if(env.chasingPath.getSize()>0){


			Point2D.Short last = env.chasingPath.getPoint(env.chasingPath.getSize()-1);

			if(previous==null || previous.getLength()==0){

				Point2D.Short nextDest = env.myPath.getNextPoint(last,env.myPath.getPoint(env.myPath.getSize()-1));

				Vector2D.Short vec = nextDest.sub(last);

				previous=vec;

				env.chasingPath.proceed(last.getx(), last.gety());
			}

			Vector2D.Short addition = new Vector2D.Short(previous);

			if(addition.getLength()>=2){
				addition.setLength(2);
			}

			last.add(addition);

			previous.setVx((short)(previous.getVx()-addition.getVx()));
			previous.setVy((short)(previous.getVy()-addition.getVy()));

			env.chasingPath.setPoint(env.chasingPath.getSize()-1, last);
		}

	}

	/*
	 * in game processor
	 */
	public class MyMessageProcessing extends MessageProcessing{

		@Override
		public void process(StartCutMsg msg){
			env.otherObject.setLocation(msg.getLocation());
			env.otherObject.startCuting(msg.getOrientation(), env.otherPath, env.myPoly);
		}

		@Override
		public void process(ProcCutMsg msg){
			env.otherObject.setLocation(msg.getLocation());
			env.otherObject.proceedCutting(msg.getOrientation());
		}

		@Override
		public void process(BorderWalkMsg msg){
			env.otherObject.setLocation(msg.getLocation());
			env.otherObject.setBoundMovingPhase(msg.getUserPoint(),msg.getDirection(),env.myPoly);
		}

		@Override
		public void process(BoundsUpdateMsg msg){

			if(msg.getNum()>otherBorderMsgCount){
				env.myPoly.setPoly(msg.getPoly());
				otherBorderMsgCount=msg.getNum();
			}

			env.myObject.recalcBoundMovingPhase(env.myPoly);	//recalc routes after poly change if in border walking phase
			env.otherObject.recalcBoundMovingPhase(env.myPoly);
		}

		@Override
		public void process(StopCutMsg msg){

			env.otherObject.setLocation(msg.getLocation());
			env.otherObject.stopCutting();
		}

		@Override
		public void process(MonsterUpdateMsg msg){

			for(int i=0;i<env.numOfMonsters;i++){

				env.enemy[i].setLocation(msg.positions.get(i));

				env.enemy[i].setOrientation(msg.orientations.get(i));


			}
		}

	}

	public void doDrawings(){
		mView.drawObject(env.myPoly);

		if(env.otherPath!=null && env.otherPath.getSize()>1)
			mView.drawObject(env.otherPath);

		if(env.myPath!=null && env.myPath.getSize()>1)
			mView.drawObject(env.myPath);

		if(env.chasingPath!=null && env.chasingPath.getSize()>1)
			mView.drawObject(env.chasingPath);

		if(!isSinglePlayer)
			mView.drawObject(env.otherObject);

		mView.drawObject(env.myObject);

		for(int i=0;i<env.numOfMonsters;i++)
			mView.drawObject(env.enemy[i]);

		mView.executeDrawing();
	}

	public void updateGame(){

		updateRefreshRate();

		doLogic();

		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), refreshRate);
	}

	public void drawGame(){
		doDrawings();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_DRAW_ROUND), Constants.DRAW_REFRESH);
	}

	public void updateRefreshRate(){
		if(roundsSinceStart==0) startTime=System.currentTimeMillis();

		roundsSinceStart++;
		int supposedTimes = (int)((float)(System.currentTimeMillis()-startTime)/(float)Constants.INIT_ROUND_REFRESH);

		int offset = supposedTimes-roundsSinceStart;

		if(offset>0)
			refreshRate--;
		else
			refreshRate=Constants.INIT_ROUND_REFRESH;

	}

	public void notifyGame(Message msg){

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

				if(env.myObject.intersects(point) && vec.getLength()>0){

					Point2D.Short pos = env.myObject.getLocation();

					pos.add(vec);

					if(env.myPoly.getClosestPointIndex(pos)==-1){
						env.myObject.startCuting(vec,env.myPath,env.myPoly);

						if(!isSinglePlayer)
							messageQueue.add(new StartCutMsg(env.myObject.getLocation(),vec));

					}

					firsttime=false;

				}
				else if(env.myObject.isCutting() && vec.getLength()>0){

					if(!isSinglePlayer)
						messageQueue.add(new ProcCutMsg(env.myObject.getLocation(),vec));

					env.myObject.proceedCutting(vec);

					firsttime=false;
				}

			}

			prev=point;

			break;
		case MotionEvent.ACTION_UP:

			point = (Point2D.Short)msg.obj;

			if(!env.myObject.isCutting()){

				if(!isSinglePlayer)
					messageQueue.add(new BorderWalkMsg(env.myObject.getLocation(),true,point));

				env.myObject.setBoundMovingPhase(point,true,env.myPoly);
			}

			break;

		case Constants.MESSAGE_LOGIC_ROUND:
			updateGame();
			break;

		case Constants.MESSAGE_DRAW_ROUND:
			drawGame();

			break;

		case Constants.MESSAGE_SEND_BT_MESSAGE_ROUND:

			if(messageQueue.size()>0){

				byte[] bytes = messConv.messagesToBytes(messageQueue);

				mChatService.write(bytes);

				messageQueue.clear();
			}

			mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_SEND_BT_MESSAGE_ROUND), Constants.SEND_BT_MESSG_REFRESH);

			break;

		case BlueToothDefaults.MESSAGE_READ:

			byte[] readBuf = (byte[]) msg.obj;

			InterMessage[] incomingMsg = messConv.bytesToMessages(readBuf);

			for(int i=0;i<incomingMsg.length;i++){
				messageProc.processMessage(incomingMsg[i]);
			}

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

			env.myPoly=new PlayPolygon();

			env.myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
			env.myPoly.proceed(Constants.MARGIN_PADDING, (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
			env.myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
			env.myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), Constants.MARGIN_PADDING);
			env.myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);



			if(!isSinglePlayer){

				myBorderMsgCount++;
				messageQueue.add(new BoundsUpdateMsg(env.myPoly,myBorderMsgCount));
			}

			env.myObject.recalcBoundMovingPhase(env.myPoly);
			env.otherObject.recalcBoundMovingPhase(env.myPoly);

			return true;
		case R.id.discoverable:
			return true;
		}
		return false;
	}


}