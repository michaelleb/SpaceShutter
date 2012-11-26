package mark.geometry;

public interface Shape2D {
	
	/*
	 * true if there exists intersection between boundaries of this and other
	 */
	public boolean isIntersection(Circle2D.Short other);
	
	public boolean isIntersection(Polygon2D.Short other);
	
	public boolean isIntersection(Path2D.Short other);
}
