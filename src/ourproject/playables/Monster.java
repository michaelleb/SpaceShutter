package ourproject.playables;

import mark.geometry.Box2D;
import mark.geometry.Point2D;
import mark.geometry.Point2D.Short;
import mark.geometry.Polygon2D;
import mark.geometry.Shape2D;
import mark.geometry.Vector2D;
import android.graphics.Canvas;

import com.andrapp.spaceshutter.MyView;

import android.util.Log;

public class Monster implements GameObject {

	private Box2D.Short body;

	private Vector2D.Short orientation;

	private short speed=4;

	private float distToWall=0;

	public Monster(short x,short y){

		body=new Box2D.Short(new Point2D.Short(x,y), (short)20, (short)20);

		this.orientation=new Vector2D.Short((short)0,(short)0);

	}

	private float len=0;

	public void behave(MyEnvironment env){

		if(len==0){
			recalcOrientation(env);
		}

		if(len<orientation.getLength()){

			Vector2D.Short orientttt = new Vector2D.Short(orientation);
			orientttt.setLength(len);

			body.getLocation().add(orientttt);

			len=0;
		}
		else{

			len-=orientation.getLength();

			body.getLocation().add(orientation);
		}

	}

	public void setOrientation(Vector2D.Short orient){
		orientation=orient;
	}

	public void recalcOrientation(MyEnvironment env){

		float currdist = env.myPoly.maxDistance(body, orientation);
		
		if(currdist>orientation.getLength() && currdist<10000){
			len=currdist;
			return;
		}
		
		Vector2D.Short orient1 = new Vector2D.Short(orientation);
		Vector2D.Short orient2 = new Vector2D.Short(orientation);

		orient1.setVx((short)-orient1.getVx());
		orient2.setVy((short)-orient2.getVy());

		float dist1=env.myPoly.maxDistance(body, orient1);
		float dist2=env.myPoly.maxDistance(body, orient2);

		if(dist1>orient1.getLength() && dist1<10000){
			orientation=orient1;
			len=dist1;
		}
		else if(dist2>orient2.getLength() && dist2<10000){
			orientation=orient2;
			len=dist2;
		}
		else{
			orientation.setVx((short)-orientation.getVx());
			orientation.setVy((short)-orientation.getVy());
			
			len = env.myPoly.maxDistance(body, orientation);
		}
		

		len-=(short)(orientation.getLength()/2);
	
	}



	public Point2D.Short getLocation(){return new Point2D.Short(body.getLocation());}

	public void setLocation(Point2D.Short loc){body.setLocation(loc);}




	public void notifyCollision(GameObject object){

	}

	public int getWidth(){return body.getWidth();}
	public int getHeight(){return body.getHeight();}

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
