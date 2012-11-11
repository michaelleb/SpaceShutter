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
		
		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;
		
		Point2D start = path.getPoint(0);
		Point2D start2 = path.getPoint(1);
		
		Line2D startLine = new Line2D(start,start2);
		
		Point2D end = path.getPoint(path.getSize()-1);
		
		
		int indexa=-1;
		int indexb=-1;
		
		while (polyIter.hasNext()) {
			
			int index=polyIter.nextIndex()-1;

			polysecond = polyIter.next();

			//--------------------------------------------
			
			Line2D polyLine=new Line2D(polyfirst,polysecond);
			
			
			if(polyLine.isBetween(start)){
				indexa=index;
			}
			
			if(polyLine.isBetween(end)){
				indexb=index;
			}
			
			//--------------------------------------------

			polyfirst=polysecond;
		}
		
		//Log.e("",""+indexa+"___"+indexb);
		
		if(indexa==-1 || indexb==-1)
			return false;
		
		if(indexb<indexa){
			Collections.reverse(path.coords);
			
			int tmp=indexa;
			indexa=indexb;
			indexb=tmp;
			
		}
		
		if(indexb==indexa){
			Point2D pta=this.coords.get(indexa);
			Point2D ptb=path.coords.get(0);
			Point2D ptc=path.coords.get(path.coords.size()-1);

			if(pta.distance(ptb)>pta.distance(ptc))
				Collections.reverse(path.coords);
		}
		
		Point2D firstptadded=null;

		ArrayList<Point2D> coords1 = new ArrayList<Point2D>();
		ArrayList<Point2D> coords2 = new ArrayList<Point2D>();


		for(int i=0;i<this.coords.size();i++){

			if(i<=indexa || i>indexb){
				coords1.add(this.coords.get(i));
			}
			else{
				coords2.add(this.coords.get(i));

				if(firstptadded==null)
					firstptadded=this.coords.get(i);
			}

			if(i==indexa){
				for(int j=0;j<path.coords.size();j++){
					coords1.add(path.coords.get(j));
					coords2.add(path.coords.get(path.coords.size()-j-1));
					
					if(firstptadded==null)
						firstptadded=path.coords.get(path.coords.size()-j-1);
				}
			}



		}
		
		coords2.add(firstptadded);

		if(sideA!=null)
			sideA.coords=coords1;

		if(sideB!=null)
			sideB.coords=coords2;

		


		return true;
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

	/*
	 * 
	 */
	public boolean contains(Point2D point){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		int intersectCnt=0;

		while (polyIter.hasNext()) {

			polysecond = polyIter.next();


			Line2D line1 = new Line2D(new Point2D(-1,-1),point);
			Line2D line2 = new Line2D(polyfirst,polysecond);

			
			if(line2.isBetween(point))
				return true;
			
			if(line1.lineIntersection(line2, false,false,false,true)!=null){

				intersectCnt++;
			}

			polyfirst=polysecond;
		}

		//log.e("",""+intersectCnt);

		return (intersectCnt%2)==1;

	}

	/*
	 * gets segment line L, returns some polygon line, that intersects segment L
	 * 
	 */
	public Line2D interSectionLine(Line2D line){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		while (polyIter.hasNext()) {
			polysecond = polyIter.next();

			Point2D intersect = line.lineIntersection(new Line2D(polyfirst,polysecond),false,false,false,false);

			if(intersect!=null){

				return new Line2D(polyfirst,polysecond);
			}

			polyfirst=polysecond;
		}

		return null;

	}



	/*
	 * gets segment line L, returns intersection point of L with polygon
	 * 
	 */
	public Point2D intersectionPoint(Line2D line){

		Line2D iline = this.interSectionLine(line);

		if(iline!=null)
			return iline.lineIntersection(line, true,true,true,true);
		return null;

	}



	/*
	 * gets point P, returns closest point on some polygon line
	 * 
	 */
	public Point2D closestPointSimplified(Point2D point){

		Point2D result=null;

		Line2D linea=new Line2D(
				new Point2D(point.getx(),point.gety()-1000),
				new Point2D(point.getx(),point.gety()+1000)
				);

		Line2D lineb=new Line2D(
				new Point2D(point.getx()-1000,point.gety()),
				new Point2D(point.getx()+1000,point.gety())
				);

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		while (polyIter.hasNext()) {
			polysecond = polyIter.next();


			Line2D currLine = new Line2D(polyfirst,polysecond);


			Point2D interPta = currLine.lineIntersection(linea, true,true,true,true);
			Point2D interPtb = currLine.lineIntersection(lineb, true,true,true,true);

			if(interPta!=null){
				if(result==null || (result.distance(point)>interPta.distance(point)))
					result=interPta;
			}

			if(interPtb!=null){
				if(result==null || (result.distance(point)>interPtb.distance(point)))
					result=interPtb;
			}

			polyfirst=polysecond;
		}

		return result;

	}



	/*
	 * gets point P, returns the index of point in polygon such that the line (index,index+1) contains the point P between
	 * 
	 */
	public int getLineWithPointIndex(Point2D point){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;
		
		int res=-1;
		
		while (polyIter.hasNext()) {

			int index=polyIter.nextIndex()-1;

			polysecond = polyIter.next();

			Line2D currLine = new Line2D(polyfirst,polysecond);

			if(currLine.isBetween(point)){
				res= index;
			}

			polyfirst=polysecond;
		}

		return res;

	}





	public void setCenter(float x,float y){
		
		
		ListIterator<Point2D> polyIter = coords.listIterator();
		
		float cx=0;
		float cy=0;
		
		while (polyIter.hasNext()) {
			Point2D polyfirst = polyIter.next();
			
			cx+=polyfirst.getx();
			cy+=polyfirst.gety();
		}
		
		cx/=coords.size();
		cy/=coords.size();
		
		
		cx=-cx+x;
		cy=-cy+y;
		
		ListIterator<Point2D> polyIter2 = coords.listIterator();
		
		while (polyIter.hasNext()) {
			Point2D polyfirst = polyIter.next();
			
			cx+=polyfirst.getx();
			cy+=polyfirst.gety();
		}
		
	}

	
	
	
	public Line2D polyIntersectionLine(Polygon2D poly){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		while (polyIter.hasNext()) {
			polysecond = polyIter.next();

			{
				ListIterator<Point2D> secondPolyIter = poly.coords.listIterator();

				Point2D secondpolyfirst = secondPolyIter.next();
				Point2D secondpolysecond;

				while (secondPolyIter.hasNext()) {
					secondpolysecond = secondPolyIter.next();

					{
						Point2D intersect = (new Line2D(polyfirst,polysecond)).lineIntersection(new Line2D(secondpolyfirst,secondpolysecond),true,true,true,true);

						if(intersect!=null){
							return new Line2D(polyfirst,polysecond);
						}
					}

					secondpolyfirst=secondpolysecond;
				}
			}

			polyfirst=polysecond;
		}

		return null;

	}
	
	
	
	
	
	
	
	
	
	
	


	public void print(){
		//log.e("-------------------------------------","");


		for(int i=0;i<=this.getSize();i++){



			int ii=i%this.getSize();

			Point2D pp = this.getPoint(ii);

			//log.e("point:"+ii,"("+pp.getx()+","+pp.gety()+")");

		}

		//log.e("-------------------------------------","");

	}

}
