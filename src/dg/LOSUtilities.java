/**
 * 
 */
package dg;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author murch
 * 
 */
public class LOSUtilities {
	/**
	 * Calculates Coordinates for Corner. Values are factor 3 of actual coordinates (i.e. misused Coordinates). Utility
	 * method for this class.
	 * 
	 * @param c
	 * @return
	 */
	private static LinkedList<Coordinates> calculateHexCorners(Coordinates c) {
		LinkedList<Coordinates> corners = new LinkedList<Coordinates>();
		for (int i = 0; i < Direction.values().length; i++) {
			// Leverages that Directions are in circular order
			Coordinates adjacent_one = new Coordinates(c, Direction.values()[i]);
			Coordinates adjacent_two = new Coordinates(c, Direction.values()[(i + 1) % (Direction.values().length)]);
			Coordinates corner = new Coordinates((c.r + adjacent_one.r + adjacent_two.r),
					(c.q + adjacent_one.q + adjacent_two.q));

			corners.add(corner);
		}
		return corners;
	}

	public static Intersection intersects(Coordinates rayOrigin, Coordinates rayTarget, Coordinates obstacleCandidate) {
		LinkedList<Coordinates> corners = calculateHexCorners(obstacleCandidate);

		if ((obstacleCandidate.r - rayOrigin.r) * (rayTarget.q - rayOrigin.q) - (rayTarget.r - rayOrigin.r)
				* (obstacleCandidate.q - rayOrigin.q) == 0) {
			return Intersection.INTERSECTS;
		}

		int left = 0;// Not sure if they are actually left and right, but doesn't matter in this method.
		int straight = 0;
		int right = 0;

		for (Coordinates corner : corners) {
			// Ugly hack with triple values in corners
			if ((corner.r - 3 * rayOrigin.r) * (3 * rayTarget.q - 3 * rayOrigin.q)
					- (3 * rayTarget.r - 3 * rayOrigin.r) * (corner.q - 3 * rayOrigin.q) > 0.0) {
				left = left + 1;
			} else if ((corner.r - 3 * rayOrigin.r) * (3 * rayTarget.q - 3 * rayOrigin.q)
					- (3 * rayTarget.r - 3 * rayOrigin.r) * (corner.q - 3 * rayOrigin.q) == 0) {
				straight = straight + 1;
			} else {
				right = right + 1;
			}
		}
		if (left == 6 || right == 6) {
			return Intersection.CLEAR;
		} else if (straight == 2) {
			return Intersection.TOUCHES;
		} else {
			return Intersection.INTERSECTS;
		}
	}

	public static HashMap<Integer, Coordinates> fieldsOnRay(Coordinates rayOrigin, Coordinates rayTarget) {
		// Let's try a naïve approach first. When I have time, I'll put a hexagonal version Bresenham algorithm here.
		HashMap<Integer, Coordinates> fieldsOnLine = new HashMap<Integer, Coordinates>();

		Integer dist = Coordinates.calculateDistance(rayOrigin, rayTarget);
		Integer dr = rayTarget.r - rayOrigin.r;
		Integer dq = rayTarget.q - rayOrigin.q;

		if (rayOrigin.r == rayTarget.r || rayOrigin.q == rayTarget.q || dr == -dq) {
			// Yay, that's a straight line!

			for (int i = 0; i < dist + 1; i++) {
				fieldsOnLine.put(i, new Coordinates(rayOrigin.r + i * dr / dist, rayOrigin.q + i * dq / dist));
			}
		} else if (dr == dq || -2 * dr == dq || -2 * dq == dr) {
			// Touching Line!
			fieldsOnLine.put(0, rayOrigin);
			Coordinates lastIntersected = rayOrigin;
			for (int i = 1; i < dist + 1; i++) {
				if (i % 2 == 0) {
					Coordinates currentIntersected = new Coordinates(rayOrigin.r + (i * dr) / dist, rayOrigin.q
							+ (i * dq) / dist);
					for (Coordinates touching : Coordinates.getCommonAdjacent(lastIntersected, currentIntersected)) {
						fieldsOnLine.put(i - 1, touching);
					}
					fieldsOnLine.put(i, currentIntersected);
					lastIntersected = currentIntersected;
				}
			}
		} else {
			// Check slanted rectangle between origin and target
			for (int range_r = Math.min(rayOrigin.r, rayTarget.r); range_r < Math.max(rayOrigin.r, rayTarget.r) + 1; range_r++) {
				for (int range_q = Math.min(rayOrigin.q, rayTarget.q); range_q < Math.max(rayOrigin.q, rayTarget.q) + 1; range_q++) {
					Coordinates cand = new Coordinates(range_r, range_q);
					if (Intersection.CLEAR != intersects(rayOrigin, rayTarget, cand)) {
						fieldsOnLine.put(Coordinates.calculateDistance(rayOrigin, cand), cand);
					}
				}
			}
		}

		return fieldsOnLine;
	}
}
