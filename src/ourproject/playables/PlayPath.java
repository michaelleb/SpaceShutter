package ourproject.playables;

import java.util.ArrayList;

import com.andrapp.spaceshutter.MyView;

import android.graphics.Canvas;
import android.util.Log;
import mark.geometry.*;

public class PlayPath extends Path2D.Short implements PlayingObject{
	
	
	
	
	
	public void draw(MyView view, Canvas canvas){
		
		view.DrawObject(this,canvas);
		
	}
	
	public PlayPath clone(){
		
		PlayPath path = new PlayPath();
		
		path.coords=(ArrayList<Point2D.Short>)coords.clone();
		
		return path;
	}
	
	
	
	
	


}
