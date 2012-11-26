package mark.geometry;

public class Circle2D {

	public static class Short implements Shape2D{


		Point2D.Short location;
		short radius;

		public Short(Point2D.Short location,short radius){

			this.location=location;
			this.radius=radius;
		}
		
		public boolean isIntersection(Polygon2D.Short other){return true;}
		public boolean isIntersection(Circle2D.Short other){return true;}
		public boolean isIntersection(Path2D.Short other){return true;}
	}
}
