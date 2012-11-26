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
	
	private float len=-1;
	
	public void behave(MyEnvironment env){
		
		if(len<orientation.getLength()){
			
			
			if(len==-1){
				len = env.myPoly.maxDistance(body, orientation);
				return;
			}
			
			Vector2D.Short orientttt = new Vector2D.Short(orientation);
			orientttt.setLength(len);
			
			body.getLocation().add(orientttt);
			
			
			
			Vector2D.Short orient1 = new Vector2D.Short(orientation);
			
			Vector2D.Short orient2 = new Vector2D.Short(orientation);
			
			
			orient1.setVx((short)-orient1.getVx());
			orient2.setVy((short)-orient2.getVy());
			
			
			
			
			if(env.myPoly.maxDistance(body, orient1)>orient1.getLength() && env.myPoly.maxDistance(body, orient1)<10000){
				orientation=orient1;
			}
			else if(env.myPoly.maxDistance(body, orient2)>orient2.getLength() && env.myPoly.maxDistance(body, orient2)<10000){
				orientation=orient2;
			}
			else{
				orientation.setVx((short)-orientation.getVx());
				orientation.setVy((short)-orientation.getVy());
			}
			
			len = env.myPoly.maxDistance(body, orientation);
			
			
			
			Log.e("","loc: x:"+body.getLocation().getx()+" y:"+body.getLocation().gety()+" len: "+len);
			
			
			//body.getLocation().add(orientttt);
			
			
			//Vector2D.Short orient = new Vector2D.Short(orientation);
			
			//body.getLocation().add(orient);
			
			
			
			
			
			/*
			if(nextVec==null)
				nextVec=new Vector2D.Short(orientation);
			else
				orientation=new Vector2D.Short(nextVec);
			
			float aa = env.myPoly.maxDistance(body, nextVec);
			
			if(aa<=0)
				nextVec.setVx((short)-nextVec.getVx());
			else
				nextVec.setVy((short)-nextVec.getVy());
			
			if(aa<0) aa*=-1;
			
			Log.e("","body: "+body.getLocation().getx()+" "+body.getLocation().gety()
					+" \n curr orient"+orientation.getVx()+" "+orientation.getVy()
					+" \n next orient "+nextVec.getVx()+" "+nextVec.getVy()+" length:"+aa);
					
			len=aa;
			
			
			*/
			
	
		}
		else{
			len-=orientation.getLength();
			
			body.getLocation().add(orientation);
		}
		
		
		
		
		
		
		/*
		 * 
		 * 
		
		System.out.println("--: "+aa);

		Point2D.Short nextloc= new Point2D.Short(body.getLocation());
		nextloc.add(orientation);

		if(aa<orientation.getLength()*2){

			Vector2D.Short orient1 = new Vector2D.Short(orientation);

			Vector2D.Short orient2 = new Vector2D.Short(orientation);

			orient1.setVx((short)(-orient1.getVx()));

			orient2.setVy((short)(-orient2.getVy()));

			Point2D.Short reserve = new Point2D.Short(body.getLocation());


			body.getLocation().add(orient1);


			float bb = env.myPoly.maxDistance(body, orient1);

			if(bb<orient1.getLength()*2){
				body.setLocation(reserve);
				body.getLocation().add(orient2);

				bb = env.myPoly.maxDistance(body, orient2);





				if(bb<orient2.getLength()*2){

					orientation.setVx((short)(-orientation.getVx()));

					orientation.setVy((short)(-orientation.getVy()));

				}
				else{



					orientation = orient2;
				}




			}
			else{
				orientation = orient1;
			}

			body.setLocation(reserve);





		}
		else{
			body.setLocation(nextloc);
		}
		*/
		/*

		Point2D.Short nextloc = new Point2D.Short(body.getLocation());
		nextloc.add(orientation);

		Vector2D.Short dir = new Vector2D.Short(orientation);

		//body.setLocation(nextloc);

		//if(!env.myPoly.isIntersection(body)){
		if(env.myPoly.contains(nextloc)){
			body.setLocation(nextloc);
		}
		else{

			//body.setCenter(location);

			//float max = env.myPoly.maxDistance(body,orientation);

			//dir.setLength(max);

			//location.add(dir);

			notifyCollision(env.myPoly);

		}

		//body.setCenter(location);



		if(this.isBodyIntersection(env.myPath)){

			if(env.chasingPath.getSize()==0){

				Point2D.Short first = env.myPath.closestPointSimplified(body.getLocation());


				if(first!=null)
					env.chasingPath.start(first.getx(), first.gety());

			}

		}

		 */

	}

	public void setOrientation(Vector2D.Short orient){
		orientation=orient;
		//orientation.setLength(speed);

		//TODO: calc .distToWall
	}





	public Point2D.Short getLocation(){return new Point2D.Short(body.getLocation());}

	public void setLocation(Point2D.Short loc){body.setLocation(loc);}




	public void notifyCollision(GameObject object){

		/*
		Vector2D.Short orient1 = new Vector2D.Short(orientation);

		Vector2D.Short orient2 = new Vector2D.Short(orientation);

		orient1.setVx((short)(-orient1.getVx()));

		orient2.setVy((short)(-orient2.getVy()));

		boolean set=false;

		Point2D.Short location=body.getLocation();

		location.add(orient1);

		body.setLocation(location);

		if(!object.isBodyIntersection(body)){
			orientation=orient1;
			set=true;
		}

		location.sub(orient1);

		location.add(orient2);
		body.setLocation(location);

		if(!object.isBodyIntersection(body)){
			orientation=orient2;
			set=true;
		}

		location.sub(orient2);

		body.setLocation(location);

		if(set==false){
			orientation=new Vector2D.Short(
					(short)-orientation.getVx(),
					(short)-orientation.getVy());
		}

		 */

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
