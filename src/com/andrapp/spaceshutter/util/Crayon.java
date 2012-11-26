package com.andrapp.spaceshutter.util;

import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.andrapp.spaceshutter.model.Monster;
import com.andrapp.spaceshutter.model.Player;

//This class provide draw capability to objects that hold it.
//The purpose of Crayon class is to simplify change of draw functionality by 
//decoupling object from its draw logic
abstract public class Crayon {
	private final static String TAG = Crayon.class.getSimpleName();
	
	protected Bitmap bitmap;	//hold the image or sprite of this creature
	protected Resources res;	//hold system resources for loading images
	protected Paint paint;		//describe paint style 
	
	//scaling parameters
	protected static int xOffset = 50;	//offset of leftUp corner of the map.
	protected static int yOffset = 50;
	protected static float scaling = 1;	//size scaling on screen
	protected Point2D realPoint	 = new Point2D();
	
	public Crayon(Resources appResources) {
		res = appResources;
	}
	
	abstract public void draw(Canvas c);
	abstract public void init();	//sets all bitmaps and variables.
	
	public Path scaleMovePahttoAndroidPath(MovePath surcePath,Path destPath){
		
		
		Iterator<Point2D> movePathIter = surcePath.iterator();
		try{
			//there is always at least start point in MovePath 
			this.getRealPoint(movePathIter.next());
			destPath.moveTo(realPoint.x, realPoint.y);
			
			while(movePathIter.hasNext()){
				this.getRealPoint(movePathIter.next());
				destPath.lineTo(realPoint.x, realPoint.y);
			}
			
		}catch(Exception e){
			
			Log.d(TAG,e.getMessage());
		}
		
		return destPath;
	}
	
	//supported Host objects. you need to expose the right one for your Crayon subclass
	protected void	setHost(Player p){}
	protected void	setHost(Monster m){}
	protected void	setHost(Map map){}

	//getters and setters
	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public float getScaling() {
		return scaling;
	}

	public void setScaling(float scaling) {
		this.scaling = scaling;
	}
	
	protected Point2D getRealPoint(Point2D logicPoint){
		
		getRealPoint(logicPoint.x,logicPoint.y);
		
		return realPoint;
	}
	
	protected  Point2D getRealPoint(int logicX, int logicY){
		
		realPoint.x = (Math.round(logicX * scaling) + xOffset);
		realPoint.y = (Math.round(logicY * scaling) + yOffset);
		
		return realPoint;
	}

	
	
}
