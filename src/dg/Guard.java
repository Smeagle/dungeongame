package dg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Guard extends Agent {
	private LinkedList<Coordinates> patrolRoute;
	private Integer nextPatrolPoint;
	private boolean isAlerted;
	private Direction directionOfView;
	private Gameboard board;
	private Integer alertedBonus;
	private Coordinates nearestEnemy;

	public Guard(Coordinates spawnPoint, LinkedList<Coordinates> patrolRoute, Gameboard board) {
		this.board = board;
		this.position = spawnPoint;
		this.affiliation = Affiliation.DUNGEON;
		this.spawn = spawnPoint;
		this.patrolRoute = patrolRoute;
		resetPatrolRoute();
		this.directionOfView = Direction.getDirectionFromCoordinates(spawn,
				board.calculatePath(spawnPoint, patrolRoute.get(nextPatrolPoint)).getFirst());
		this.isAlerted = false;
		this.movesPerTurn = 2;
		this.alertedBonus = 3;
		this.nearestEnemy = position;
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
	private Coordinates checkFieldOfView(Gameboard board) {
		Coordinates enemyTarget = position;

		for (Agent agent : board.getAgents()) {
			if (agent.getAffiliation() == Affiliation.PLAYER) {
				if (notices(agent)) {
					if (isAlerted == false) {
						// Rerun checkFieldOfView when alerted with full view
						alert();
						return checkFieldOfView(board);
					} else {
						// Target closest
						if (enemyTarget == position
								|| getDistance(agent) < Coordinates.calculateDistance(position, enemyTarget)) {
							enemyTarget = agent.getPosition();
						}
					}
				}
			}
		}

		return enemyTarget;
	}

	@Override
	public void kill() {
		position = spawn;
		resetPatrolRoute();
		isAlerted = true;
	}

	private void makeMove(Gameboard board) {
		if (position == patrolRoute.get(nextPatrolPoint)) {
			// Waypoint reached, start from beginning if at end
			nextPatrolPoint = (nextPatrolPoint + 1) % patrolRoute.size();
		}
		Coordinates target = patrolRoute.get(nextPatrolPoint);
		if (nearestEnemy != position) {
			target = nearestEnemy;
		}

		LinkedList<Coordinates> path = board.calculatePath(position, target);
		position = path.pollFirst();
		directionOfView = Direction.getDirectionFromCoordinates(position, path.getFirst());
		movesLeft = movesLeft - 1;
		nearestEnemy = checkFieldOfView(board);
	}

	private boolean notices(Agent agent) {
		boolean agentNoticed = false;
		if (board.isVisible(position, agent.getPosition())) {
			if (isAlerted == true) {
				// 360° view
				agentNoticed = true;
			} else {
				// In 180° view, line of sight must include one of three fields in front of guard.
				HashMap<Integer, HashSet<Coordinates>> fieldsOnLineOfSight = LOSUtilities.getFieldsOnRay(position,
						agent.getPosition());
				HashSet<Coordinates> rayFieldsAtDistOne = fieldsOnLineOfSight.get(1);

				Coordinates fieldInFront = position.getAdjacentInDirection(directionOfView);
				LinkedList<Coordinates> fieldsInFront = (Coordinates.getCommonAdjacent(position, fieldInFront));
				fieldsInFront.add(fieldInFront);

				for (Coordinates fif : fieldsInFront) {
					if (rayFieldsAtDistOne.contains(fif)) {
						agentNoticed = true;
					}
				}
			}
		}
		return agentNoticed;
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
	public void takeTurn(Gameboard board) {
		nearestEnemy = checkFieldOfView(board);
		if (isAlerted == false || nearestEnemy == position && isAlerted == true) {
			// Reset alerted if enemy no longer in sight.
			isAlerted = false;
			movesLeft = movesPerTurn;
		} else {
			movesLeft = movesPerTurn + alertedBonus;
		}

		while (movesPerTurn > 0) {
			makeMove(board);
		}
	}
}
