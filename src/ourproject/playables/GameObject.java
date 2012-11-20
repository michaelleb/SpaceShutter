package ourproject.playables;

import mark.geometry.Point2D;
import mark.geometry.Vector2D;

import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;

public interface GameObject {
	
	public void draw(MyView view, Canvas canvas);
	
	public void behave(PlayPolygon pol);
	
	public void notifyCollision(GameObject object);
	
	public Vector2D.Short getOrientation();
	
	public boolean isCollision(Monster object);
	
	public boolean isCollision(Player object);
	
	public boolean isCollision(PlayPolygon object);
}
