package ourproject.playables;

import com.andrapp.spaceshutter.MyView;

import android.util.Log;


import android.graphics.Canvas;
import mark.geometry.*;

public class PlayPolygon extends Polygon2D.Short implements GameObject{
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this,canvas);
		
	}
	
	public boolean cut(PlayPath path,PlayPolygon half1,PlayPolygon half2){
		
		return this.cut((Path2D.Short)path, (Polygon2D.Short)half1, (Polygon2D.Short)half2);
	}
	
	
	public void behave(MyEnvironment env){}
	
	public void notifyCollision(GameObject object){}
	
	public Vector2D.Short getOrientation(){
		return new Vector2D.Short((short)0,(short)0);
	}
	
	public boolean isBodyIntersection(Shape2D shape){
		
		return shape.isIntersection(this);
	}
	
}
