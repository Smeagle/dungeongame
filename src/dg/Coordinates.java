package dg;

/* Immutable Coordinate class. */
public class Coordinates {
	public final Integer r;
	public final Integer q;

	/* r rises from top to bottom, q rises from left to right, */
	public Coordinates(Integer r, Integer q) {
		super();
		this.r = r;
		this.q = q;
	}

	public Coordinates(Coordinates c, Direction dir) {
		super();
		this.r = c.r + dir.dr;
		System.out.println("c.r is " + c.r + ", dir.dr is " + dir.dr + ", r is " + this.r);
		this.q = c.q + dir.dq;
		System.out.println("c.q is " + c.q + ", dir.dq is " + dir.dq + ", q is " + this.q);
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
		return (this.r == other.r && this.q == other.q);
	}

	@Override
	public int hashCode() {
		String hashInput = "(" + this.r + "," + this.q + ")";
		return hashInput.hashCode();
	}
}
