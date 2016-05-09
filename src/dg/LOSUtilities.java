/**
 * 
 */
package dg;

import java.util.HashMap;
import java.util.HashSet;
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
			Coordinates corner = new Coordinates((c.q + adjacent_one.q + adjacent_two.q),
					(c.r + adjacent_one.r + adjacent_two.r));

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

	/**
	 * Generates all fields along the vision ray from origin to target. Does not check whether Coordinates correspond to
	 * in-bounds fields on the board!
	 * 
	 * @param rayOrigin
	 * @param rayTarget
	 * @return
	 */
	public static HashMap<Integer, HashSet<Coordinates>> getFieldsOnRay(Coordinates rayOrigin, Coordinates rayTarget) {
		// Let's try a naïve approach first. When I have time, I'll put a hexagonal version Bresenham algorithm here.
		HashMap<Integer, HashSet<Coordinates>> fieldsOnLine = new HashMap<Integer, HashSet<Coordinates>>();

		Integer dist = Coordinates.calculateDistance(rayOrigin, rayTarget);
		Integer dq = rayTarget.q - rayOrigin.q;
		Integer dr = rayTarget.r - rayOrigin.r;

		if (rayOrigin == rayTarget) {
			// Duh, looking at myself.
			HashSet<Coordinates> fieldsAtSameDist = new HashSet<Coordinates>();
			fieldsAtSameDist.add(rayOrigin);
			fieldsOnLine.put(0, fieldsAtSameDist);
		} else if (rayOrigin.r == rayTarget.r || rayOrigin.q == rayTarget.q || dr == -dq) {
			// Yay, that's a straight line!
			for (int i = 0; i < dist + 1; i++) {
				HashSet<Coordinates> fieldsAtSameDist = new HashSet<Coordinates>();
				fieldsAtSameDist.add(new Coordinates(rayOrigin.q + i * dq / dist, rayOrigin.r + i * dr / dist));
				fieldsOnLine.put(i, fieldsAtSameDist);
			}
		} else if (dr == dq || -2 * dr == dq || -2 * dq == dr) {
			// Touching Line!
			HashSet<Coordinates> fieldsAtSameDist = new HashSet<Coordinates>();
			fieldsAtSameDist.add(rayOrigin);
			fieldsOnLine.put(0, fieldsAtSameDist);

			Coordinates lastIntersected = rayOrigin;
			for (int i = 1; i < dist + 1; i++) {
				if (i % 2 == 0) {
					Coordinates currentIntersected = new Coordinates(rayOrigin.q + (i * dq) / dist, rayOrigin.r
							+ (i * dr) / dist);
					fieldsAtSameDist = new HashSet<Coordinates>();
					for (Coordinates touching : Coordinates.getCommonAdjacent(lastIntersected, currentIntersected)) {
						fieldsAtSameDist.add(touching);
					}
					fieldsOnLine.put(i - 1, fieldsAtSameDist);

					fieldsAtSameDist = new HashSet<Coordinates>();
					fieldsAtSameDist.add(currentIntersected);
					fieldsOnLine.put(i, fieldsAtSameDist);

					lastIntersected = currentIntersected;
				}
			}
		} else {
			// Check slanted rectangle between origin and target
			HashSet<Coordinates> fieldsAtSameDist = new HashSet<Coordinates>();
			for (int range_r = Math.min(rayOrigin.r, rayTarget.r); range_r < Math.max(rayOrigin.r, rayTarget.r) + 1; range_r++) {
				for (int range_q = Math.min(rayOrigin.q, rayTarget.q); range_q < Math.max(rayOrigin.q, rayTarget.q) + 1; range_q++) {
					Coordinates cand = new Coordinates(range_q, range_r);
					if (Intersection.CLEAR != intersects(rayOrigin, rayTarget, cand)) {
						if (fieldsOnLine.containsKey(Coordinates.calculateDistance(rayOrigin, cand)) == false) {
							fieldsAtSameDist = new HashSet<Coordinates>();
							fieldsAtSameDist.add(cand);
							fieldsOnLine.put(Coordinates.calculateDistance(rayOrigin, cand), fieldsAtSameDist);
						} else {
							fieldsOnLine.get(Coordinates.calculateDistance(rayOrigin, cand)).add(cand);
						}
					}
				}
			}
		}

		return fieldsOnLine;
	}
}
