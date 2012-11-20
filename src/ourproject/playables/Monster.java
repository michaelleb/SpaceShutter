package ourproject.playables;

import mark.geometry.Point2D;
import mark.geometry.Polygon2D;
import mark.geometry.Vector2D;
import android.graphics.Canvas;

import com.andrapp.spaceshutter.MyView;

import android.util.Log;

public class Monster implements GameObject {

	private int width;
	private int height;

	private Polygon2D.Short body;

	private Point2D.Short location;

	private Vector2D.Short orientation;

	private short speed=7;

	public Monster(short x,short y){

		width=20;
		height=20;

		body=new Polygon2D.Short();

		body.start((short)0, (short)0);
		body.proceed((short)19, (short)0);
		body.proceed((short)19, (short)19);
		body.proceed((short)0, (short)19);
		body.proceed((short)0, (short)0);

		this.location=new Point2D.Short(x,y);
	}

	public void behave(PlayPolygon pol){



		Point2D.Short nextloc = new Point2D.Short(location);
		nextloc.add(orientation);

		Vector2D.Short dir = new Vector2D.Short(orientation);

		body.setCenter(nextloc);

		if(!body.isCollision(pol)){

			location=nextloc;
		}
		else{

			body.setCenter(location);

			float max = pol.maxDistance(body,orientation);

			dir.setLength(max);

			//location.add(dir);

			notifyCollision(pol);

		}

		body.setCenter(location);
	}

	public Polygon2D.Short getBounds(){return body;}

	public Point2D.Short getLocation(){return new Point2D.Short(location.getx(),location.gety());}

	public void setLocation(Point2D.Short loc){location=new Point2D.Short(loc.getx(),loc.gety());}

	public void setOrientation(Vector2D.Short orient){orientation=orient;orientation.setLength(speed);}

	public void notifyCollision(GameObject object){



		Vector2D.Short orient1 = new Vector2D.Short(orientation);

		Vector2D.Short orient2 = new Vector2D.Short(orientation);


		orient1.setVx((short)(-orient1.getVx()));

		orient2.setVy((short)(-orient2.getVy()));
		
		
		


		body.add(orient1);
		if(!object.isCollision(this)){
			orientation=orient1;
			Log.e("","orient1");
		}
		body.sub(orient1);

		body.add(orient2);
		if(!object.isCollision(this)){
			orientation=orient2;
			Log.e("","orient2");
		}
		body.sub(orient2);

	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}

	public void draw(MyView view, Canvas canvas){

		view.DrawObject(this, canvas);
	}

	public Vector2D.Short getOrientation(){
		return orientation;
	}


	public boolean isCollision(Monster object){
		return true;
	}

	public boolean isCollision(Player object){
		return true;
	}

	public boolean isCollision(PlayPolygon object){
		return true;
	}

}
