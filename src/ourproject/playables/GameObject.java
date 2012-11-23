package ourproject.playables;


import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;

import mark.geometry.*;

public interface GameObject {
	
	public void draw(MyView view, Canvas canvas);
	
	public void behave(PlayPolygon pol);
	
	public void notifyCollision(GameObject other);
	
	public boolean isBodyIntersection(Shape2D shape);
	
}
