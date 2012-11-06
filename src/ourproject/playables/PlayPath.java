package ourproject.playables;

import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;
import mark.geometry.*;

public class PlayPath extends Path2D implements PlayingObject{
	
	
	
	
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this,canvas);
		
	}
}
