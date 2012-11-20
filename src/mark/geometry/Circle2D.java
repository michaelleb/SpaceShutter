package mark.geometry;

public class Circle2D {

	public static class Short implements Shape2D{


		Point2D.Short location;
		short radius;

		public Short(Point2D.Short location,short radius){

			this.location=location;
			this.radius=radius;
		}

		public boolean isCollision(Polygon2D.Short other){
			return false;
		}

		public boolean isCollision(Circle2D.Short other){
			return false;
		}
	}
}
