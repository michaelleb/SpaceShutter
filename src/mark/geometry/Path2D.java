package mark.geometry;

import java.util.ArrayList;
import java.util.ListIterator;

import mark.geometry.Point2D.Short;






public class Path2D {


	public static class Short implements Shape2D{

		protected ArrayList<Point2D.Short> coords = new ArrayList<Point2D.Short>();

		public Short(){}

		public void start(short x, short y){

			coords.clear();

			coords.add(new Point2D.Short(x,y));

			//Log.e("",""+coords.size());
		}


		public void proceed(short x, short y){

			if(coords.size()==0)
				return;

			coords.add(new Point2D.Short(x,y));

			//Log.e("",""+coords.size());
		}

		public void pushAtFront(short x,short y){
			coords.add(0, new Point2D.Short(x,y));
		}

		public void removeLast(){
			coords.remove(coords.size()-1);
		}

		public void clear(){
			coords.clear();
		}

		public int getSize(){
			return this.coords.size();
		}

		public Point2D.Short getPoint(int index){

			Point2D.Short pt = new Point2D.Short(this.coords.get(index).getx(),this.coords.get(index).gety());

			return pt;
		}



		public Path2D.Short clone(){

			Path2D.Short path = new Path2D.Short();

			path.coords=(ArrayList<Point2D.Short>)coords.clone();

			return path;
		}







		public void reverse(){


			Path2D.Short path=new Path2D.Short();

			ListIterator<Point2D.Short> pathIter = path.coords.listIterator();

			Point2D.Short elem;

			if(pathIter.hasNext()){			

				elem = pathIter.next();

				path.pushAtFront(elem.getx(), elem.gety());
			}

			this.coords=path.coords;



		}


		public void setValue(int index,short x,short y){
			this.coords.get(index).setx(x);
			this.coords.get(index).sety(y);
		}

		public void setPoint(int index,Point2D.Short point){
			this.coords.set(index, point);
		}









		/*
		 * gets point P, returns the index of point in polygon such that the line (index,index+1) contains the point P between
		 * 
		 */
		public int getClosestPointIndex(Point2D.Short point){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			while (polyIter.hasNext()) {

				int index=polyIter.nextIndex()-1;

				polysecond = polyIter.next();

				Line2D.Short currLine = new Line2D.Short(polyfirst,polysecond);

				if(currLine.isBetween(point)){
					return index;
				}

				polyfirst=polysecond;
			}

			return -1;

		}











		public boolean isIntersection(Path2D.Short other){

			Line2D.Short line1;
			Line2D.Short line2;

			for(int i=0;i<this.getSize()-1;i++){
				for(int j=0;j<other.getSize()-1;j++){

					line1=new Line2D.Short(this.getPoint(i),this.getPoint(i+1));
					line2=new Line2D.Short(other.getPoint(j),other.getPoint(j+1));

					if(line1.lineIntersection(line2, true, true, true, true)!=null){
						return true;
					}
				}
			}


			return false;
		}


		public boolean isIntersection(Polygon2D.Short other){

			return isIntersection(((Path2D.Short)other));
		}


		public boolean isIntersection(Circle2D.Short other){return true;}

		public boolean isIntersection(Box2D.Short other){return true;}






		public Point2D.Short getNextPoint(Point2D.Short location,Point2D.Short dest){

			int index1=getClosestPointIndex(location);
			int index2=getClosestPointIndex(dest);

			if(index1<index2){

				if(this.getPoint(index1+1).equals(location))
					return this.getPoint((index1+2)%this.getSize());
				else
					return this.getPoint(index1+1);
			}
			else if(index1>index2)
				return this.getPoint(index1);
			else
				return dest;
		}

















		/*
		 * gets point P, returns closest point on some path line
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
		 * gets segment line L, returns some polygon line, that intersects segment L
		 * 
		 */
		public Line2D.Short interSectionLine(Line2D.Short line){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short polyfirst = polyIter.next();
			Point2D.Short polysecond;

			Line2D.Short res= null;
			float record = 10000;

			while (polyIter.hasNext()) {
				polysecond = polyIter.next();


				Line2D.Short polyline=new Line2D.Short(polyfirst,polysecond);

				Point2D.Short intersect = line.lineIntersection(polyline,false,false,false,false);

				if(intersect!=null){

					if(intersect.distance(line.getStart())<record){
						record=intersect.distance(line.getStart());

						res=polyline;
					}
				}

				polyfirst=polysecond;
			}

			return res;

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




		public Line2D.Short polyIntersectionLine(Path2D.Short poly){

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








		/*
		 * gets: box and vector
		 * returns: max ditance the box can go towards that vec wihout colliding to one of path's lines
		 */
		public float maxDistance(Box2D.Short box, Vector2D.Short origvec){

			ListIterator<Point2D.Short> polyIter = coords.listIterator();

			Point2D.Short pt = polyIter.next();
			Point2D.Short pt2 = pt;



			float minh=10000;
			float minw=10000;

			Point2D.Short origin=null,phoriz=null,pvert=null;

			Vector2D.Short vec = new Vector2D.Short(origvec);
			vec.setLength(10000);



			if(origvec.getVx()>=0 && origvec.getVy()>=0){
				origin = new Point2D.Short(
						(short)(box.getLocation().getx()+box.getWidth()/2),
						(short)(box.getLocation().gety()+box.getHeight()/2));
			}
			else if(origvec.getVx()>=0 && origvec.getVy()<0){
				origin = new Point2D.Short(
						(short)(box.getLocation().getx()+box.getWidth()/2),
						(short)(box.getLocation().gety()-box.getHeight()/2));

			}
			else if(origvec.getVx()<0 && origvec.getVy()<0){
				origin = new Point2D.Short(
						(short)(box.getLocation().getx()-box.getWidth()/2),
						(short)(box.getLocation().gety()-box.getHeight()/2));
			}
			else if(origvec.getVx()<0 && origvec.getVy()>=0){
				origin = new Point2D.Short(
						(short)(box.getLocation().getx()-box.getWidth()/2),
						(short)(box.getLocation().gety()+box.getHeight()/2));
			}







			Point2D.Short originend;




			originend=new Point2D.Short(origin);


			originend.add(vec);
			
			Line2D.Short originline=new Line2D.Short(origin,originend);
			
			
			//System.out.println("origin: "+origin.getx()+","+origin.gety()+"");

			//Line2D.Short line= new Line2D.Short(origin, origin2);

			//System.out.println("origin "+origin.getx()+" "+origin.gety());


			while (polyIter.hasNext()) {


				if(isInRange(origin,origvec,box.getWidth(),false,pt2)){



					float diff = Math.abs(pt2.getx()-origin.getx());

					if(minh>diff){
						minh=diff;
					}

				}

				//System.out.println("test: "+pt2.getx()+" "+pt2.gety());

				if(isInRange(origin,origvec,box.getHeight(),true,pt2)){

					float diff = Math.abs(pt2.gety()-origin.gety());

					//.out.println("yes");

					if(minw>diff){
						minw=diff;
					}

				}



				pt=pt2;
				pt2 = polyIter.next();


				//System.out.println(""+pt.getx()+","+pt.gety()+" | "+pt2.getx()+","+pt2.gety());

				Line2D.Short polyline = new Line2D.Short(pt, pt2);

				Point2D.Short pppp =  polyline.lineIntersection(originline, true, true, true, true);
				
				if(pppp!=null){
					/*
					System.out.println("polyline: "+polyline.start.getx()+","+polyline.start.gety()+" | "
							
							+polyline.end.getx()+","+polyline.end.gety());
					
					System.out.println("vertline: "+vertline.start.getx()+","+vertline.start.gety()+" | "
							
							+vertline.end.getx()+","+vertline.end.gety());

					
					
					
					
					System.out.println("test "+p22.getx()+" "+p22.gety());
					*/
					
				}

				
				if(pppp!=null){

					//System.out.println("test "+p11.getx()+" "+p11.gety());

					if(isInRange(origin,origvec,box.getWidth(),false,pppp)){

						//System.out.println("test "+p11.getx()+" "+p11.gety()+" acc");

						float diff = Math.abs(pppp.gety()-origin.gety());

						if(minh>diff){
							minh=diff;
						}

					}
				}
				if(pppp!=null){

					//System.out.println("test "+p22.getx()+" "+p22.gety());

					if(isInRange(origin,origvec,box.getHeight(),true,pppp)){

						//System.out.println("test "+p22.getx()+" "+p22.gety()+" acc");

						float diff = Math.abs(pppp.getx()-origin.getx());

						//.out.println("yes");

						if(minw>diff){
							minw=diff;
						}

					}
				}



			}



			//System.out.println(">>>"+minw+" "+minh);


			float len1=vecLen(origvec, minw, true);

			float len2=vecLen(origvec, minh, false);

			if(len1<len2)
				return len1;
			else
				return len2;
		}



		public boolean isInRange(Point2D.Short origin, Vector2D.Short dir, int size,boolean isVertical, Point2D.Short subject){




			if(isVertical){




				if(dir.getVx()!=0){

					//System.out.println(" is ver: "+isVertical+" ("+subject.getx()+","+subject.gety()+") +ox: "+origin.getx()+" oy: "+origin.gety());

					float n = ((float)origin.gety()-(float)dir.getVy()/(float)dir.getVx()*(float)origin.getx());

					float y = (float)subject.getx()*(float)dir.getVy()/(float)dir.getVx()+n;



					float foundSize=Math.abs(y-subject.gety());


					boolean res = (
							(
									(dir.getVy()>=0 && subject.gety()<=y)
									||
									(dir.getVy()<0 && subject.gety()>=y)
									)
									&&
									(
											(subject.getx() >= origin.getx() && dir.getVx()>=0)
											||
											(subject.getx() <= origin.getx() && dir.getVx()<=0)

											)


							)
							&&
							(foundSize<=size)
							;

					//if(res)
					//System.out.println("acc");

					return res;

					/*
					return (
							((y-subject.gety())<=size && (y-subject.gety())>=0 && (dir.getVy()>=0 && subject.gety()>=origin.gety()))
							||
							((subject.gety()-y)<=size && (subject.gety()-y)>=0 && (dir.getVy()<=0 && subject.gety()<=origin.gety()))
							)
							; 

					 */

				}
				else{
					return origin.gety()==subject.gety();
				}
			}

			if(!isVertical){



				if(dir.getVy()!=0){




					float n = ((float)origin.getx()-(float)dir.getVx()/(float)dir.getVy()*(float)origin.gety());

					float x = (float)subject.gety()*(float)dir.getVx()/(float)dir.getVy()+n;

					float foundSize=Math.abs(x-subject.getx());


					return(
							(
									(dir.getVx()>=0 && subject.getx()<=x)
									||
									(dir.getVx()<0 && subject.getx()>=x)
									)
									&&
									(
											(subject.gety() >= origin.gety() && dir.getVy()>=0)
											||
											(subject.gety() < origin.gety() && dir.getVy()<=0)

											)


							)
							&&
							(foundSize<=size)
							;


					/*
					return (
							((x-subject.getx())<=size && (x-subject.getx())>=0 && (dir.getVx()>=0 && subject.getx()>=origin.getx()))
							||
							((subject.getx()-x)<=size && (subject.getx()-x)>=0 && (dir.getVx()<=0 && subject.getx()<=origin.getx()))
							)
							; 


					 */

				}
				else{
					return origin.getx()==subject.getx();
				}
			}

			return false;
		}



		public float vecLen(Vector2D.Short vec, float len, boolean isX){



			Vector2D.Short newVec = new Vector2D.Short(vec);

			//System.out.println("-1-- "+newVec.getVx()+" "+newVec.getVy());

			if(isX){

				if(newVec.getVx()==0)
					return 10000;

				newVec.setVy((short)((float)newVec.getVy()/(float)newVec.getVx()*(float)len));
				newVec.setVx((short)len);

			}
			else{

				if(newVec.getVy()==0)
					return 10000;

				newVec.setVx((short)((float)newVec.getVx()/(float)newVec.getVy()*(float)len));
				newVec.setVy((short)len);
			}


			//System.out.println("-2-- "+newVec.getVx()+" "+newVec.getVy());

			return newVec.getLength();
		}



	}

}
