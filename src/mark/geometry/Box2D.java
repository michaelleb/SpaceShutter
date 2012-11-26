package mark.geometry;

public class Box2D {

	public static class Short implements Shape2D{


		private Point2D.Short location;
		public Point2D.Short getLocation() {
			return location;
		}



		public void setLocation(Point2D.Short location) {
			this.location = location;
		}



		public short getHeight() {
			return height;
		}



		public void setHeight(short height) {
			this.height = height;
		}



		public short getWidth() {
			return width;
		}



		public void setWidth(short width) {
			this.width = width;
		}
		private short height;
		private short width;

		public Short(Point2D.Short location,short w,short h){

			this.location=location;
			this.height=h;
			this.width=w;
		}
		
		public boolean isIntersection(Shape2D other){return true;}
		
		public boolean isIntersection(Polygon2D.Short other){return true;}
		public boolean isIntersection(Circle2D.Short other){return true;}
		public boolean isIntersection(Path2D.Short other){return true;}
		
		public boolean isIntersection(Box2D.Short other){return true;}
	}
}
