package ourproject.playables;



import com.andrapp.spaceshutter.MyView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import mark.geometry.*;
import mark.geometry.Vector2D.Short;
import android.graphics.Canvas;
import android.util.Log;

public class Player implements GameObject{

	private int width;
	private int height;

	private int type;

	private short speed=2;

	private Point2D.Short location;

	private Vector2D.Short orientation;


	private PlayPath cuttingPath;		//paths that player cuts


	private Point2D.Short userPointOnMap;		//user specified point to move near to
	private Point2D.Short closestPointOnPoly;	//point on polygon which is closest to userPointOnMap
	private boolean direction;			//bounds direction (2 options)

	public boolean boundariesPhase=false;	//boundaries moving phase

	public boolean cuttingPhase=false;		//path cutting phase

	public boolean isCutting(){return cuttingPhase==true;}

	public void stopCutting(){
		cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());
		cuttingPhase=false;
	}

	public boolean isBoundariesMoving(){return boundariesPhase==true;}


	/*
	 * set moving on boundaries phase, gets user selected point on map, moving direction, polyogn
	 * 
	 * finds closest point on polygon to what user specified, 
	 */

	public void setBoundMovingPhase(Point2D.Short userPointOnMap,boolean direction,PlayPolygon pol){

		if(cuttingPhase){
			cuttingPhase=false;
		}

		this.userPointOnMap=userPointOnMap;

		this.direction=direction;

		this.closestPointOnPoly=pol.closestPointSimplified(userPointOnMap);
		
		boundariesPhase=true;

	}

	public void recalcBoundMovingPhase(PlayPolygon pol){
		if(boundariesPhase)
			setBoundMovingPhase(userPointOnMap,direction,pol);
	}


	/*
	 * start cutting phase, gets initial cut orientation
	 * 
	 */

	public void startCuting(Vector2D.Short orientation,PlayPath path,PlayPolygon pol){

		if(boundariesPhase){
			boundariesPhase=false;
		}

		if(!cuttingPhase){

			orientation.setLength(speed);

			cuttingPhase=true;

			cuttingPath=path;

			cuttingPath.start(location.getx(), location.gety());
			cuttingPath.proceed(location.getx(), location.gety());

			this.orientation=new Vector2D.Short(orientation.getVx(),orientation.getVy());

		}

	}

	public void proceedCutting(Vector2D.Short orientation){

		if(!cuttingPhase)
			return;

		orientation.setLength(speed);

		cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());

		cuttingPath.proceed(location.getx(), location.gety());

		this.orientation=new Vector2D.Short(orientation.getVx(),orientation.getVy());
	}




	public void behave(MyEnvironment env){


		//----------------------- boundary moving phase

		if(boundariesPhase){
			
			Point2D.Short next = env.myPoly.getNextPoint(location,closestPointOnPoly,direction);
			
			if(next==null)
				return;
			
			if(next.sub(location).getLength()>=speed){
				
				orientation = next.sub(location);
				orientation.setLength(speed);
				
				location.add(orientation);
			}
			else{
				location=next;
			}

		}

		//----------------------- cutting phase

		if(cuttingPhase){

			Point2D.Short nextloc= new Point2D.Short(location);
			nextloc.add(orientation);

			if(env.myPoly.contains(nextloc)){

				location=nextloc;

				cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());
			}
			else{

				Line2D.Short traj = new Line2D.Short(location,nextloc);

				Point2D.Short newLoc = env.myPoly.intersectionPoint(traj);

				if(newLoc!=null){
					location=newLoc;
				}

				cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());

				orientation=new Vector2D.Short((short)0,(short)0);
			}

		}


	}



	public Point2D.Short getLocation(){return new Point2D.Short(location.getx(),location.gety());}

	public void setLocation(Point2D.Short loc){location=new Point2D.Short(loc.getx(),loc.gety());}
	
	public void setOrientation(Vector2D.Short orient){orientation=orient;}


	public Vector2D.Short getOrientation(){
		return orientation;
	}





	public Player(short x,short y,int type){

		width=20;
		height=20;

		this.type=type;

		//orientation=new Vector2D.Short(0,0);

		this.location=new Point2D.Short(x,y);
	}

	public int getType(){return type;}

	public short getCenterX(){return location.getx();}
	public short getCenterY(){return location.gety();}


	public void setCenter(Point2D.Short loc){
		location=loc;
	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}

	public boolean intersects(Point2D.Short point){

		if(getDistToCenter(point)<Math.min(height/2,width/2))
			return true;

		return false;
	}



	public float getDistToCenter(Point2D.Short point){
		return location.distance(point);
	}
	
	public boolean[] collisionState(GameObject other){
		boolean arr[] = this.collisionState(other);
		return arr;
	}
	
	

	public void draw(MyView view, Canvas canvas){

		view.DrawObject(this, canvas);

	}

	
	public void notifyCollision(GameObject object){
		
		
		
	}
	
	
	public boolean isBodyIntersection(Shape2D shape){
		return false;
	}


}

