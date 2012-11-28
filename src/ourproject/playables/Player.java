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

	private Box2D.Short body;

	private int type;

	private short speed=2;

	private Vector2D.Short orientation;


	private PlayPath cuttingPath;		//paths that player cuts


	private Point2D.Short userPointOnMap;		//user specified point to move near to
	private Point2D.Short closestPointOnPoly;	//point on polygon which is closest to userPointOnMap
	private boolean direction;			//bounds direction (2 options)

	public boolean boundariesPhase=false;	//boundaries moving phase

	public boolean cuttingPhase=false;		//path cutting phase

	public boolean isCutting(){return cuttingPhase==true;}

	public void stopCutting(){

		if(cuttingPhase && cuttingPath.getSize()>0){

			cuttingPath.setValue(cuttingPath.getSize()-1, body.getLocation().getx(), body.getLocation().gety());

			cuttingPhase=false;
		}
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

			Point2D.Short tmploc = new Point2D.Short(body.getLocation());

			tmploc.add(orientation);

			if(!pol.contains(tmploc)){
				return;
			}

			orientation.setLength(speed);

			cuttingPhase=true;

			cuttingPath=path;

			cuttingPath.start(body.getLocation().getx(), body.getLocation().gety());
			cuttingPath.proceed(body.getLocation().getx(), body.getLocation().gety());

			this.orientation=new Vector2D.Short(orientation.getVx(),orientation.getVy());

		}

	}

	public void proceedCutting(Vector2D.Short orientation){

		if(cuttingPath.getSize()>=2){

			orientation.setLength(speed);

			if(!cuttingPhase || (this.orientation.getVx()==orientation.getVx() && this.orientation.getVy()==orientation.getVy())){
				return;
			}

			cuttingPath.setValue(cuttingPath.getSize()-1, body.getLocation().getx(), body.getLocation().gety());

			Point2D.Short p1 = cuttingPath.getPoint(cuttingPath.getSize()-1);
			Point2D.Short p2 = cuttingPath.getPoint(cuttingPath.getSize()-2);


			if(p1.equals(p2)){
				cuttingPath.removeLast();
				cuttingPath.removeLast();
			}

			if(!(this.orientation.getVx()==-orientation.getVx() && this.orientation.getVy()==-orientation.getVy()))
				cuttingPath.proceed(body.getLocation().getx(), body.getLocation().gety());


			this.orientation=new Vector2D.Short(orientation.getVx(),orientation.getVy());

		}
	}




	public void behave(MyEnvironment env){


		//----------------------- boundary moving phase

		if(boundariesPhase){

			Point2D.Short next = env.myPoly.getNextPoint(body.getLocation(),closestPointOnPoly,direction);

			if(next==null)
				return;

			if(next.sub(body.getLocation()).getLength()>=speed){

				orientation = next.sub(body.getLocation());
				orientation.setLength(speed);

				body.getLocation().add(orientation);
			}
			else{
				body.setLocation(next);
			}

		}

		//----------------------- cutting phase

		if(cuttingPhase && cuttingPath.getSize()>0){

			Point2D.Short nextloc= new Point2D.Short(body.getLocation());
			nextloc.add(orientation);

			if(env.myPoly.contains(nextloc)){

				body.setLocation(nextloc);

				cuttingPath.setValue(cuttingPath.getSize()-1, body.getLocation().getx(), body.getLocation().gety());
			}
			else{

				Line2D.Short traj = new Line2D.Short(body.getLocation(),nextloc);

				Point2D.Short newLoc = env.myPoly.intersectionPoint(traj);

				if(newLoc!=null){
					body.setLocation(newLoc);
				}


				cuttingPath.setValue(cuttingPath.getSize()-1, body.getLocation().getx(), body.getLocation().gety());

				orientation=new Vector2D.Short((short)0,(short)0);
			}

		}


	}



	public Point2D.Short getLocation(){return new Point2D.Short(body.getLocation());}

	public void setLocation(Point2D.Short loc){
		body.setLocation(loc);

	}

	public void setOrientation(Vector2D.Short orient){orientation=orient;}


	public Vector2D.Short getOrientation(){
		return orientation;
	}





	public Player(short x,short y,int type){

		body=new Box2D.Short(new Point2D.Short(x,y), (short)20, (short)20);

		this.type=type;

		orientation=new Vector2D.Short((short)0,(short)0);
	}

	public int getType(){return type;}

	public short getCenterX(){return body.getLocation().getx();}
	public short getCenterY(){return body.getLocation().gety();}


	public void setCenter(Point2D.Short loc){
		body.setLocation(loc);
	}

	public int getWidth(){return body.getWidth();}
	public int getHeight(){return body.getHeight();}

	public boolean intersects(Point2D.Short point){

		if(getDistToCenter(point)<Math.min(body.getHeight()/2,body.getWidth()/2))
			return true;

		return false;
	}



	public float getDistToCenter(Point2D.Short point){
		return body.getLocation().distance(point);
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

