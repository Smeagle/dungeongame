package dg;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import dg.action.DebugAgentAction;
import dg.gui.BoardPanel;
import dg.gui.Colors;
import dg.gui.GUIUtils;
import dg.gui.ImageCache;
import dg.gui.Shapes;
import dg.gui.animation.AnimationQueue;
import dg.gui.animation.MoveAnimation;

public class Guard extends Agent {
	private LinkedList<Coordinates> patrolRoute;
	private Integer nextPatrolPoint = 0;
	private boolean isAlerted;
	private Direction directionOfView;
	private Integer alertedBonus;
	private Agent nearestEnemy;

	public Guard() {
		super(new Coordinates(0, 0), GameState.getBoard());
	}
	
	public Guard(Coordinates spawnpoint, Gameboard board) {
		super(spawnpoint,board);
		this.position = spawnpoint;
		this.affiliation = Affiliation.DUNGEON;
		patrolRoute = new LinkedList<Coordinates>();
		resetPatrolRoute();
		this.isAlerted = false;
		this.movesPerTurn = 2;
		this.alertedBonus = 3;
		this.nearestEnemy = null;
	}
	
	public Guard(Coordinates spawnpoint, LinkedList<Coordinates> patrolRoute, Gameboard board) {	
		super(spawnpoint,board);
		this.position = spawnpoint;
		this.affiliation = Affiliation.DUNGEON;
		this.patrolRoute = patrolRoute;
		resetPatrolRoute();
		this.isAlerted = false;
		this.movesPerTurn = 2;
		this.alertedBonus = 3;
		this.nearestEnemy = null;
	}

	public void alert() {
		if (isAlerted == false) {
			movesLeft = movesLeft + alertedBonus;
		}
		isAlerted = true;
	}

	/**
	 * Checks the field of view. Alerts Guard if enemy is spotted and reruns with 360° vision. Returns Coordinates of
	 * nearest enemy, or position if no enemy in sight.
	 * 
	 * @param board
	 * @return Coordinates of nearest enemy, or own position if no enemies in sight.
	 */
	private Agent checkFieldOfView() {
		Agent nearestEnemy = null;

		for (Agent agent : board.getAgents()) {
			if (agent.getAffiliation() == Affiliation.PLAYER) {
				if (notices(agent)) {
					if (isAlerted == false) {
						// Rerun checkFieldOfView when alerted with full view
						alert();
						return checkFieldOfView();
					} else {
						// Target closest
						if (nearestEnemy == null || getDistance(agent) < getDistance(nearestEnemy)) {
							nearestEnemy = agent;
						}
					}
				}
			}
		}

		return nearestEnemy;
	}

	@Override
	public void kill() {
		position = spawn;
		resetPatrolRoute();
		isAlerted = true;
	}

	private void makeMove() {
		if (position.equals(patrolRoute.get(nextPatrolPoint))) {
			// Waypoint reached, start from beginning if at end
			nextPatrolPoint = (nextPatrolPoint + 1) % patrolRoute.size();
		}
		Coordinates target = patrolRoute.get(nextPatrolPoint);
		if (nearestEnemy != null) {
			target = nearestEnemy.getPosition();
		}
		LinkedList<Coordinates> path = calculatePath(position, target);
		
		if (path.isEmpty() == false) {
			Coordinates oldPosition = position;
			Coordinates newPosition = path.pollFirst();
			
			Direction newDirectionOfView = Direction.getDirectionFromCoordinates(newPosition, oldPosition);
			
			AnimationQueue.push(new MoveAnimation(this, oldPosition, newPosition, newDirectionOfView));
			
			directionOfView = newDirectionOfView;
			position = newPosition;
			
			if (nearestEnemy != null && position.equals(nearestEnemy.getPosition())) {
				nearestEnemy.kill();
			}
		}
		
//		if (path.isEmpty() == false) {
//			directionOfView = Direction.getDirectionFromCoordinates(position, path.getFirst());
//		}
		
		movesLeft = movesLeft - 1;
		nearestEnemy = checkFieldOfView();
	}
	
	private boolean notices(Coordinates from, Direction viewDirection, Coordinates targetPosition) {
		if (board.isVisible(from, targetPosition)) {
			if (isAlerted == true) {
				// 360° view
				return true;
			} else {
				// In 180° view, line of sight must include one of three fields in front of guard.
				HashMap<Integer, HashSet<Coordinates>> fieldsOnLineOfSight = LOSUtilities.getFieldsOnRay(from,
						targetPosition);
				HashSet<Coordinates> rayFieldsAtDistOne = fieldsOnLineOfSight.get(1);

				if (rayFieldsAtDistOne == null) {
					return false;
				}
				
				Coordinates fieldInFront = from.getAdjacentInDirection(viewDirection);
				LinkedList<Coordinates> fieldsInFront = (Coordinates.getCommonAdjacent(from, fieldInFront));
				fieldsInFront.add(fieldInFront);
				
				for (Coordinates fif : fieldsInFront) {
					if (rayFieldsAtDistOne.contains(fif)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean notices(Agent agent) {
		return notices(this.getPosition(), this.getDirectionOfView(), agent.getPosition());
	}

	/**
	 * Sets target waypoint to patrol waypoint after current position, if current position is in patrol route. Else does
	 * nothing.
	 */
	private void resetPatrolRoute() {
		if (patrolRoute.contains(position)) {
			nextPatrolPoint = patrolRoute.indexOf(position) + 1;
		}
	}

	@Override
	public void takeTurn() {
		nearestEnemy = checkFieldOfView();
		if (isAlerted == false || nearestEnemy == null && isAlerted == true) {
			// Reset alerted if enemy no longer in sight.
			isAlerted = false;
			movesLeft = movesPerTurn;
		} else {
			movesLeft = movesPerTurn + alertedBonus;
		}

		while (movesLeft > 0) {
			makeMove();
		}
	}

	/**
	 * Generates and returns the valid moves from the given Coordinates.
	 * 
	 * @param c
	 *            Field for which move options are requested.
	 * @return Coordinates of empty neighboring fields.
	 */
	@Override
	protected LinkedList<Coordinates> getMoveOptions(Coordinates c) {
		LinkedList<Coordinates> moveOptions = new LinkedList<Coordinates>();

		for (Coordinates neighbor : board.getNeighbors(c)) {
			if (board.getTerrain(neighbor) == Terrain.FLOOR) {
				boolean isOccupiedByFriend = false;
				for (Agent agent : board.getAgents()) {
					if (agent.getPosition().equals(neighbor) && agent.getAffiliation() == Affiliation.DUNGEON) {
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

	@Override
	public String getImage() {
		return ImageCache.GUARD;
	}
	
	@Override
	public void paintAgent(Graphics2D g2) {
		// paint view direction
		if (directionOfView != null) {
			Point2D hexOffset = AnimationQueue.getHexOffset(this);
			AffineTransform t = getDirectionTransform(hexOffset);
			g2.setTransform(t);
			g2.setColor(Colors.AGENT_VIEW_DIRECTION_POINTER);
			g2.fill(Shapes.getShape(Shapes.VIEW_TRIANGLE));
			g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
		}
		
		super.paintAgent(g2);
	}
	
	@Override
	public void paintBeforeAgents(Graphics2D g2) {
		if (this.equals(DebugAgentAction.getDebugAgent())) {
			// field of view
			Coordinates animationPosition = AnimationQueue.getPosition(this);
			Direction animationViewDirection = AnimationQueue.getDirectionOfView(this);
			for (Coordinates c : GameState.getBoard().getFieldOfView(AnimationQueue.getPosition(this))) {
				if (GameState.getBoard().getTerrain(c) == Terrain.WALL) {
					continue;
				}
				if (notices(animationPosition, animationViewDirection, c)) {
					Point2D hexOffset = GUIUtils.getHexOffset(c);
					AffineTransform t = BoardPanel.getHexTransform(c, hexOffset);
					g2.setTransform(t);
					g2.setColor(Colors.DEBUG_FIELD_OF_VIEW);
					g2.fill(Shapes.getShape(Shapes.HEX));
					g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
				}
			}
		}
	}
	
	@Override
	public void paintAfterAgents(Graphics2D g2) {
		if (this.equals(DebugAgentAction.getDebugAgent())) {
			// paint way points
			for (Coordinates p : patrolRoute) {
				Point2D hexOffset = GUIUtils.getHexOffset(p);
				AffineTransform t = getWayPointTransform(hexOffset);
				g2.setTransform(t);
				g2.setColor(Colors.DEBUG_GUARD_WAYPOINT);
				g2.fillOval(-10, -10, 20, 20);
				g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
			}
		}
	}
	
	private AffineTransform getWayPointTransform(Point2D hexOffset) {
		AffineTransform transform = new AffineTransform();
		transform.translate(BoardPanel.translateX, BoardPanel.translateY);
		transform.scale(BoardPanel.scale, BoardPanel.scale);
		transform.translate(hexOffset.getX(), hexOffset.getY());
		return transform;
	}
	
	private AffineTransform getDirectionTransform(Point2D hexOffset) {
		AffineTransform transform = new AffineTransform();
		transform.translate(BoardPanel.translateX, BoardPanel.translateY);
		transform.scale(BoardPanel.scale, BoardPanel.scale);
		transform.translate(hexOffset.getX(), hexOffset.getY());
		transform.rotate(GUIUtils.getDirectionRotation(AnimationQueue.getDirectionOfView(this)));
		return transform;
	}
	
}
