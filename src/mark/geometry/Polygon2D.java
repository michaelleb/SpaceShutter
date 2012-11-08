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
				
				Line2D linea=new Line2D(first,second);
				Line2D lineb=new Line2D(polyfirst,polysecond);
				
				Point2D res = linea.lineIntersection(lineb, false);
						

				if(res!=null){
					
					collisioncnt++;
					
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
		
		//pathIndexA polyIndexA
		
		Log.e("",""+pathIndexA+" "+pathIndexB+" "+polyIndexA+" "+polyIndexB);

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


			Line2D line1 = new Line2D(new Point2D(-10,-10),point);
			Line2D line2 = new Line2D(polyfirst,polysecond);


			if(line1.lineIntersection(line2, false)!=null){

				intersectCnt++;
			}

			polyfirst=polysecond;
		}

		return (intersectCnt%2)==1;

	}


	public Line2D interSectionLine(Line2D line){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		while (polyIter.hasNext()) {
			polysecond = polyIter.next();

			Point2D intersect = line.lineIntersection(new Line2D(polyfirst,polysecond),false);

			if(intersect!=null){

				return new Line2D(polyfirst,polysecond);
			}

			polyfirst=polysecond;
		}

		return null;

	}

	public Point2D intersectionPoint(Line2D line){

		Line2D iline = this.interSectionLine(line);

		if(iline!=null)
			return iline.lineIntersection(line, true);
		return null;

	}
	
	
	
	
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
			
			
			Point2D interPta = currLine.lineIntersection(linea, true);
			Point2D interPtb = currLine.lineIntersection(lineb, true);
			
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getLineWithPointIndex(Point2D point){

		ListIterator<Point2D> polyIter = coords.listIterator();

		Point2D polyfirst = polyIter.next();
		Point2D polysecond;

		while (polyIter.hasNext()) {
			
			int index=polyIter.nextIndex();
			
			polysecond = polyIter.next();

			Line2D currLine = new Line2D(polyfirst,polysecond);
			
			if(currLine.isBetween(point)){
				return index;
			}
			
			polyfirst=polysecond;
		}

		return -1;

	}
	
	
	
	
	
	
	
	
	
	
	public void print(){
		Log.e("-------------------------------------","");
		
		
		for(int i=0;i<=this.getSize();i++){
			
			
			
			int ii=i%this.getSize();
			
			Point2D pp = this.getPoint(ii);
			
			Log.e("point:"+ii,"("+pp.getx()+","+pp.gety()+")");
			
		}
		
		Log.e("-------------------------------------","");
		
	}

}
