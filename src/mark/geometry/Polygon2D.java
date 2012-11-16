package mark.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import android.util.Log;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Polygon2D extends Path2D{

	public static class Short extends Path2D.Short{

		public Short(){

		}
		
		public void setPoly(Polygon2D.Short poly){
			this.coords=poly.coords;
		}

		public boolean cut(Path2D.Short cuttingPath,Polygon2D.Short sideA,Polygon2D.Short sideB){

			Path2D.Short path =  cuttingPath.clone();

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			Point2D.Short start = path.getPoint(0);
			Point2D.Short start2 = path.getPoint(1);

			Line2D.Short startLine = new Line2D.Short(start,start2);

			Point2D.Short end = path.getPoint(path.getSize()-1);


			int indexa=-1;
			int indexb=-1;

			while (polyIter.hasNext()) {

				int index=polyIter.nextIndex()-1;

				polysecond = polyIter.next();

				//--------------------------------------------

				Line2D.Short polyLine=new Line2D.Short(polyfirst,polysecond);


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
				Point2D.Short pta=this.coords.get(indexa);
				Point2D.Short ptb=path.coords.get(0);
				Point2D.Short ptc=path.coords.get(path.coords.size()-1);

				if(pta.distance(ptb)>pta.distance(ptc))
					Collections.reverse(path.coords);
			}

			Point2D.Short firstptadded=null;

			ArrayList<Point2D.Short> coords1 = new ArrayList<Point2D.Short>();
			ArrayList<Point2D.Short> coords2 = new ArrayList<Point2D.Short>();


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
		
		
		/*
		 * 
		 */
		public boolean contains(Point2D.Short point){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			int intersectCnt=0;

			while (polyIter.hasNext()) {

				polysecond = polyIter.next();


				Line2D.Short line1 = new Line2D.Short(new Point2D.Short((short)-1,(short)10000),point);
				Line2D.Short line2 = new Line2D.Short(polyfirst,polysecond);


				if(line2.isBetween(point)){
					
					//Log.e("","BETWEEN");
					
					return true;
					
				}

				if(line1.lineIntersection(line2, false,false,false,true)!=null){

					intersectCnt++;
				}

				polyfirst=polysecond;
			}

			//Log.e(">>>>",""+(intersectCnt));

			return (intersectCnt%2)==1;

		}

		/*
		 * gets segment line L, returns some polygon line, that intersects segment L
		 * 
		 */
		public Line2D.Short interSectionLine(Line2D.Short line){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			while (polyIter.hasNext()) {
				polysecond = polyIter.next();

				Point2D.Short intersect = line.lineIntersection(new Line2D.Short(polyfirst,polysecond),false,false,false,false);

				if(intersect!=null){

					return new Line2D.Short(polyfirst,polysecond);
				}

				polyfirst=polysecond;
			}

			return null;

		}



		/*
		 * gets segment line L, returns intersection point of L with polygon
		 * 
		 */
		public Point2D.Short intersectionPoint(Line2D.Short line){

			Line2D.Short iline = this.interSectionLine(line);

			if(iline!=null)
				return iline.lineIntersection(line, true,true,true,true);
			return null;

		}



		/*
		 * gets point P, returns closest point on some polygon line
		 * 
		 */
		public Point2D.Short closestPointSimplified(Point2D.Short point){

			Point2D.Short result=null;

			Line2D.Short linea=new Line2D.Short(
					new Point2D.Short(point.getx(),(short)(point.gety()-1000)
							),
					new Point2D.Short(
							point.getx(),(short)(point.gety()+1000)
							)
					);

			Line2D.Short lineb=new Line2D.Short(
					new Point2D.Short(
							(short)(point.getx()-1000),point.gety()
							),
					new Point2D.Short(
							(short)(point.getx()+1000),point.gety()
							)
					);

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			while (polyIter.hasNext()) {
				polysecond = polyIter.next();


				Line2D.Short currLine = new Line2D.Short(polyfirst,polysecond);


				Point2D.Short interPta = currLine.lineIntersection(linea, true,true,true,true);
				Point2D.Short interPtb = currLine.lineIntersection(lineb, true,true,true,true);

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
		public int getLineWithPointIndex(Point2D.Short point){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			int res=-1;

			while (polyIter.hasNext()) {

				int index=polyIter.nextIndex()-1;

				polysecond = polyIter.next();

				Line2D.Short currLine = new Line2D.Short(polyfirst,polysecond);

				if(currLine.isBetween(point)){
					res= index;
				}

				polyfirst=polysecond;
			}

			return res;

		}




		public Line2D.Short polyIntersectionLine(Polygon2D.Short poly){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			while (polyIter.hasNext()) {
				polysecond = polyIter.next();

				{
					ListIterator<Point2D.Short> secondPolyIter = poly.coords.listIterator();

					Point2D.Short secondpolyfirst = secondPolyIter.next();
					Point2D.Short secondpolysecond;

					while (secondPolyIter.hasNext()) {
						secondpolysecond = secondPolyIter.next();

						{
							Point2D.Short intersect = (new Line2D.Short(polyfirst,polysecond)).lineIntersection(new Line2D.Short(secondpolyfirst,secondpolysecond),true,true,true,true);

							if(intersect!=null){
								return new Line2D.Short(polyfirst,polysecond);
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

				Point2D.Short pp = this.getPoint(ii);

				//log.e("point:"+ii,"("+pp.getx()+","+pp.gety()+")");

			}

			//log.e("-------------------------------------","");

		}

	}

}
