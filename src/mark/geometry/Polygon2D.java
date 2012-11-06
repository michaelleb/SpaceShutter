package mark.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import android.util.Log;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Polygon2D extends Path2D {

	public Polygon2D(){

	}

	public boolean cut(Path2D cuttingPath,Polygon2D sideA,Polygon2D sideB){

		Path2D path =  cuttingPath.clone();

		ListIterator<Point2D> pathIter = path.coords.listIterator();

		Point2D firstptadded=null;
		Point2D firstptadded2=null;

		Point2D first = pathIter.next();
		Point2D second;

		int pathIndexA=-1,pathIndexB=-1;

		int polyIndexA=-1,polyIndexB=-1;


		int collisioncnt=0;

		while (pathIter.hasNext()) {

			second = pathIter.next();

			ListIterator<Point2D> polyIter = coords.listIterator();

			Point2D polyfirst = polyIter.next();
			Point2D polysecond;

			while (polyIter.hasNext()) {
				polysecond = polyIter.next();

				//--------------------------------------------

				Point2D res = segIntersection(
						first.getx(),first.gety(),second.getx(),second.gety(),
						polyfirst.getx(),polyfirst.gety(),polysecond.getx(),polysecond.gety()
						,false);

				if(res!=null){

					collisioncnt++;

					Log.i(""," path: "+(pathIter.nextIndex()-2)+" poly: "+(polyIter.nextIndex()-2));

					if(pathIndexA==-1){
						pathIndexA=pathIter.nextIndex()-2;
						polyIndexA=polyIter.nextIndex()-2;

						pathIter.previous();
						pathIter.previous();
						pathIter.set(res);
						pathIter.next();
						pathIter.next();

					}
					else{
						pathIndexB=pathIter.nextIndex()-2;
						polyIndexB=polyIter.nextIndex()-2;


						pathIter.previous();
						pathIter.set(res);
						pathIter.next();

					}

				}

				//--------------------------------------------

				polyfirst=polysecond;
			}


			//-------------------------------------------------	

			first = second; 
		}

		Log.e("",""+collisioncnt);

		if(collisioncnt!=2)
			return false;

		for(int i=path.coords.size()-1;i>pathIndexB+1;i--)
			path.coords.remove(i);

		for(int i=0;i<pathIndexA;i++)
			path.coords.remove(0);

		if(polyIndexB<polyIndexA){
			int tmp=polyIndexA;
			polyIndexA=polyIndexB;
			polyIndexB=tmp;
			Collections.reverse(path.coords);
		}


		if(polyIndexB==polyIndexA){

			Point2D pta=this.coords.get(polyIndexA);
			Point2D ptb=path.coords.get(0);
			Point2D ptc=path.coords.get(path.coords.size()-1);

			if(pta.distance(ptb)>pta.distance(ptc))
				Collections.reverse(path.coords);
		}


		ArrayList<Point2D> coords1 = new ArrayList<Point2D>();
		ArrayList<Point2D> coords2 = new ArrayList<Point2D>();


		for(int i=0;i<this.coords.size();i++){

			if(i<=polyIndexA || i>polyIndexB){
				coords1.add(this.coords.get(i));

				if(firstptadded==null)
					firstptadded=this.coords.get(i);
			}
			else{
				coords2.add(this.coords.get(i));

				if(firstptadded2==null)
					firstptadded2=this.coords.get(i);
			}

			if(i==polyIndexA){
				for(int j=0;j<path.coords.size();j++){
					coords1.add(path.coords.get(j));
					coords2.add(path.coords.get(path.coords.size()-j-1));

					if(firstptadded==null)
						firstptadded=path.coords.get(j);

					if(firstptadded2==null)
						firstptadded2=path.coords.get(path.coords.size()-j-1);
				}
			}



		}
		coords1.add(firstptadded);
		coords2.add(firstptadded2);

		if(sideA!=null)
			sideA.coords=coords1;

		if(sideB!=null)
			sideB.coords=coords2;

		return true;
	}

	protected Point2D segIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4,boolean includeEdges) 
	{ 
		float bx = x2 - x1; 
		float by = y2 - y1; 
		float dx = x4 - x3; 
		float dy = y4 - y3;
		float b_dot_d_perp = bx * dy - by * dx;
		if(b_dot_d_perp == 0) {
			return null;
		}
		float cx = x3 - x1;
		float cy = y3 - y1;
		float t = (cx * dy - cy * dx) / b_dot_d_perp;
		if(
		(!includeEdges && (t <= 0 || t >= 1)) 
		||
		(includeEdges && (t < 0 || t > 1))
		) {
			return null;
		}
		float u = (cx * by - cy * bx) / b_dot_d_perp;
		if(
				(!includeEdges && (u <= 0 || u >= 1)) 
				||
				(includeEdges && (u < 0 || u > 1))
				) { 
			return null;
		}
		return new Point2D(x1+t*bx, y1+t*by);
	}



	public float getArea(){

		float res=0;

		int len = coords.size();

		for(int i=0;i<len;i++)
		{
			float x1,x2,y1,y2;

			x1=coords.get(i).getx();
			x2=coords.get((i+1)%len).getx();
			y1=coords.get(i).gety();
			y2=coords.get((i+1)%len).gety();

			res+=(x1*y2-x2*y1);
		}

		res/=2;

		return res;
	}


	public boolean contains(Point2D point){
		
		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;
		
		int intersectCnt=0;
		
		while (polyIter.hasNext()) {
			polysecond = polyIter.next();
			
			if(segIntersection(-10,-10,point.getx(),point.gety(),
					polyfirst.getx(),polyfirst.gety(),polysecond.getx(),polysecond.gety(),
					false
					)!=null){
				
				intersectCnt++;
			}
			
			polyfirst=polysecond;
		}
		
		return (intersectCnt%2)==1;

	}
	
	
	public Point2D lineIntersects(Line2D line){
		
		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;
		
		while (polyIter.hasNext()) {
			polysecond = polyIter.next();
			
			Point2D intersect =segIntersection(
					line.getStart().getx(),line.getStart().gety(),
					line.getEnd().getx(),line.getEnd().gety(),
					polyfirst.getx(),polyfirst.gety(),
					polysecond.getx(),polysecond.gety(),
					false
					);
			
			if(intersect!=null){
				
				return intersect;
			}
			
			polyfirst=polysecond;
		}
		
		return null;
		
	}

}
