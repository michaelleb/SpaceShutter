package mark.geometry;

public class Vector2D {


	public static class Short{

		private short vx;
		private short vy;


		public Short(short vvx,short vvy){
			this.vx=vvx;
			this.vy=vvy;
		}
		
		public Short(Short other){
			this.vx=other.vx;
			this.vy=other.vy;
		}
		
		public void setLength(float newLength){

			float len=getLength();

			vx=(short)(vx/len*newLength);
			vy=(short)(vy/len*newLength);

		}

		public float getLength(){

			return (float)Math.sqrt(this.vx*this.vx+this.vy*this.vy);
		}

		public short getVx(){return this.vx;}
		public short getVy(){return this.vy;}

		public void setVx(short vx){this.vx=vx;}
		public void setVy(short vy){this.vy=vy;}

	}

}
