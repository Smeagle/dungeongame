package dg;

import java.util.LinkedList;

/**
 * @author murch Immutable Coordinate class.
 */
public class Coordinates {
	public final Integer q;
	public final Integer r;

	/* r rises from top to bottom, q rises from left to right, */
	public Coordinates(Integer q, Integer r) {
		super();
		this.q = q;
		this.r = r;
	}

	public Coordinates(Coordinates c, Direction dir) {
		super();
		this.q = c.q + dir.dq;
		this.r = c.r + dir.dr;
	}

	public String toString() {
		return "(" + q + "," + r + ")";
	}

	/**
	 * Gets a hexagonal "Manhattan" distance, i.e. number of fields counted, not length of a straight line.
	 * 
	 * @param a
	 *            Starting field for distance calculation.
	 * @param b
	 *            End field for distance calculation.
	 * @return Distance in number of fields.
	 */
	public static Integer calculateDistance(Coordinates a, Coordinates b) {
		Integer dist = (Math.abs(a.r - b.r) + Math.abs(a.r + a.q - b.r - b.q) + Math.abs(a.q - b.q)) / 2;
		return dist;
	}

	@Override
	public boolean equals(Object c) {
		if (this == c)
			return true;
		if (c == null || (this.getClass() != c.getClass())) {
			return false;
		}
		Coordinates other = (Coordinates) c;
		return (this.q == other.q && this.r == other.r);
	}

	@Override
	public int hashCode() {
		String hashInput = "(" + this.q + "," + this.r + ")";
		return hashInput.hashCode();
	}

	public static LinkedList<Coordinates> getAdjacent(Coordinates c) {
		LinkedList<Coordinates> adjacentFields = new LinkedList<Coordinates>();
		for (Direction dir : Direction.values()) {
			Coordinates adjacent = new Coordinates(c, dir);
			adjacentFields.add(adjacent);
		}
		return adjacentFields;
	}
	
	public Coordinates getAdjacentInDirection(Direction dir) {
		return new Coordinates(this.r + dir.dr, this.q + dir.dq);
	}

	public static LinkedList<Coordinates> getCommonAdjacent(Coordinates a, Coordinates b) {
		LinkedList<Coordinates> commonAdjacentFields = new LinkedList<Coordinates>();
		LinkedList<Coordinates> adjacentFieldsA = getAdjacent(a);
		LinkedList<Coordinates> adjacentFieldsB = getAdjacent(b);
		
		for (Coordinates adjacentA : adjacentFieldsA) {
			if(adjacentFieldsB.contains(adjacentA)) {
				commonAdjacentFields.add(adjacentA);
			}
		}
		return commonAdjacentFields;
	}
}
