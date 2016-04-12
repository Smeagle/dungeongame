package dg;

/**
 * @author murch
 * Direction encapsulates differences between two coordinates. Mostly used for neighbors.
 */
public enum Direction {
	TOPLEFT(0, -1), 
	TOPRIGHT(1, -1), 
	RIGHT(1, 0), 
	BOTTOMRIGHT(0, 1), 
	BOTTOMLEFT(-1, 1), 
	LEFT(-1, 0);

	public final Integer dq;
	public final Integer dr;

	Direction(Integer dq, Integer dr) {
		this.dq = dq;
		this.dr = dr;
	}

	public static Direction getDirectionFromCoordinates(Coordinates to, Coordinates from) {
		if (Coordinates.calculateDistance(to, from) > 1) {
			throw new IllegalArgumentException();
		}

		for (Direction dir : Direction.values()) {
			if (from.getAdjacentInDirection(dir).equals(to)) {
				return dir;
			}
		}
		
		return null;
	}
}
