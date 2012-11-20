package mark.geometry;

public interface Shape2D {
	
	public boolean isCollision(Circle2D.Short other);
	
	public boolean isCollision(Polygon2D.Short other);
}
