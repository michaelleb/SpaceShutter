package mark.geometry;


public class Point2D{

	public static class Short{


		private short x;
		private short y;

		public Short(short x,short y){
			this.x=x;
			this.y=y;
		}

		public Short(Point2D.Short other){
			this.x=other.x;
			this.y=other.y;
		}

		public short getx(){return x;}
		public short gety(){return y;}

		public void setx(short xx){
			this.x=xx;
		}
		public void sety(short yy){
			this.y=yy;
		}

		public float distance(Point2D.Short other){
			return (new Line2D.Short(this,other)).getLength();
		}

		public void add(Vector2D.Short other){
			this.x+=other.getVx();
			this.y+=other.getVy();
		}

		public void sub(Vector2D.Short other){
			this.x-=other.getVx();
			this.y-=other.getVy();
		}
		
		public boolean equals(Point2D.Short other){
			return(this.x==other.x && this.y==other.y);
		}
		
		public Vector2D.Short sub(Point2D.Short other){
			
			return new Vector2D.Short(
					(short)(this.getx()-other.getx())
					,
					(short)(this.gety()-other.gety())
					);
		}

	}
}
