package dg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

/* This is a grid using axial coordinates with pointy topped hexagons. */
/**
 * @author murch
 * 
 */
public class Gameboard {

	private Hashtable<Coordinates, Terrain> grid;
	private LinkedList<Agent> gamePieces;
	private LinkedList<Agent> killedPlayers;

	/**
	 * Creates an empty gameboard.
	 */
	public Gameboard() {
		this.grid = new Hashtable<Coordinates, Terrain>();
		this.gamePieces = new LinkedList<Agent>();
	}

	/**
	 * @param c
	 * @param t
	 * @throws IllegalArgumentException
	 */
	public void addField(Coordinates c, Terrain t) throws IllegalArgumentException {
		if (false == grid.containsKey(c)) {
			System.out.println("Adding field " + c.toString() + ", Terrain is " + t);
			grid.put(c, t);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Adds a player.
	 * 
	 * @param spawn
	 *            Spawnpoint coordinates of player.
	 * @param identity
	 *            Identifier of the player.
	 */
	public void addPlayer(Coordinates spawn, String identity) {
		gamePieces.add(new Player(spawn, this, identity));
	}

	/**
	 * Removes player figure from game.
	 * 
	 * @param killedPlayer
	 *            The figure that is being removed from the game.
	 */
	public void playerKilled(Player killedPlayer) {
		killedPlayers.add(killedPlayer);
		gamePieces.remove(killedPlayer);
	}

	public LinkedList<Agent> getKilledPlayers() {
		return killedPlayers;
	}

	/**
	 * Adds a standard guard as seen in Spar Wars.
	 * 
	 * @param spawn
	 * @param route
	 * @return The newly added guard.
	 */
	public Guard addGuard(Coordinates spawn, LinkedList<Coordinates> route) {
		Guard guard = new Guard(spawn, route, this);
		gamePieces.add(guard);
		return guard;
	}

	public void addAgent(Agent agent) {
		gamePieces.add(agent);
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
			// No need to check target field, it's visible if not blocked by earlier step.
			HashSet<Coordinates> nextStep = rayFields.get(i);

			// Touching Lines only block view when both are wall.
			boolean wall = true;
			for (Coordinates c : nextStep) {
				// Treat outOfBound fields like walls for visibility.
				if (isInBounds(c) == true && getTerrain(c) != Terrain.WALL) {
					// For only one intersected or two touched fields a single Floor tile is sufficient.
					wall = false;
				} else if (LOSUtilities.intersects(viewPoint, target, c) == Intersection.INTERSECTS) {
					// Is either outOfBounds or wall, and intersects, stops visibility.
					visible = false;
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
				System.out.println(currentField.toString() + " visible");
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

	public LinkedList<Agent> getPlayers() {
		LinkedList<Agent> players = new LinkedList<Agent>();
		for (Agent agent : getAgents()) {
			if (agent.getAffiliation().equals(Affiliation.PLAYER)) {
				players.add(agent);
			}
		}
		return players;
	}
}
