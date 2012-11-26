package ourproject.playables;

import mark.geometry.Point2D;
import mark.geometry.Polygon2D;
import mark.geometry.Shape2D;
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

	private short speed=4;

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
		this.orientation=new Vector2D.Short((short)0,(short)0);

	}

	public void behave(MyEnvironment env){



		Point2D.Short nextloc = new Point2D.Short(location);
		nextloc.add(orientation);

		Vector2D.Short dir = new Vector2D.Short(orientation);

		body.setCenter(nextloc);

		if(!env.myPoly.isIntersection(body)){
		//if(env.myPoly.contains(nextloc)){
			location=nextloc;
		}
		else{

			body.setCenter(location);

			float max = env.myPoly.maxDistance(body,orientation);

			dir.setLength(max);

			location.add(dir);

			notifyCollision(env.myPoly);

		}

		body.setCenter(location);



		if(this.isBodyIntersection(env.myPath)){

			if(env.chasingPath.getSize()==0){

				Point2D.Short first = env.myPath.closestPointSimplified(location);


				if(first!=null)
					env.chasingPath.start(first.getx(), first.gety());

			}

		}

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

		boolean set=false;

		location.add(orient1);
		body.setCenter(location);

		if(!object.isBodyIntersection(body)){
			orientation=orient1;
			set=true;
		}

		location.sub(orient1);

		location.add(orient2);
		body.setCenter(location);

		if(!object.isBodyIntersection(body)){
			orientation=orient2;
			set=true;
		}

		location.sub(orient2);

		body.setCenter(location);

		if(set==false){
			orientation=new Vector2D.Short(
					(short)-orientation.getVx(),
					(short)-orientation.getVy());
		}

	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}

	public void draw(MyView view, Canvas canvas){

		view.DrawObject(this, canvas);
	}

	public Vector2D.Short getOrientation(){
		return orientation;
	}


	public boolean isBodyIntersection(Shape2D shape){
		return shape.isIntersection(body);
	}


}
