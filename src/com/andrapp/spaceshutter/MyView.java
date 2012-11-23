package com.andrapp.spaceshutter;

import mark.geometry.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.os.Message;

import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.ListIterator;

import java.lang.Runnable;

import com.andrapp.spaceshutter.R;

import ourproject.playables.*;


import mark.geometry.*;



public class MyView extends View {

	private Paint paint = new Paint();

	private Paint pathpaint = new Paint();



	private int logicHeight;
	private int logicWidth;

	private int height;
	private int width;



	Path mPath = new Path();

	private Handler evHandler;

	private ArrayList<GameObject> objectsQueue;


	public MyView(Context context, AttributeSet attrs,int logicHeight,int logicWidth,Handler handler) {
		super(context, attrs);

		paint.setAntiAlias(true);
		paint.setStrokeWidth(6f);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);


		//pathpaint

		pathpaint.setAntiAlias(true);
		pathpaint.setStrokeWidth(6f);
		pathpaint.setColor(Color.RED);
		pathpaint.setStyle(Paint.Style.STROKE);
		pathpaint.setStrokeJoin(Paint.Join.ROUND);


		this.logicHeight=logicHeight;
		this.logicWidth=logicWidth;

		evHandler=handler;

		objectsQueue=new ArrayList<GameObject>();
	}



	public void drawObject(Player obj){


		objectsQueue.add(obj);
	}

	public void drawObject(PlayPolygon obj){


		objectsQueue.add(obj);
	}


	public void drawObject(PlayPath obj){


		objectsQueue.add(obj);
	}
	
	public void drawObject(Monster obj){
		objectsQueue.add(obj);
	}
	
	public void executeDrawing(){
		invalidate();
	}


	public Point2D.Short logicToPhys(Point2D.Short point){
		Point2D.Short newPnt=new Point2D.Short(
				(short)((float)point.getx()*((float)width/(float)logicWidth)),
				(short)((float)point.gety()*((float)height/(float)logicHeight))
				);

		return newPnt;
	}

	public Point2D.Short PhysToLogic(Point2D.Short point){
		Point2D.Short newPnt=new Point2D.Short(
				(short)((float)point.getx()*((float)logicWidth/(float)width)),
				(short)((float)point.gety()*((float)logicHeight/(float)height))
				);

		return newPnt;
	}

	public int PhysToLogicHeight(int physHeight){
		return (int)((float)physHeight*(float)((float)logicHeight/(float)height));
	}
	public int PhysToLogicWidth(float physWidth){
		return (int)((float)physWidth*(float)((float)logicWidth/(float)width));
	}

	public int LogicToPhysHeight(int logHeight){
		return (int)((float)logHeight*(float)((float)height/(float)logicHeight));
	}
	public int LogicToPhysWidth(int logWidth){
		return (int)((float)logWidth*(float)((float)width/(float)logicWidth));
	}

	@Override
	protected void onDraw(Canvas canvas) {



		canvas.drawColor(Color.BLACK);

		for(int i=0;i<objectsQueue.size();i++){

			GameObject currObj = objectsQueue.get(i);

			currObj.draw(this,canvas);
		}

		objectsQueue.clear();

		canvas.drawPath(mPath, pathpaint);
	}

	public void DrawObject(PlayPath currObj,Canvas canvas){

		Path path = new Path();

		boolean first=true;

		for(int i=0;i<currObj.getSize();i++){

			Point2D.Short elem=currObj.getPoint(i);

			Point2D.Short phys = logicToPhys(elem);

			if(first)
				path.moveTo(phys.getx(), phys.gety());
			else
				path.lineTo(phys.getx(), phys.gety());

			first=false;


			//Log.e("",""+phys.getx()+" "+phys.gety());
		}
		
		if(currObj.getType()==0)
			pathpaint.setColor(Color.RED);
		if(currObj.getType()==1)
			pathpaint.setColor(Color.GREEN);
		if(currObj.getType()==2)
			pathpaint.setColor(Color.YELLOW);

		canvas.drawPath(path, pathpaint);

	}

	public void DrawObject(PlayPolygon currObj,Canvas canvas){

		Path path = new Path();

		boolean first=true;

		for(int i=0;i<currObj.getSize();i++){

			Point2D.Short elem=currObj.getPoint(i);

			Point2D.Short phys = logicToPhys(elem);

			if(first)
				path.moveTo(phys.getx(), phys.gety());
			else
				path.lineTo(phys.getx(), phys.gety());

			first=false;


			//Log.e("",""+phys.getx()+" "+phys.gety());
		}

		canvas.drawPath(path, paint);

	}

	public Bitmap p1=null;
	public Bitmap p2=null;

	public void DrawObject(Player currObj,Canvas canvas){

		


		int objHeight=LogicToPhysHeight(currObj.getHeight());
		int objWidth=LogicToPhysWidth(currObj.getWidth());

		Bitmap bitmapPtr;


		if(currObj.getType()==0)
			bitmapPtr=p1;
		else
			bitmapPtr=p2;

		if(bitmapPtr==null){

			Resources r = this.getContext().getResources();

			Drawable dwble;

			if(currObj.getType()==0)
				dwble = r.getDrawable(R.drawable.redstar);
			else
				dwble = r.getDrawable(R.drawable.greenstar);

			bitmapPtr = Bitmap.createBitmap(objWidth, objHeight, Bitmap.Config.ARGB_8888);
			Canvas canvass = new Canvas(bitmapPtr);

			dwble.setBounds(0, 0, objWidth, objHeight);

			dwble.draw(canvass);
		}
		
		Point2D.Short physLoc = logicToPhys(currObj.getLocation());
		
		canvas.drawBitmap(bitmapPtr,
				physLoc.getx()-objWidth/2,
				physLoc.gety()-objHeight/2, null);

	}

	
	
	
	
	
	
	
	

	public Bitmap p3=null;

	public void DrawObject(Monster currObj,Canvas canvas){

		


		int objHeight=LogicToPhysHeight(currObj.getHeight());
		int objWidth=LogicToPhysWidth(currObj.getWidth());

		Bitmap bitmapPtr;


		bitmapPtr=p3;

		if(bitmapPtr==null){

			Resources r = this.getContext().getResources();

			Drawable dwble;

			dwble = r.getDrawable(R.drawable.yellowstar);

			bitmapPtr = Bitmap.createBitmap(objWidth, objHeight, Bitmap.Config.ARGB_8888);
			Canvas canvass = new Canvas(bitmapPtr);

			dwble.setBounds(0, 0, objWidth, objHeight);

			dwble.draw(canvass);
		}
		
		Point2D.Short physLoc = logicToPhys(currObj.getLocation());
		
		canvas.drawBitmap(bitmapPtr,
				physLoc.getx()-objWidth/2,
				physLoc.gety()-objHeight/2, null);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float eventX = event.getX();
		float eventY = event.getY();

		Point2D.Short point = null;








		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:


			mPath.reset();

			mPath.moveTo(eventX, eventY);


			point = new Point2D.Short((short)eventX,(short)eventY);

			sendKeyEvent(event,point);

			return true;
		case MotionEvent.ACTION_MOVE:

			mPath.lineTo(eventX, eventY);

			point = new Point2D.Short((short)eventX,(short)eventY);

			sendKeyEvent(event,point);

			break;
		case MotionEvent.ACTION_UP: // nothing to do break; default:


			mPath.lineTo(eventX, eventY);

			point = new Point2D.Short((short)eventX,(short)eventY);

			sendKeyEvent(event,point);

		} 



		//invalidate();

		return true;

	}

	public void sendKeyEvent(MotionEvent event, Point2D.Short point){

		point=PhysToLogic(point);

		Message msg = new Message();

		msg.what=event.getAction();

		msg.obj=point;

		evHandler.sendMessage(msg);
	}

	public void setSizes(int height,int width){


		LayoutParams params = (LayoutParams) this.getLayoutParams();

		params.height=height;

		params.width = width;
		this.setLayoutParams(params);

		this.height=height;
		this.width=width;

	}

}