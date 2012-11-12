package mark.geometry;

public class Line2D {


	public static class Short{

		Point2D.Short start;
		Point2D.Short end;

		public Short(Point2D.Short start,Point2D.Short end){

			this.start=start;
			this.end=end;
		}

		public float getLength(){
			return getVector().getLength();
		}

		public Point2D.Short getStart(){
			return start;
		}

		public Point2D.Short getEnd(){
			return end;
		}

		public Vector2D.Short getVectorWidthLen(short newLen){

			Vector2D.Short res = getVector();

			res.setLength(newLen);

			return res;
		}

		public Vector2D.Short getVector(){

			Vector2D.Short res = new Vector2D.Short((short)(end.getx()-start.getx()),(short)(end.gety()-start.gety()));

			return res;
		}









		public Point2D.Short lineIntersection(Line2D.Short line2,boolean left1,boolean right1,boolean left2,boolean right2){
			return segIntersection(
					this.getStart().getx(),this.getStart().gety(),
					this.getEnd().getx(),this.getEnd().gety(),
					line2.getStart().getx(),line2.getStart().gety(),
					line2.getEnd().getx(),line2.getEnd().gety(),
					left1,right1,left2,right2
					);
		}




		protected Point2D.Short segIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, 
				boolean includeLeftEdge1, boolean includeRightEdge1,
				boolean includeLeftEdge2, boolean includeRightEdge2) 
		{ 
			float bx = x2 - x1; 
			float by = y2 - y1; 
			float dx = x4 - x3; 
			float dy = y4 - y3;
			float b_dot_d_perp = bx * dy - by * dx;
			if(b_dot_d_perp == 0) {
				return null;
			}
			float cx = x3 - x1;
			float cy = y3 - y1;
			float t = (cx * dy - cy * dx) / b_dot_d_perp;

			if(
					(t < 0 || (t <= 0  && !includeLeftEdge1))
					||
					(t > 1 || (t >= 1 && !includeRightEdge1))
					){
				return null;
			}
			float u = (cx * by - cy * bx) / b_dot_d_perp;
			if(
					(u < 0 || (u <= 0  && !includeLeftEdge2))
					||
					(u > 1 || (u >= 1 && !includeRightEdge2))
					){ 
				return null;
			}
			return new Point2D.Short((short)(x1+t*bx), (short)(y1+t*by));
		}


		public boolean isBetween(Point2D.Short point){

			float wholelen = getLength();

			float len1 = (new Line2D.Short(start,point)).getLength();
			float len2 = (new Line2D.Short(end,point)).getLength();

			return (len1+len2==wholelen);
		}

	}

}
