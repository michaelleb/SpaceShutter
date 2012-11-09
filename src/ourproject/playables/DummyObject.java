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


	private float speed=2.13f;

	private Point2D location;
	
	private Vector2D orientation;


	private PlayPath cuttingPath;		//paths that player cuts

	
	
	
	private Point2D userPointOnMap;		//user specified point to move near to
	private Point2D closestPointOnPoly;	//point on polygon which is closest to userPointOnMap
	private boolean direction;			//bounds direction (2 options)
	private int nextCheckpointIndex; 	//next index on a poly to move to, in boundaries moving phase

	public boolean boundariesPhase=false;	//boundaries moving phase

	public boolean cuttingPhase=false;		//path cutting phase

	public boolean isCutting(){return cuttingPhase==true;}
	public boolean isBoundariesMoving(){return boundariesPhase==true;}


	private Vector2D getOrientation(){return orientation;}
	private void setOrientation(float x,float y){orientation=new Vector2D(x,y);}


	
	
	/*
	 * set moving on boundaries phase, gets user selected point on map, moving direction, polyogn
	 * 
	 * finds closest point on polygon to what user specified, 
	 */
	
	public void setBoundMovingPhase(Point2D userPointOnMap,boolean direction,PlayPolygon pol){
		
		if(cuttingPhase)
			return;
		
		this.userPointOnMap=userPointOnMap;

		this.direction=direction;

		this.closestPointOnPoly=pol.closestPointSimplified(userPointOnMap);

		nextCheckpointIndex = pol.getLineWithPointIndex(location);
		
		if(direction)
			nextCheckpointIndex=(nextCheckpointIndex-1+pol.getSize())%pol.getSize();
		
		if(closestPointOnPoly!=null && nextCheckpointIndex>=0){
			boundariesPhase=true;
		}
		else{
			return;
		}
		
		//log.e("closest point !!!>>>",""+closestPointOnPoly.getx()+"-"+closestPointOnPoly.gety()+" next="+nextCheckpointIndex);

	}

	public void recalcBoundMovingPhase(PlayPolygon pol){
		setBoundMovingPhase(userPointOnMap,direction,pol);
	}


	public void behave(PlayPolygon pol){


//----------------------- boundary moving phase

		if(boundariesPhase){
			
			int polsize = pol.getSize();
			
			Point2D next = pol.getPoint(nextCheckpointIndex);	//next point destination
			
			
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

				Vector2D vec = new Vector2D(next.getx()-location.getx(),next.gety()-location.gety());

				vec.setLength(speed);

				location.add(vec);
			}


		}

//----------------------- cutting phase

		//Log.e("",""+pol.contains(location));
		
		if(cuttingPhase){

			Point2D nextloc= new Point2D(location);
			nextloc.add(orientation);

			if(pol.contains(nextloc)){
				
				//Log.e("on poly 1",location.getx()+","+location.gety()+" : "+pol.contains(location));
				
				location=nextloc;
				
				//Log.e("on poly 2",location.getx()+","+location.gety()+" : "+pol.contains(nextloc));
			}
			else{
				
				
				
				Line2D traj = new Line2D(location,nextloc);

				Point2D newLoc = pol.intersectionPoint(traj);

				if(newLoc!=null){
					location=newLoc;
					
					//Log.e(">>>>",newLoc.getx()+","+newLoc.gety());
					
				}
				
				Log.e("stop",location.getx()+","+location.gety());
				
				cuttingPath.proceed(location.getx(), location.gety());
				
				
				
				
				cuttingPhase=false;
				
				
			}
		}


	}



	public PlayPath getCutPath(){

		if(cuttingPath==null || !cuttingPhase)
			return null;

		PlayPath getCutPathTemp = cuttingPath.clone();

		Point2D offset = new Point2D(location);
		
		Vector2D orientation2=new Vector2D(orientation.getVx(),orientation.getVy());
		
		orientation2.setLength(speed+0.1f);
		
		offset.add(orientation2);
		
		getCutPathTemp.proceed(offset.getx(), offset.gety());
		
		return getCutPathTemp;

	}

	/*
	 * start cutting phase, gets initial cut orientation
	 * 
	 */
	
	public void startCuting(Vector2D orientation){

		if(!boundariesPhase && !cuttingPhase){

			orientation.setLength(speed);


			cuttingPhase=true;

			cuttingPath=new PlayPath();
			
			
			Point2D offset = new Point2D(location);
			
			Vector2D orientation2=new Vector2D(orientation.getVx(),orientation.getVy());
			
			orientation2.setLength(0.1f);
			
			offset.sub(orientation2);
			
			cuttingPath.start(offset.getx(), offset.gety());

			this.orientation=new Vector2D(orientation.getVx(),orientation.getVy());

		}

	}

	public void proceedCutting(Vector2D orientation){

		if(!cuttingPhase)
			return;

		cuttingPath.proceed(location.getx(), location.gety());

		this.orientation=new Vector2D(orientation.getVx(),orientation.getVy());

		this.orientation.setLength(speed);
	}


















	public DummyObject(float x,float y,int type){

		width=10;
		height=10;

		this.type=type;

		//orientation=new Vector2D(0,0);

		this.location=new Point2D(x,y);
	}

	public int getType(){return type;}

	public float getCenterX(){return location.getx();}
	public float getCenterY(){return location.gety();}


	public void setCenter(Point2D loc){
		location=loc;
	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}

	public Point2D getLocation(){return new Point2D(location.getx(),location.gety());}

	//public Vector2D getOrientation(){return orientation;} 

	//public void setOrientation(Vector2D newVec){orientation=newVec;}

	public boolean intersects(Point2D point){

		if(getDistToCenter(point)<Math.min(height/2,width/2))
			return true;

		return false;
	}



	public float getDistToCenter(Point2D point){
		return location.distance(point);
	}

	public void draw(MyView view, Canvas canvas){

		view.DrawObject(this, canvas);

	}





}

