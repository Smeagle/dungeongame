/**
 * 
 */
package dg;

import java.util.LinkedList;

/**
 * @author murch
 * 
 */
public class LOSUtilities {
	/**
	 * Calculates Coordinates for Corner. Values are factor 3 of actual coordinates.
	 * @param c
	 * @return
	 */
	public static LinkedList<Coordinates> calculateHexCorners(Coordinates c) {
		LinkedList<Coordinates> corners = new LinkedList<Coordinates>();
		for (int i = 0; i < Direction.values().length; i++) {
			// Leverages that Directions are in circular order
			Coordinates adjacent_one = new Coordinates(c, Direction.values()[i]);
			Coordinates adjacent_two = new Coordinates(c, Direction.values()[(i+1)%(Direction.values().length)]);
			Coordinates corner = new Coordinates((c.r+adjacent_one.r+adjacent_two.r),(c.q+adjacent_one.q+adjacent_two.q));

			corners.add(corner);
		}
		return corners;
	}

	public static Intersection intersects(Coordinates rayOrigin, Coordinates rayTarget, Coordinates obstacleCandidate) {
		LinkedList<Coordinates> corners = calculateHexCorners(obstacleCandidate);
		
		if((obstacleCandidate.r-rayOrigin.r)*(rayTarget.q-rayOrigin.q) - (rayTarget.r-rayOrigin.r)*(obstacleCandidate.q-rayOrigin.q) == 0) {
			return Intersection.INTERSECTS;
		}
		
		int left = 0;
		int straight = 0;
		int right = 0;
		
		for(Coordinates corner : corners) {
			//Ugly hack with triple values in corners
			if((corner.r-3*rayOrigin.r)*(3*rayTarget.q-3*rayOrigin.q) - (3*rayTarget.r-3*rayOrigin.r)*(corner.q-3*rayOrigin.q) > 0.0) {
				left = left+1;
			} else if ((corner.r-3*rayOrigin.r)*(3*rayTarget.q-3*rayOrigin.q) - (3*rayTarget.r-3*rayOrigin.r)*(corner.q-3*rayOrigin.q) == 0) {
				straight = straight+1;
			} else {
				right = right+1;
			}
		}
		if(left == 6 || right == 6) {
			return Intersection.CLEAR;
		} else if (straight == 2) {
			return Intersection.TOUCHES;
		} else {
			return Intersection.INTERSECTS;
		}
		
		
		// TODO not implemented

	}
}