package dg;

import java.util.Hashtable;

/* This is a grid using Axial Coordinates with pointy topped hexagons. */
public class Gameboard {

	public Gameboard() {
		this.grid = new Hashtable<Coordinates, Terrain>();
	}

	private Hashtable<Coordinates, Terrain> grid;

	public void addField(Coordinates c, Terrain t)
			throws IllegalArgumentException {
		if (false == grid.containsKey(c)) {
			grid.put(c, t);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Reads the terrain for a field from the board. Requires field to be on the
	 * board.
	 * 
	 * @param c
	 *            Coordinates of the field.
	 * @return The terrain of the requested field.
	 */
	public Terrain getTerrain(Coordinates c) throws IndexOutOfBoundsException {
		if (false == grid.containsKey(c)) {
			throw new IndexOutOfBoundsException();
		}

		return grid.get(c);
	}

	/**
	 * Calculates whether target is visible from viewPoint.
	 * 
	 * @param viewPoint
	 *            The field that the viewer is occupying.
	 * @param target
	 *            The field that is being checked for visibility.
	 * @return true when target is visible from viewPoint.
	 */
	public boolean isVisible(Coordinates viewPoint, Coordinates target)
			throws IndexOutOfBoundsException {
		if (false == grid.containsKey(viewPoint) && grid.containsKey(target)) {
			throw new IndexOutOfBoundsException();
		}
		boolean visible = false;
		if(calculateDistance(viewPoint, target) <= 1.0) {
			visible = true;
		}
		// TODO implement general function for bigger distances
		return visible;
	}
	
	public double calculateDistance(Coordinates a, Coordinates b) {
		double dist = (
				Math.abs(a.q - b.q)
				+ Math.abs(a.q + a.r - b.q - b.r)
				+ Math.abs(a.r - b.r)
				) / 2.0;
		return dist;
	}

}
