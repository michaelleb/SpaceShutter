package mark.geometry;

import java.util.ArrayList;
import java.util.ListIterator;

import android.util.Log;




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





		public void print(){
			Log.e("","-------------------------------------");


			for(int i=0;i<this.getSize();i++){

				Point2D.Short pp = this.getPoint(i);

				Log.e("point:"+i,"("+pp.getx()+","+pp.gety()+")");

			}

			Log.e("","-------------------------------------");

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






	}

}
