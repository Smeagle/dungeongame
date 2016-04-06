package dg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import dg.gui.BoardPanel;
import dg.gui.GUIUtils;
import dg.gui.ImageCache;
import dg.gui.Shapes;
import dg.gui.animation.AnimationQueue;

public abstract class Agent {
	protected Gameboard board;
	protected Coordinates position;
	protected Affiliation affiliation;
	protected boolean computerControlled = true;
	protected Coordinates spawn;
	protected Integer movesPerTurn;
	protected Integer movesLeft;
	protected Direction directionOfView = Direction.BOTTOMLEFT;

	public Agent(Coordinates spawnpoint, Gameboard board) {
		this.board = board;
		this.spawn = spawnpoint;
		this.position = spawnpoint;
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

	/**
	 * The image to paint for this character.
	 */
	public abstract String getImage();
	
	public Integer getDistance(Agent agent) {
		return Coordinates.calculateDistance(position, agent.getPosition());
	}

	/**
	 * Generates and returns the valid moves from the given Coordinates. Can't move through friendly units.
	 * 
	 * @param c
	 *            Field for which move options are requested.
	 * @return Coordinates of empty neighboring fields.
	 */
	protected LinkedList<Coordinates> getMoveOptions(Coordinates c) {
		LinkedList<Coordinates> moveOptions = new LinkedList<Coordinates>();

		for (Coordinates neighbor : board.getNeighbors(c)) {
			if (board.getTerrain(neighbor) == Terrain.FLOOR || board.getTerrain(neighbor) == Terrain.EXIT) {
				boolean isOccupiedByFriend = false;
				for (Agent agent : board.getAgents()) {
					if (agent.getPosition() == neighbor && agent.getAffiliation() == affiliation) {
						isOccupiedByFriend = true;
					}
				}
				if (false == isOccupiedByFriend) {
					moveOptions.add(neighbor);
				}
			}
		}

		return moveOptions;
	}

	/**
	 * @param origin
	 *            The field the path starts from.
	 * @param target
	 *            The field that wants to be reached.
	 * @return List of Coordinates that need to be traveled to reach the target. Starts with neighbor of origin. Returns empty list if target is origin or target is unreachable.
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
		
		// Initialize path candidates with origin
		realCostToField.put(origin, 0);
		Integer estimatedCost = Coordinates.calculateDistance(origin, target);
		
		LinkedList<Coordinates> candidateList = new LinkedList<Coordinates>();
		candidateList.add(origin);
		pathCandidates.put(estimatedCost, candidateList);

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
								pathCandidates.get(newEstimatedCost).add(cand);
							} else {
								candidateList = new LinkedList<Coordinates>();
								candidateList.add(cand);
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
	 * Called by paintComponent() of BoardPanel to paint the agent
	 */
	public void paintAgent(Graphics2D g2) {
		Image image = ImageCache.getImage(this.getImage());
		if (image != null) {
			Point2D hexOffset = AnimationQueue.getHexOffset(this);
			AffineTransform t = getAgentTransform(hexOffset);
			g2.setTransform(t);
			double r = Shapes.HEX_RADIUS;
			double h = Shapes.HEX_TRIANGLE_HEIGHT;
			g2.drawImage(image, (int) -h - 1, (int) -r - 1, (int) (2 * h) + 2, (int) (2 * r) + 2, null);
			g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
		}
	}
	
	public void paintBeforeAgents(Graphics2D g2) {
	}
	
	public void paintAfterAgents(Graphics2D g2) {
	}

	private static AffineTransform getAgentTransform(Point2D hexOffset) {
		AffineTransform transform = new AffineTransform();
		transform.translate(BoardPanel.translateX, BoardPanel.translateY);
		transform.scale(BoardPanel.scale, BoardPanel.scale);
		transform.translate(hexOffset.getX(), hexOffset.getY());
		return transform;
	}

	public Direction getDirectionOfView() {
		return directionOfView;
	}
	
	public boolean isComputerControlled() {
		return computerControlled;
	}
	
}
