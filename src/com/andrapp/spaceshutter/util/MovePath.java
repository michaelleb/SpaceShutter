package com.andrapp.spaceshutter.util;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Misha
 * 
 */
public class MovePath implements Iterable<Point2D> {
	private final static String TAG = MovePath.class.getSimpleName();
	public final static int POINT_NOT_ON_PATH = -1;

	protected Point2D startPoint;
	protected Point2D endPoint;
	protected LinkedList<Vector2D> pathVectors = new LinkedList<Vector2D>();

	/**
	 * create default MovePaht that start and ends at (0,0) coordinate
	 */
	public MovePath() {
		this.startPoint = new Point2D(0, 0);
		this.endPoint = new Point2D(0, 0);
		pathVectors.add(new Vector2D());
	}

	public MovePath(int startX, int startY) {

		this.startPoint = new Point2D(startX, startY);
		this.endPoint = new Point2D(startPoint);

		// add (0,0) vector
		pathVectors.add(new Vector2D());
	}

	public MovePath(MovePath other) {
		this.startPoint = new Point2D(other.startPoint);
		this.endPoint = new Point2D(other.endPoint);
		this.pathVectors = new LinkedList<Vector2D>();

		Iterator<Vector2D> otherVectors = other.pathVectors.iterator();

		while (otherVectors.hasNext()) {
			this.pathVectors.add(new Vector2D(otherVectors.next()));
		}
	}

	public Point2D getStartPoint() {
		return startPoint;
	}

	public void addRect(PointS leftUp, PointS rightDouw) {

		this.lineTo(leftUp.x, rightDouw.y); // left down point
		this.lineTo(rightDouw.x, rightDouw.y); // right down point
		this.lineTo(rightDouw.x, leftUp.y); // right up
		this.lineTo(leftUp.x, leftUp.y); // left up ( close rect path )
	}

	/**
	 * Subtract some path from front end, and return subtracted MovePath,
	 * current path shorten accordingly
	 * 
	 * @param distance
	 * @return
	 */
	public MovePath chopFront(int distance) {

		MovePath resultPaht = new MovePath(startPoint.x, startPoint.y);
		chopFront(resultPaht, distance);

		return resultPaht;
	}

	/**
	 * Subtract some path from front end of current path, and append subtracted
	 * path to destination Path, current path become shorter for given distance.
	 * note: only vectors appended (start point of destPath remain the same).
	 * 
	 * @param destPath
	 * @param distance
	 */
	public void chopFront(MovePath resultCutoff, int distance) {

		int size; // hold current vector size (let V = [v1,v2] then sizeOf(V) =
					// v1+v2. )
		Vector2D vector; // hold current vector
		Iterator<Vector2D> pathVectorIter = pathVectors.iterator();// iterator
																	// over path
																	// vectors

		while (pathVectorIter.hasNext() && distance > 0) {

			vector = pathVectorIter.next();
			size = vector.norm();

			if (size < distance) {
				// simple case: current vector shorter then wanted distance:
				// add whole vector to destination path
				resultCutoff.appendVector(vector.x, vector.y);
				// remove this vector from current path
				pathVectorIter.remove();
				// reduce remain distance
				distance -= size;
			} else {
				// special case: the vector of current path longer then wanted
				// distance
				if (vector.x > 0) {
					vector.x -= distance;
					resultCutoff.appendVector(distance, 0);
				} else {
					vector.y -= distance;
					resultCutoff.appendVector(0, distance);
				}
				distance = 0;
			}
		}

		// promote start point of current path
		this.startPoint.set(resultCutoff.endPoint);
		// if no vectors remain in vectorPath, then restore it to hold initial
		// vector of [0,0].
		if (this.pathVectors.isEmpty()) {
			pathVectors.add(new Vector2D());
		}

	}

	/**
	 * This function add absolute direction to the path. if new point is opposed
	 * to previous direction then it effectively shorten the path.
	 * 
	 * @param x
	 * @param y
	 */
	public void lineTo(int x, int y) {

		int dx = x - this.endPoint.x;
		int dy = y - this.endPoint.y;

		if (Math.abs(dx) > Math.abs(dy)) {

			appendVector(dx, 0);

		} else {

			appendVector(0, dy);
		}
	}

	/**
	 * append pathVector of other path to end of this one.
	 * 
	 * @param other
	 */
	public void appendPath(MovePath otherPath) {
		
		if(!this.endPoint.equals(otherPath.startPoint)){
			return;
		}
		
		Iterator<Point2D> iter = otherPath.iterator();
		Point2D nextPointToBeAdded;
		iter.next();
		
		while(iter.hasNext()){
			nextPointToBeAdded = iter.next();
			this.lineTo(nextPointToBeAdded.x, nextPointToBeAdded.y);
		}
		
		
	}

	@Override
	public String toString() {
		String str = new String();
		Point2D point;

		str += "pathPoints:";
		Iterator<Point2D> pointsIter = this.iterator();
		while (pointsIter.hasNext()) {
			point = pointsIter.next();
			str += " (" + point.x + "," + point.y + ")";
		}
		str += " endPoint: (" + endPoint.x + "," + endPoint.y + ")";
		return str;
	}

	public Iterator<Point2D> iterator() {

		return new MovePathIterator();

	}

	// Iterator class of MovePath
	class MovePathIterator implements Iterator<Point2D> {

		// start with first point
		private Point2D coordinate = new Point2D(startPoint); 
															
		private int firstVecSize = pathVectors.getFirst().norm();

		private Iterator<Vector2D> pathVecorIterator = pathVectors.iterator();

		private boolean firstTime = true; // true if next point is the first one
											// of this path

		public boolean hasNext() {

			if (firstTime) {
				// There is always at least one point ( the start point )
				return true;
			}
			return pathVecorIterator.hasNext();
		}

		public Point2D next() {

			if (firstTime && firstVecSize != 0) {
				// if this path is not only one point then first time return the
				// start coordinate of this path.
				// the coordinate already presetted with first point in C-tor,
				// hence do nothing here
			} else {
				// case the path consist of only one point or we already
				// returned
				// the first point
				coordinate.add(pathVecorIterator.next());
			}

			firstTime = false;
			return coordinate;

		}

		/**
		 * There is no meaning of remove point from path in current
		 * implementation. Throw UnsupportedOperationException
		 * 
		 */
		public void remove() {

			throw new UnsupportedOperationException(
					"MovePath.Iterator.remove() is not implemented");
		}

	}

	private void appendVector(int v1, int v2) {

		if (v2 == 0 && pathVectors.getLast().y == 0 || v1 == 0
				&& pathVectors.getLast().x == 0) {
			pathVectors.getLast().plus(v1, v2);
		} else {

			pathVectors.add(new Vector2D(v1, v2));
		}

		endPoint.add(v1, v2);
	}

	public boolean isOnlyPoint() {
		if (pathVectors.getLast().norm() == 0) {
			// vector[0,0] may be only in path that have no length
			return true;
		}

		return false;
	}

	/**
	 * set the start point to given (x,y) coordinate. the end point of this path
	 * changed accordingly to new end position
	 * 
	 * @param x
	 * @param y
	 */
	public void offsetTo(int x, int y) {
		int dx = x - startPoint.x;
		int dy = y - startPoint.y;

		startPoint.x += dx;
		startPoint.y += dy;
		endPoint.x += dx;
		endPoint.y += dy;
	}

	public void getRemotePoint(float atRemotePracentOfPath, Point2D result) {

		if (atRemotePracentOfPath <= 0 || this.isOnlyPoint())
			result.set(startPoint);
		else if (atRemotePracentOfPath >= 1)
			result.set(endPoint);
		else {

			// if we here then we can assume that:
			// 1.path is not single point.
			// 2.the remote point that we seek is some where between the two
			// ends of this path not inclusive

			int pathLength = this.getLength();
			// calculate actual distance of point from start of the path
			int actualDistance = Math.round(pathLength * atRemotePracentOfPath);

			// make start point, then go over vector and track location till you
			// finished this length

			result.set(this.startPoint);
			Iterator<Vector2D> pathVecIter = pathVectors.iterator();
			Vector2D currentVector;

			while (actualDistance > 0) {
				currentVector = pathVecIter.next();
				if (actualDistance > currentVector.norm()) {
					result.add(currentVector);

				} else {

					float vectorPart = (float) actualDistance
							/ currentVector.norm();
					Vector2D temp = new Vector2D(currentVector);
					result.add(temp.times(vectorPart));
				}

				actualDistance -= currentVector.norm();
			}
		}
	}

	/**
	 * @return path length
	 */
	public int getLength() {

		Iterator<Vector2D> pathVecIter = pathVectors.iterator();

		int pathLength = 0;

		// sum the Path length
		while (pathVecIter.hasNext()) {
			pathLength += pathVecIter.next().norm();
		}

		return pathLength;
	}

	/**
	 * 
	 * @param p
	 * @return true if given point is on this MovePath
	 */
	public boolean hasPoint(Point2D p) {

		return this.getDistanceOf(p.x, p.y) != MovePath.POINT_NOT_ON_PATH;
	}

	public int getDistanceOf(int pX, int pY) {

		int distance = MovePath.POINT_NOT_ON_PATH;

		if (this.startPoint.equals(pX, pY)) {
			distance = 0;
		} else if (this.endPoint.equals(pX, pY)) {
			distance = this.getLength();
		} else if (!this.isOnlyPoint()) {

			// get the origin point of each vector on the path.
			// build another vector form this origin point and given point
			boolean thePointWasFound = false;
			Iterator<Vector2D> pahtVecIter = this.pathVectors.iterator();
			Point2D originPoint = new Point2D(this.startPoint);
			Vector2D currentVector;
			Vector2D otherVector = new Vector2D();
			int currentVecNorm;
			int otherVecNorm;
			int currentDistance = 0;

			while (pahtVecIter.hasNext() && !thePointWasFound) {
				currentVector = pahtVecIter.next();
				otherVector.setX(pX - originPoint.x);
				otherVector.setY(pY - originPoint.y);
				currentVecNorm = currentVector.norm();

				if (currentVector.isHaveSameDirectionAs(otherVector)) {
					otherVecNorm = otherVector.norm();
					if (otherVecNorm <= currentVecNorm) {
						thePointWasFound = true;
						currentDistance += otherVecNorm;
					}
				} else {
					currentDistance += currentVecNorm;
				}

				originPoint.add(currentVector);

			}

			if (thePointWasFound)
				distance = currentDistance;

		}

		return distance;
	}

	public MovePath getSubPath(Point2D start, Point2D end) {

		int distanceToFirstPoint = this.getDistanceOf(start.x, start.y);
		int distanceToSecondPoint = this.getDistanceOf(end.x, end.y);

		// case one of the points not on the path
		if (distanceToFirstPoint == MovePath.POINT_NOT_ON_PATH
				|| distanceToSecondPoint == MovePath.POINT_NOT_ON_PATH) {
			return null;
		}
		// case start point is the same as end point
		if (distanceToFirstPoint == distanceToSecondPoint) {
			return null;
		}

		// case end point located on path before the start: swap distances
		if (distanceToSecondPoint < distanceToFirstPoint) {
			int temp = distanceToSecondPoint;
			distanceToSecondPoint = distanceToFirstPoint;
			distanceToFirstPoint = temp;
		}
		
		int resultPahtLength = distanceToSecondPoint - distanceToFirstPoint;

		// copy the path.
		MovePath copyOfPaht = new MovePath(this);

		copyOfPaht.chopFront(distanceToFirstPoint);
		
		copyOfPaht = copyOfPaht.chopFront(resultPahtLength);

		return copyOfPaht;
	}

	public Point2D getEndPont() {
		return this.endPoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endPoint == null) ? 0 : endPoint.hashCode());
		result = prime * result
				+ ((pathVectors == null) ? 0 : pathVectors.hashCode());
		result = prime * result
				+ ((startPoint == null) ? 0 : startPoint.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MovePath)) {
			return false;
		}
		MovePath other = (MovePath) obj;
		if (endPoint == null) {
			if (other.endPoint != null) {
				return false;
			}
		} else if (!endPoint.equals(other.endPoint)) {
			return false;
		}
		if (pathVectors == null) {
			if (other.pathVectors != null) {
				return false;
			}
		} else if (!pathVectors.equals(other.pathVectors)) {
			return false;
		}
		if (startPoint == null) {
			if (other.startPoint != null) {
				return false;
			}
		} else if (!startPoint.equals(other.startPoint)) {
			return false;
		}
		return true;
	}

}
