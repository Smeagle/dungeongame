package dg;

public class Coordinate {
	private Integer q;
	private Integer r;

	/* q rises from left to right, r rises from top to bottom */
	Coordinate(Integer q, Integer r) {
		this.q = q;
		this.r = r;
	}

	public Integer getR() {
		return r;
	}

	public Integer getQ() {
		return q;
	}

}
