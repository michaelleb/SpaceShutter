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


	private float speed=4.0f;





	private Point2D location;
	private Vector2D orientation;


	private PlayPath cuttingPath;

	private Point2D closestPoint;
	private Point2D userClosestPoint;
	private boolean direction;



	public boolean boundariesPhase=false;

	public boolean cuttingPhase=false;

	public boolean isCutting(){return cuttingPhase==true;}


	private Vector2D getOrientation(){return orientation;}
	private void setOrientation(float x,float y){orientation=new Vector2D(x,y);}


	private int nextChkpnt;

	public void setBoundMovingPhase(Point2D userClosestPoint,boolean direction,PlayPolygon pol){
		
		
		if(cuttingPhase)
			return;
		
		this.userClosestPoint=closestPoint;

		this.direction=direction;


		this.closestPoint=pol.closestPointSimplified(userClosestPoint);


		nextChkpnt = pol.getLineWithPointIndex(location);
		
		if(closestPoint!=null && nextChkpnt>=0){
			boundariesPhase=true;
		}
		else{
			return;
		}
		
		if(direction){
			
			//nextChkpnt--;
			
			nextChkpnt=(nextChkpnt-1+pol.getSize())%pol.getSize();
			
			//if(nextChkpnt==-1)
			//	nextChkpnt=pol.getSize()-1;
			
		}
			
		
		Log.e("closest point !!!>>>",""+closestPoint.getx()+"-"+closestPoint.gety()+" next="+nextChkpnt);

	}

	public void recalcBoundMovingPhase(PlayPolygon pol){
		setBoundMovingPhase(userClosestPoint,direction,pol);
	}


	public void behave(PlayPolygon pol){


		//----------------------- boundary sticking phase

		if(boundariesPhase){
			
			int polsize = pol.getSize();
			
			Point2D next = pol.getPoint(nextChkpnt);
			
			
			if(closestPoint==null)
			{
				boundariesPhase=false;
				
				return;
			}
			
			if(location.distance(closestPoint)<=speed){
				location=closestPoint;

				boundariesPhase=false;
			}
			else if(location.distance(next)<=speed){
				
				int diff=1;
				
				if(direction)
					diff=-1;
				
				nextChkpnt=(nextChkpnt+diff+pol.getSize())%polsize;

				location=next;
			}
			else{

				Vector2D vec = new Vector2D(next.getx()-location.getx(),next.gety()-location.gety());

				vec.setLength(speed);

				location.add(vec);
			}


		}

		//----------------------- cutting phase

		if(cuttingPhase){

			Point2D nextloc= new Point2D(location);
			nextloc.add(orientation);

			if(pol.contains(nextloc)){
				location=nextloc;
			}
			else{

				Line2D traj = new Line2D(location,nextloc);

				Point2D newLoc = pol.intersectionPoint(traj);

				if(newLoc!=null)
					location=newLoc;
				
				cuttingPath.proceed(location.getx(), location.gety());
				
				cuttingPhase=false;
			}
		}


	}



	public PlayPath getCutPath(){

		if(cuttingPath==null || !cuttingPhase)
			return null;

		PlayPath getCutPathTemp = cuttingPath.clone();

		Point2D additional = new Point2D(location);
		
		Vector2D orientation2=new Vector2D(orientation.getVx(),orientation.getVy());
		
		orientation2.setLength(speed+0.1f);
		
		additional.add(orientation2);
		
		getCutPathTemp.proceed(additional.getx(), additional.gety());
		
		return getCutPathTemp;

	}


	public void startCuting(Vector2D orientation){

		if(!boundariesPhase && !cuttingPhase){

			orientation.setLength(speed);


			cuttingPhase=true;

			cuttingPath=new PlayPath();
			
			
			Point2D additional = new Point2D(location);
			
			Vector2D orientation2=new Vector2D(orientation.getVx(),orientation.getVy());
			
			orientation2.setLength(0.1f);
			
			additional.sub(orientation2);
			
			cuttingPath.start(additional.getx(), additional.gety());

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

