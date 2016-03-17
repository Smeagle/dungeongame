package dg;

public enum Direction {
	TOPLEFT(0, -1),
	TOPRIGHT(1, -1),
	RIGHT(1,0),
	BOTTOMRIGHT(0,1),
	BOTTOMLEFT(-1,1),
	LEFT(-1,0);
		
	private final Integer dq;
	private final Integer dr;
	
	Direction(Integer dq, Integer dr) {
		this.dq = dq;
		this.dr = dr;
	}
	
	public Integer get_dr() {
		return dr;
	}
	
	public Integer get_dq() {
		return dq;
	}
}