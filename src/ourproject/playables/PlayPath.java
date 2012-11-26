package ourproject.playables;

import java.util.ArrayList;

import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;
import android.util.Log;
import mark.geometry.*;

public class PlayPath extends Path2D.Short implements GameObject{
	
	
	
	private int type;
	
	public int getType(){return type;}
	
	public PlayPath(int type){
		this.type=type;
	}
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this,canvas);
		
	}
	
	public PlayPath clone(){
		
		PlayPath path = new PlayPath(getType());
		
		path.coords=(ArrayList<Point2D.Short>)coords.clone();
		
		return path;
	}
	
	public void behave(MyEnvironment env){}
	
	public Vector2D.Short getOrientation(){
		return new Vector2D.Short((short)0,(short)0);
	}
	
	
	public boolean[] collisionState(GameObject other){
		boolean arr[] = this.collisionState(other);
		return arr;
	}
	
	public void notifyCollision(GameObject object){
		
		
		
	}
	
	
	public boolean isBodyIntersection(Shape2D shape){
		return false;
	}

}
