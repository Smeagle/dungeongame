package dg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public abstract class Agent {
	protected Gameboard board;
	protected Coordinates position;
	protected Affiliation affiliation;
	protected Coordinates spawn;
	protected Integer movesPerTurn;
	protected Integer movesLeft;

	public Agent(Coordinates spawnpoint, Gameboard board) {
		this.board = board;
		this.spawn = spawnpoint;
	}
	
	public Affiliation getAffiliation() {
		return affiliation;
	}

	public Coordinates getPosition() {
		return position;
	}

	public Coordinates getSpawn() {
		return spawn;
	}
	
	/**
	 * Call this method when agent is killed.
	 */
	public abstract void kill();
	
	/**
	 * Call this method when it's the agents turn to make his move.
	 * @param board Current state of the gameboard.
	 */
	public abstract void takeTurn();
	
	public Integer getDistance(Agent agent) {
		return Coordinates.calculateDistance(position, agent.getPosition());
	}

	/**
	 * Generates and returns the valid moves from the given Coordinates.
	 * 
	 * @param c
	 *            Field for which move options are requested.
	 * @return Coordinates of empty neighboring fields.
	 */
	protected LinkedList<Coordinates> getMoveOptions(Coordinates c) {
		LinkedList<Coordinates> moveOptions = new LinkedList<Coordinates>();

		for (Coordinates neighbor : board.getNeighbors(c)) {
			if (board.getTerrain(neighbor) == Terrain.FLOOR) {
				moveOptions.add(neighbor);
			}
		}

		return moveOptions;
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
		if (board.isInBounds(origin) == false || board.isInBounds(target) == false) {
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

	
}
