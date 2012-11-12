package ourproject.playables;



import com.andrapp.spaceshutter.MyView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import mark.geometry.*;
import android.graphics.Canvas;
import android.util.Log;

public class DummyObject implements PlayingObject{

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
	private int nextCheckpointIndex; 	//next index on a poly to move to, in boundaries moving phase

	public boolean boundariesPhase=false;	//boundaries moving phase

	public boolean cuttingPhase=false;		//path cutting phase

	public boolean isCutting(){return cuttingPhase==true;}
	public boolean isBoundariesMoving(){return boundariesPhase==true;}


	/*
	 * set moving on boundaries phase, gets user selected point on map, moving direction, polyogn
	 * 
	 * finds closest point on polygon to what user specified, 
	 */

	public void setBoundMovingPhase(Point2D.Short userPointOnMap,boolean direction,PlayPolygon pol){

		if(cuttingPhase)
			return;

		this.userPointOnMap=userPointOnMap;

		this.direction=direction;

		this.closestPointOnPoly=pol.closestPointSimplified(userPointOnMap);

		nextCheckpointIndex = pol.getLineWithPointIndex(location);
		
		if(closestPointOnPoly!=null && nextCheckpointIndex>=0){
			boundariesPhase=true;
			
			if(!direction)
				nextCheckpointIndex=(nextCheckpointIndex+1+pol.getSize())%pol.getSize();
		}
		else{
			
			boundariesPhase=false;
			
			return;
		}

		//log.e("closest point !!!>>>",""+closestPointOnPoly.getx()+"-"+closestPointOnPoly.gety()+" next="+nextCheckpointIndex);

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







	
	
	
	
	
	
	
	
	

	public void behave(PlayPolygon pol){


		//----------------------- boundary moving phase

		if(boundariesPhase){

			int polsize = pol.getSize();

			Point2D.Short next = pol.getPoint(nextCheckpointIndex);	//next point destination


			if(closestPointOnPoly==null)
			{
				boundariesPhase=false;

				return;
			}

			if(location.distance(closestPointOnPoly)<=speed){	//if final destination within reach

				location=closestPointOnPoly;

				boundariesPhase=false;
			}
			else if(location.distance(next)<=speed){	//if next point within reach

				int diff=1;

				if(direction)
					diff=-1;

				nextCheckpointIndex=(nextCheckpointIndex+diff+pol.getSize())%polsize;

				location=next;
			}
			else{	//else: proceed moving towards next point

				Vector2D.Short vec = new Vector2D.Short(
						(short)(next.getx()-location.getx()),
						(short)(next.gety()-location.gety()));

				vec.setLength(speed);

				location.add(vec);
			}


		}

		//----------------------- cutting phase
		
		if(cuttingPhase){

			Point2D.Short nextloc= new Point2D.Short(location);
			nextloc.add(orientation);
			
			//Log.e("",""+pol.contains(nextloc));
			
			if(pol.contains(nextloc)){
				
				location=nextloc;

				cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());
			}
			else{

				//Log.e("","bbbb");

				Line2D.Short traj = new Line2D.Short(location,nextloc);

				Point2D.Short newLoc = pol.intersectionPoint(traj);

				if(newLoc!=null){
					location=newLoc;
				}

				finnishCutting();
			}

		}


	}
	
	public void finnishCutting(){
		cuttingPhase=false;

		cuttingPath.setValue(cuttingPath.getSize()-1, location.getx(), location.gety());
	}

	
	
	
	public Point2D.Short getLocation(){return new Point2D.Short(location.getx(),location.gety());}
	
	public void setLocation(Point2D.Short loc){location=new Point2D.Short(loc.getx(),loc.gety());}
	
	
	
	
	
	
	

	public DummyObject(short x,short y,int type){

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

	public void draw(MyView view, Canvas canvas){

		view.DrawObject(this, canvas);

	}





}

