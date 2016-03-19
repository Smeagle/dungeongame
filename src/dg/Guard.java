package dg;

import java.util.LinkedList;

/**
 * @author murch
 * Class for adversaries in the game.
 */
public class Guard {

	private final Integer alertedFieldOfView;
	private final Integer alertedSpeed;
	private final Integer fieldOfView;
	private final Integer speed;
	private final LinkedList<Coordinates> patrolRoute;
	private final Coordinates spawnPoint;

	private boolean alerted;
	private Coordinates currentPosition;
	private Coordinates currentTarget;
	private Direction directionOfView;
	private Integer enemiesInSight;
	private Integer movesLeft;
	private Coordinates nearestEnemy;
	private Gameboard board;

	public Guard(Coordinates spawn, Integer fov, Integer fovA, Integer speed, Integer speedAlerted,
			LinkedList<Coordinates> route, Gameboard board) {
		this.spawnPoint = spawn;
		this.fieldOfView = fov;
		this.alertedFieldOfView = fovA;
		this.speed = speed;
		this.alertedSpeed = speedAlerted;
		this.patrolRoute = route;
		this.currentPosition = spawn;
		this.directionOfView = Direction.TOPLEFT; // TODO should be in direction of next waypoint.
		this.movesLeft = 0;
		this.alerted = false;
		this.board = board;
	}

	public boolean checkLineOfSight() {
		boolean enemySpotted = false;
		// TODO check all fields in field of view for enemies
		if (enemySpotted == true) {
			alerted = true;
			movesLeft = alertedSpeed - speed + movesLeft;
			checkLOS_ALERTED();
		}
		return enemySpotted;
	}

	public void checkLOS_ALERTED() {
		// TODO check all fields in alerted fov for enemies
	}

	public Coordinates move(Direction dir) {
		Coordinates targetField = new Coordinates(currentPosition.r + dir.dr, currentPosition.q + dir.dq);
		currentPosition = targetField; // TODO needs to check that move is allowed.
		movesLeft = movesLeft - 1;
		checkLineOfSight();
		return currentPosition;
	}

	public void respawn() {
		currentPosition = spawnPoint;
		movesLeft = 0;
		alerted = false;
		directionOfView = Direction.TOPLEFT; // TODO should be in direction of next waypoint
		// TODO Logic for respawning after being killed.
	}

	public void startRound() {
		/* Check if still alerted */
		if (alerted == true) {
			checkLOS_ALERTED();
		} else {
			checkLineOfSight();
		}

		if (enemiesInSight == 0) {
			alerted = false;
			movesLeft = speed;
			currentTarget = patrolRoute.get(0); // TODO must be the next one obviously, not always the first
		} else {
			alerted = true;
			movesLeft = alertedSpeed;
			currentTarget = nearestEnemy;
		}

		/* Move along patrol or towards enemy */
		while (movesLeft > 0) {
			LinkedList<Coordinates> path = board.calculatePath(currentPosition, currentTarget);
			Coordinates nextField = path.get(0);
				
			move(Direction.getDirectionFromCoordinates(nextField, currentPosition));}

	}
}
