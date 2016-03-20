package dg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeMap;

/* This is a grid using axial coordinates with pointy topped hexagons. */
/**
 * @author murch
 * 
 */
public class Gameboard {

	private Hashtable<Coordinates, Terrain> grid;
	private LinkedList<Agent> gamePieces;

	/**
	 * Creates an empty gameboard.
	 */
	public Gameboard() {
		this.grid = new Hashtable<Coordinates, Terrain>();
	}

	/**
	 * @param c
	 * @param t
	 * @throws IllegalArgumentException
	 */
	public void addField(Coordinates c, Terrain t) throws IllegalArgumentException {
		if (false == grid.containsKey(c)) {
			grid.put(c, t);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Adds a standard guard as seen in Spar Wars.
	 * 
	 * @param spawn
	 * @param route
	 */
	public void addGuard(Coordinates spawn, LinkedList<Coordinates> route) {
		gamePieces.add(new Guard(spawn, route, this));
	}

	/**
	 * @param origin
	 *            The field the path starts from.
	 * @param target
	 *            The field that wants to be reached.
	 * @return List of Coordinates that need to be traveled to reach the target. Starts with neighbor of origin.
	 * @throws IllegalArgumentException
	 *             When either origin or target are out of bounds.
	 */
	public LinkedList<Coordinates> calculatePath(Coordinates origin, Coordinates target)
			throws IllegalArgumentException {
		if (isInBounds(origin) == false || isInBounds(target) == false) {
			throw new IllegalArgumentException();
		}

		boolean success = false;
		LinkedList<Coordinates> bestPath = new LinkedList<Coordinates>();
		HashMap<Coordinates, Coordinates> previousField = new HashMap<Coordinates, Coordinates>();
		HashMap<Coordinates, Integer> realCostToField = new HashMap<Coordinates, Integer>();
		TreeMap<Integer, LinkedList<Coordinates>> pathCandidates = new TreeMap<Integer, LinkedList<Coordinates>>();

		// Don't search if target is origin.
		if (origin.equals(target)) {
			success = true;
		}

		realCostToField.put(origin, 0);

		// Initialize path candidates from origin
		for (Coordinates cand : getMoveOptions(origin)) {
			previousField.put(cand, origin);
			realCostToField.put(cand, Coordinates.calculateDistance(origin, cand));

			Integer estimatedCost = Coordinates.calculateDistance(cand, target) + realCostToField.get(cand);
			LinkedList<Coordinates> candidateList = new LinkedList<Coordinates>();
			candidateList.push(cand);
			pathCandidates.put(estimatedCost, candidateList);
		}

		// A* search. Guarantees shortest path.
		while (success == false && pathCandidates.isEmpty() == false) {
			LinkedList<Coordinates> mostPromising = pathCandidates.get(pathCandidates.firstKey());
			if (mostPromising.isEmpty()) {
				pathCandidates.remove(pathCandidates.firstKey());
			} else {
				Coordinates current = mostPromising.pollFirst();
				if (current.equals(target)) {
					success = true;
				} else {
					for (Coordinates cand : getMoveOptions(current)) {
						Integer newRealCost = realCostToField.get(current)
								+ Coordinates.calculateDistance(current, cand);
						if (realCostToField.containsKey(cand) == false || realCostToField.get(cand) > newRealCost) {
							previousField.put(cand, current);
							realCostToField.put(cand, newRealCost);
							Integer newEstimatedCost = Coordinates.calculateDistance(cand, target)
									+ realCostToField.get(cand);
							if (pathCandidates.containsKey(newEstimatedCost)) {
								pathCandidates.get(newEstimatedCost).push(cand);
							} else {
								LinkedList<Coordinates> candidateList = new LinkedList<Coordinates>();
								candidateList.push(cand);
								pathCandidates.put(newEstimatedCost, candidateList);
							}
						}
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
		for (Coordinates cand : Coordinates.getAdjacent(c)) {
			if (isInBounds(cand)) {
				neighbors.add(cand);
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
		if (false == isInBounds(viewPoint) || false == isInBounds(target)) {
			throw new IndexOutOfBoundsException();
		}

		boolean visible = true;
		Integer distance = Coordinates.calculateDistance(viewPoint, target);
		HashMap<Integer, HashSet<Coordinates>> rayFields = LOSUtilities.getFieldsOnRay(viewPoint, target);

		for (int i = 0; (visible == true) && (i < distance); i++) {
			// No need to check last field, it's visible if not blocked by earlier step.
			HashSet<Coordinates> nextStep = rayFields.get(i);

			// Touching Lines only block view when both are wall.
			boolean wall = true;
			for (Coordinates c : nextStep) {
				// Treat outOfBound fields like walls for visibility
				if (isInBounds(c) == true && getTerrain(c) != Terrain.WALL) {
					wall = false;
				}
			}
			if (wall == true) {
				visible = false;
			}
		}

		return visible;
	}

	public HashSet<Coordinates> getFieldOfView(Coordinates viewPoint) throws IndexOutOfBoundsException {
		if (false == isInBounds(viewPoint)) {
			throw new IndexOutOfBoundsException();
		}
		HashSet<Coordinates> visibleFields = new HashSet<Coordinates>();
		HashSet<Coordinates> evaluatedFields = new HashSet<Coordinates>();

		LinkedList<Coordinates> frontier = new LinkedList<Coordinates>();
		frontier.add(viewPoint);

		while (frontier.isEmpty() == false) {
			Coordinates currentField = frontier.pollFirst();
			if (evaluatedFields.contains(currentField) == false && isVisible(viewPoint, currentField)) {
				visibleFields.add(currentField);
				for (Coordinates neighbor : getNeighbors(currentField)) {
					if (false == evaluatedFields.contains(neighbor)) {
						frontier.add(neighbor);
					}
				}
			}
			evaluatedFields.add(currentField);
		}

		return visibleFields;
	}

	public Hashtable<Coordinates, Terrain> getGrid() {
		return grid;
	}

	public LinkedList<Agent> getAgents() {
		return gamePieces;
	}
}
