package com.andrapp.spaceshutter;

import mark.geometry.Path2D;
import mark.geometry.Point2D;

import mark.geometry.Polygon2D;

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

	private ArrayList<PlayingObject> objectsQueue;


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

		objectsQueue=new ArrayList<PlayingObject>();
	}



	public void drawObject(DummyObject obj){


		objectsQueue.add(obj);
	}
	
	public void drawObject(PlayPolygon obj){


		objectsQueue.add(obj);
	}


	public void drawObject(PlayPath obj){


		objectsQueue.add(obj);
	}

	public void execureDrawing(){
		invalidate();
	}


	public Point2D logicToPhys(Point2D point){
		Point2D newPnt=new Point2D(
				(int)((float)point.getx()*((float)width/(float)logicWidth)),
				(int)((float)point.gety()*((float)height/(float)logicHeight))
				);

		return newPnt;
	}

	public Point2D PhysToLogic(Point2D point){
		Point2D newPnt=new Point2D(
				(int)((float)point.getx()*((float)logicWidth/(float)width)),
				(int)((float)point.gety()*((float)logicHeight/(float)height))
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

			PlayingObject currObj = objectsQueue.get(i);

			currObj.draw(this,canvas);
		}

		objectsQueue.clear();
		
		canvas.drawPath(mPath, pathpaint);
	}

	public void DrawObject(PlayPath currObj,Canvas canvas){

		Path path = new Path();

		boolean first=true;

		for(int i=0;i<currObj.getSize();i++){

			Point2D elem=currObj.getPoint(i);

			Point2D phys = logicToPhys(elem);

			if(first)
				path.moveTo(phys.getx(), phys.gety());
			else
				path.lineTo(phys.getx(), phys.gety());

			first=false;


			//Log.e("",""+phys.getx()+" "+phys.gety());
		}

		canvas.drawPath(path, paint);

	}
	
	public void DrawObject(PlayPolygon currObj,Canvas canvas){
		
		Path path = new Path();

		boolean first=true;

		for(int i=0;i<currObj.getSize();i++){

			Point2D elem=currObj.getPoint(i);

			Point2D phys = logicToPhys(elem);

			if(first)
				path.moveTo(phys.getx(), phys.gety());
			else
				path.lineTo(phys.getx(), phys.gety());

			first=false;


			//Log.e("",""+phys.getx()+" "+phys.gety());
		}

		canvas.drawPath(path, paint);
		
	}
	
	public void DrawObject(DummyObject currObj,Canvas canvas){
		int objHeight=LogicToPhysHeight(currObj.getHeight());
		int objWidth=LogicToPhysWidth(currObj.getWidth());



		Resources r = this.getContext().getResources();
		
		Drawable dwble;
		
		if(currObj.getType()==0)
			dwble = r.getDrawable(R.drawable.redstar);
		else
			dwble = r.getDrawable(R.drawable.greenstar);
		
		Bitmap bitmap = Bitmap.createBitmap(objWidth, objHeight, Bitmap.Config.ARGB_8888);
		Canvas canvass = new Canvas(bitmap);

		dwble.setBounds(0, 0, objWidth, objHeight);

		dwble.draw(canvass);

		Point2D physLoc = logicToPhys(currObj.getLocation());

		canvas.drawBitmap(bitmap,
				physLoc.getx()-objWidth/2,
				physLoc.gety()-objHeight/2, null);

	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float eventX = event.getX();
		float eventY = event.getY();

		Point2D point = null;

		






		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			
			mPath.reset();
			
			mPath.moveTo(eventX, eventY);
			
			
			point = new Point2D(eventX,eventY);
			
			sendKeyEvent(event,point);
			
			return true;
		case MotionEvent.ACTION_MOVE:
			
			mPath.lineTo(eventX, eventY);
			
			point = new Point2D(eventX,eventY);
			
			sendKeyEvent(event,point);
			
			break;
		case MotionEvent.ACTION_UP: // nothing to do break; default:
			
			
			mPath.lineTo(eventX, eventY);
			
			point = new Point2D(eventX,eventY);
			
			sendKeyEvent(event,point);

		} 


		
		//invalidate();

		return true;

	}
	
	public void sendKeyEvent(MotionEvent event, Point2D point){
		
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