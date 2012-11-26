package mark.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import mark.geometry.Vector2D.Short;


public class Polygon2D extends Path2D{

	public static class Short extends Path2D.Short implements Shape2D{

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

		

		public Polygon2D.Short clone(){
			Polygon2D.Short path = new Polygon2D.Short();
			path.coords=(ArrayList<Point2D.Short>)coords.clone();
			return path;
		}

		public void add(Vector2D.Short vec){
			for(int i=0;i<this.getSize();i++){
				Point2D.Short pt = this.getPoint(i);
				pt.add(vec);
				this.setPoint(i,pt);
			}
		}

		public void sub(Vector2D.Short vec){
			for(int i=0;i<this.getSize();i++){
				Point2D.Short pt = this.getPoint(i);
				pt.sub(vec);
				this.setPoint(i,pt);
			}
		}

		public void setCenter(Point2D.Short center){


			Point2D.Short currCenter = getCenter();

			currCenter.setx((short)(currCenter.getx()-center.getx()));
			currCenter.sety((short)(currCenter.gety()-center.gety()));

			for(int i=0;i<this.getSize();i++){


				//currCenter.sub(new Vector2D.Short(currCenter.getx(),currCenter.gety()));

				Point2D.Short newPoint = this.getPoint(i);
				newPoint.sub(new Vector2D.Short(currCenter.getx(),currCenter.gety()));

				this.setPoint(i, newPoint);
			}

		}


		public Point2D.Short getCenter(){
			Point2D.Short currCenter= new Point2D.Short((short)0,(short)0);

			for(int i=0;i<this.getSize()-1;i++){
				Point2D.Short pt = this.getPoint(i);
				currCenter.add(new Vector2D.Short(pt.getx(),pt.gety()));
			}

			currCenter.setx((short)(currCenter.getx()/(this.getSize()-1)));
			currCenter.sety((short)(currCenter.gety()/(this.getSize()-1)));

			return currCenter;
		}

		public boolean isIntersection(Polygon2D.Short other){

			return super.isIntersection(other);
		}

		public boolean isIntersection(Circle2D.Short other){return true;}
		
		public boolean isIntersection(Box2D.Short other){return true;}
		
		public Point2D.Short getNextPoint(Point2D.Short location,Point2D.Short dest,boolean dir){


			int index1=getClosestPointIndex(location);
			int index2=getClosestPointIndex(dest);

			if(index1==-1 || index2==-1)
				return null;
			
			if((index1<index2 && dir) || (index1>index2 && !dir)){
				return getNextPoint(location,dest);
			}
			else if(index1>index2 && dir){

				if(index1<this.getSize()){
					if(this.getPoint((index1+1)%this.getSize()).equals(location))
						return this.getPoint((index1+2)%this.getSize());
					return this.getPoint((index1+1)%this.getSize());
				}
			}
			else if(index1<index2 && !dir){
				if(location.equals(this.getPoint(0)))
					return this.getPoint(this.getSize()-2);
				return this.getPoint((this.getSize()+index1)%this.getSize());
			}
			else if(index1==index2){
				Line2D.Short line1 = new Line2D.Short(this.getPoint(index1),dest);
				Line2D.Short line2 = new Line2D.Short(dest,this.getPoint((index1+1)%this.getSize()));
				
				if((line1.isBetween(location) && dir) || (line2.isBetween(location) && !dir))
					return dest;
				else{
					if(dir)
						if(this.getPoint((index1+1)%this.getSize()).equals(location))
							return this.getPoint((index1+2)%this.getSize());
						else
							return this.getPoint((index1+1)%this.getSize());
					else
						return this.getPoint(index1);
				}
			}

			return getNextPoint(location,location);
		}
		


	}


}
