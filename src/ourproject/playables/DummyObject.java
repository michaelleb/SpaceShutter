package ourproject.playables;



import com.andrapp.spaceshutter.MyView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import mark.geometry.*;
import android.graphics.Canvas;
import android.util.Log;

public class DummyObject implements PlayingObject{

	private Point2D location;
	private Vector2D orientation;

	private int width;
	private int height;
	
	private int type;
	
	public DummyObject(float x,float y,int type){

		width=10;
		height=10;
		
		this.type=type;
		
		orientation=new Vector2D(0,0);
		
		this.location=new Point2D(x,y);
	}
	
	public int getType(){return type;}
	
	public float getCenterX(){return location.getx();}
	public float getCenterY(){return location.gety();}
	
	
	public void setCenter(Point2D loc){
		location=loc;
	}
	
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public Point2D getLocation(){return new Point2D(location.getx(),location.gety());}
	
	public Vector2D getOrientation(){return orientation;} 
	
	public void setOrientation(Vector2D newVec){
		orientation=newVec;
	}
	
	public boolean intersects(Point2D point){

		if(getDistToCenter(point)<Math.min(height/2,width/2))
			return true;

		return false;
	}


	public void behave(PlayPolygon pol){
		
		Point2D loc2 = new Point2D(location);
					
		loc2.add(orientation);
		
		Point2D intersection = pol.lineIntersects(new Line2D(location,loc2));
		
		if(intersection!=null){
			
			//Log.e("","intersects");
			
			orientation=new Vector2D(0,0);
			
			location=intersection;
		}
		else if(!pol.contains(loc2)){
			orientation=new Vector2D(0,0);
		}
		else{
			//Log.e("","noo");
			
			location=loc2;
		}
		
		
	}
	
	public float getDistToCenter(Point2D point){
		return location.distance(point);
	}
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this, canvas);
		
	}

}

