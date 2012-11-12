package mark.geometry;

import java.util.ArrayList;
import java.util.ListIterator;



import android.util.Log;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


public class Path2D {


	public static class Short{

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

		public Path getPath(){
			Path path=new Path();

			ListIterator<Point2D.Short> pathIter = this.coords.listIterator();

			Point2D.Short elem;

			if(pathIter.hasNext()){			

				elem = pathIter.next();
				path.moveTo(elem.getx(), elem.gety());

				while (pathIter.hasNext()) {
					elem = pathIter.next();

					path.lineTo(elem.getx(), elem.gety());
				}	
			}

			return path;
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

	}

}
