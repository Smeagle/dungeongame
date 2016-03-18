package dg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeMap;

/* This is a grid using axial coordinates with pointy topped hexagons. */
public class Gameboard {

	private Hashtable<Coordinates, Terrain> grid;

	public Gameboard() {
		this.grid = new Hashtable<Coordinates, Terrain>();
	}

	public void addField(Coordinates c, Terrain t) throws IllegalArgumentException {
		if (false == grid.containsKey(c)) {
			grid.put(c, t);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public LinkedList<Coordinates> calculatePath(Coordinates origin, Coordinates target)
			throws IllegalArgumentException {
		if (isInBounds(origin) == false || isInBounds(target) == false) {
			throw new IllegalArgumentException();
		}

		boolean success = false;
		LinkedList<Coordinates> bestPath = new LinkedList<Coordinates>();
		HashMap<Coordinates, Coordinates> previousField = new HashMap<Coordinates, Coordinates>();
		HashMap<Coordinates, Integer> realCostToField = new HashMap<Coordinates, Integer>();
		TreeMap<Integer, Coordinates> pathCandidates = new TreeMap<Integer, Coordinates>();
		
		// Don't search if target is origin.
		if (origin.equals(target)) {
			success = true;
		}

		realCostToField.put(origin, 0);
		
		// Initialize path candidates from origin
		for (Coordinates cand : getMoveOptions(origin)) {
			previousField.put(cand, origin);
			realCostToField.put(cand, Coordinates.calculateDistance(origin, cand));
			pathCandidates.put(Coordinates.calculateDistance(cand, target) + realCostToField.get(cand), cand);
		}

		// A* search. Guarantees shortest path.
		while (success == false && pathCandidates.isEmpty() == false) {
			Coordinates current = pathCandidates.pollFirstEntry().getValue();
			if (current.equals(target)) {
				success = true;
			} else {
				for (Coordinates cand : getMoveOptions(current)) {
					Integer newRealCost = realCostToField.get(current) + Coordinates.calculateDistance(current, cand);
					if (realCostToField.containsKey(cand) == false || realCostToField.get(cand) > newRealCost) {
						previousField.put(cand, current);
						realCostToField.put(cand, newRealCost);
						pathCandidates.put(Coordinates.calculateDistance(cand, target) + realCostToField.get(cand),
								cand);
					}
				}
			}
		}

		// Step through previous fields in reverse to get best path.
		if (success == true && false == target.equals(origin)) {
			boolean pathComplete = false;
			Coordinates step = target;
			while (pathComplete == false) {
				bestPath.addFirst(step);
				Coordinates prevStep = previousField.get(step);
				if (prevStep == origin) {
					pathComplete = true;
				} else {
					step = prevStep;
				}
			}
		}

		return bestPath;
	}

	/**
	 * Reads the terrain for a field from the board. Requires field to be on the board.
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
	 * Check to ask whether field is part of gameboard.
	 * 
	 * @param c
	 *            Coordinates of the field.
	 * @return false when c out of bounds.
	 */
	public boolean isInBounds(Coordinates c) {
		return grid.containsKey(c);
	}

	/**
	 * Generates and returns the neighboring fields that are not out of bounds.
	 * 
	 * @param c
	 *            Field for which neighbors are requested.
	 * @return Coordinates of in-bound neighbor fields.
	 */
	public LinkedList<Coordinates> getNeighbors(Coordinates c) throws IndexOutOfBoundsException {
		if (false == grid.containsKey(c)) {
			throw new IndexOutOfBoundsException();
		}
		LinkedList<Coordinates> neighbors = new LinkedList<Coordinates>();
		for (Direction dir : Direction.values()) {
			Coordinates neighbor = new Coordinates(c, dir);
			if (isInBounds(neighbor)) {
				neighbors.add(neighbor);
			}
		}

		return neighbors;
	}

	/**
	 * Generates and returns the valid moves from the given Coordinates.
	 * 
	 * @param c
	 *            Field for which move options are requested.
	 * @return Coordinates of empty neighboring fields.
	 */
	public LinkedList<Coordinates> getMoveOptions(Coordinates c) {
		LinkedList<Coordinates> moveOptions = new LinkedList<Coordinates>();

		for (Coordinates neighbor : getNeighbors(c)) {
			if (getTerrain(neighbor) == Terrain.FLOOR) {
				moveOptions.add(neighbor);
			}
		}

		return moveOptions;
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
	public boolean isVisible(Coordinates viewPoint, Coordinates target) throws IndexOutOfBoundsException {
		if (false == grid.containsKey(viewPoint) && grid.containsKey(target)) {
			throw new IndexOutOfBoundsException();
		}
		boolean visible = false;
		if (Coordinates.calculateDistance(viewPoint, target) <= 1.0) {
			visible = true;
		}
		// TODO implement general function for bigger distances
		return visible;
	}
}
