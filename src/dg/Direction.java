package dg;

public enum Direction {
	TOPLEFT(-1, 0), TOPRIGHT(-1, 1), RIGHT(0, 1), BOTTOMRIGHT(1, 0), BOTTOMLEFT(1, -1), LEFT(0, -1);

	public final Integer dr;
	public final Integer dq;

	Direction(Integer dr, Integer dq) {
		this.dr = dr;
		this.dq = dq;
	}

	public static Direction getDirectionFromCoordinates(Coordinates to, Coordinates from) {
		if (Coordinates.calculateDistance(to, from) > 1) {
			throw new IllegalArgumentException();
		}

		Direction result = TOPLEFT;

		for (Direction dir : Direction.values()) {
			if (dir.dq == to.q - from.q && dir.dr == to.r - from.r) {
				result = dir;
			}
		}
		return result;
	}
}
