package ourproject.playables;

import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;
import mark.geometry.*;

public class PlayPolygon extends Polygon2D implements PlayingObject{
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this,canvas);
		
	}
	
	public boolean cut(PlayPath path,PlayPolygon half1,PlayPolygon half2){
		
		return this.cut((Path2D)path, (Polygon2D)half1, (Polygon2D)half2);
	}
	
}
