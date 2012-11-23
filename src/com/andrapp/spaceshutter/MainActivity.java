
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


				isSinglePlayer=false;

				startGame();

				break;	

			}

			notifyGame(msg);
		}
	};


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

	//--------------------------------------------------

	private Player myObject;		//my player
	private Player otherObject;	//other player
	
	
	private Monster[] enemy;	//other player
	private int numOfMonsters=3;

	private PlayPath myPath;			//path of my player
	private PlayPath otherPath;			//path of other player

	private PlayPolygon myPoly;			//boundaries polygon

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

	ArrayList<byte[]> messageQueue = new ArrayList<byte[]>();	//queue for outgoing messages

	MyMessageProcessing messageProc = new MyMessageProcessing();	//game custom message processor

	//-------------------------------------------------

	private void startGame(){

		finishActivity(BlueToothDefaults.REQUEST_CONNECT_DEVICE);	//hide multiplayer screen

		setGameScreen();	//create game view

		initVars();	//init game variables

		//start doing rounds
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_LOGIC_ROUND), refreshRate);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_DRAW_ROUND), Constants.DRAW_REFRESH);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_SEND_BT_MESSAGE_ROUND), 0);
	}

	public void initVars(){

		myObject=new Player((short)(Constants.MARGIN_PADDING+10),(short)Constants.MARGIN_PADDING,0);

		otherObject=new Player(Constants.MARGIN_PADDING,Constants.MARGIN_PADDING,1);
		
		enemy=new Monster[numOfMonsters];
		
		Random randomGenerator = new Random();
		
		for(int i=0;i<numOfMonsters;i++){
			enemy[i]=new Monster(
				(short)(40+i*30),
				(short)(40+i*30)
			);
			
			short aa = (short)(randomGenerator.nextInt(100)-100);
			short bb = (short)(randomGenerator.nextInt(100)-100);
			
			if(Math.abs(aa)<2) aa=50;
			if(Math.abs(bb)<2) bb=-50;
			
			enemy[i].setOrientation(new Vector2D.Short(aa,bb));
			
		}
		

		
		myPath=new PlayPath();
		otherPath=new PlayPath();

		myPoly=new PlayPolygon();


		myPoly.start(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), (short)(Constants.PROJ_HEIGHT-Constants.MARGIN_PADDING));
		myPoly.proceed((short)(Constants.PROJ_WIDTH-Constants.MARGIN_PADDING), Constants.MARGIN_PADDING);
		myPoly.proceed(Constants.MARGIN_PADDING, Constants.MARGIN_PADDING);

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

			if(myPoly.cut(path, sideA, sideB)==true){

				myPoly.setPoly(sideA);

				myBorderMsgCount++;
				
				//new border update message
				
				byte[] newBorderMsg = MessageConvertion.messageToBytes(new BoundsUpdateMsg(myPoly,myBorderMsgCount));

				if(!isSinglePlayer) messageQueue.add(newBorderMsg);

				myObject.recalcBoundMovingPhase(myPoly);
				otherObject.recalcBoundMovingPhase(myPoly);
			}
		}

	}
	
	void doLogic(){

		//------------------------------

		myObject.behave(myPoly);

		//if my object in cutting mode but collided with boundary
		if(myObject.isCutting() && myPoly.getClosestPointIndex(myObject.getLocation())>=0){

			myObject.stopCutting();

			//stop cutting message
			byte[] stopCutMsg = MessageConvertion.messageToBytes(new StopCutMsg(myObject.getLocation()));

			if(!isJoiningGame) TryCutBorder(myPath);
			
			if(!isSinglePlayer) messageQueue.add(stopCutMsg);	//stop cut msg comes after potential update border msg !!!

			if(myPath!=null) myPath.clear();
		}

		//------------------------------

		otherObject.behave(myPoly);

		if(!otherObject.isCutting()){

			if(!isJoiningGame) TryCutBorder(otherPath);

			if(otherPath!=null) otherPath.clear();
		}
		
		//------------------------------
		for(int i=0;i<numOfMonsters;i++)
			enemy[i].behave(myPoly);

	}

	/*
	 * in game processor
	 */
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

			if(msg.getNum()>otherBorderMsgCount){
				myPoly.setPoly(msg.getPoly());
				otherBorderMsgCount=msg.getNum();
			}

			myObject.recalcBoundMovingPhase(myPoly);	//recalc routes after poly change if in border walking phase
			otherObject.recalcBoundMovingPhase(myPoly);
		}

		@Override
		public void process(StopCutMsg msg){

			otherObject.setLocation(msg.getLocation());
			otherObject.stopCutting();
		}
	}

	public void doDrawings(){
		mView.drawObject(myPoly);

		if(otherPath!=null && otherPath.getSize()>1)
			mView.drawObject(otherPath);

		if(myPath!=null && myPath.getSize()>1)
			mView.drawObject(myPath);

		if(!isSinglePlayer)
			mView.drawObject(otherObject);

		mView.drawObject(myObject);
		
		for(int i=0;i<numOfMonsters;i++)
			mView.drawObject(enemy[i]);

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

				if(myObject.intersects(point) && vec.getLength()>0){

					Point2D.Short pos =myObject.getLocation();

					pos.add(vec);

					if(myPoly.getClosestPointIndex(pos)==-1){
						myObject.startCuting(vec,myPath,myPoly);

						byte[] byteMsg = MessageConvertion.messageToBytes(new StartCutMsg(myObject.getLocation(),vec));

						if(!isSinglePlayer)
							messageQueue.add(byteMsg);

					}

					firsttime=false;

				}
				else if(myObject.isCutting() && vec.getLength()>0){
					
					byte[] byteMsg = MessageConvertion.messageToBytes(new ProcCutMsg(myObject.getLocation(),vec));

					if(!isSinglePlayer)
						messageQueue.add(byteMsg);

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

				if(!isSinglePlayer)
					messageQueue.add(byteMsg);

				myObject.setBoundMovingPhase(point,true,myPoly);
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

				mChatService.write(messageQueue.get(0));

				messageQueue.remove(0);

			}

			mHandler.sendMessageDelayed(mHandler.obtainMessage(Constants.MESSAGE_SEND_BT_MESSAGE_ROUND), Constants.SEND_BT_MESSG_REFRESH);

			break;

		case BlueToothDefaults.MESSAGE_READ:

			byte[] readBuf = (byte[]) msg.obj;

			InterMessage incomingMsg = MessageConvertion.bytesToMessage(readBuf);

			messageProc.processMessage(incomingMsg);

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

			myBorderMsgCount++;
			byte[] byteMsg = MessageConvertion.messageToBytes(new BoundsUpdateMsg(myPoly,myBorderMsgCount));


			if(!isSinglePlayer)
				messageQueue.add(byteMsg);

			myObject.recalcBoundMovingPhase(myPoly);
			otherObject.recalcBoundMovingPhase(myPoly);

			return true;
		case R.id.discoverable:
			return true;
		}
		return false;
	}

}