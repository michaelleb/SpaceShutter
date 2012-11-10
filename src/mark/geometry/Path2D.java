package mark.geometry;

import java.util.ArrayList;
import java.util.ListIterator;



import android.util.Log;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


public class Path2D {

	protected ArrayList<Point2D> coords = new ArrayList<Point2D>();

	public Path2D(){

	}

	public void start(float x, float y){

		coords.clear();

		coords.add(new Point2D(x,y));

		//Log.e("",""+coords.size());
	}


	public void proceed(float x, float y){

		if(coords.size()==0)
			return;
		
		coords.add(new Point2D(x,y));

		//Log.e("",""+coords.size());
	}
	
	public void pushAtFront(float x,float y){
		coords.add(0, new Point2D(x,y));
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
	
	public Point2D getPoint(int index){
		
		Point2D pt = new Point2D(this.coords.get(index).getx(),this.coords.get(index).gety());
		
		return pt;
	}
	
	public Path getPath(){
		Path path=new Path();

		ListIterator<Point2D> pathIter = this.coords.listIterator();

		Point2D elem;

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
	
	public Path2D clone(){
		
		Path2D path = new Path2D();
		
		path.coords=(ArrayList<Point2D>)coords.clone();
		
		return path;
	}

	
	
	
	
	public void print(){
		Log.e("","-------------------------------------");
		
		
		for(int i=0;i<this.getSize();i++){
			
			Point2D pp = this.getPoint(i);
			
			Log.e("point:"+i,"("+pp.getx()+","+pp.gety()+")");
			
		}
		
		Log.e("","-------------------------------------");
		
	}
	
	
	
	
	public void reverse(){
		
		
		Path2D path=new Path2D();

		ListIterator<Point2D> pathIter = path.coords.listIterator();

		Point2D elem;

		if(pathIter.hasNext()){			

			elem = pathIter.next();
			
			path.pushAtFront(elem.getx(), elem.gety());
		}
		
		this.coords=path.coords;
		
		
		
	}
	
}
