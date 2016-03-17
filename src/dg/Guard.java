package dg;
import java.util.LinkedList;

public class Guard {

	/* Agent characteristics */
	private final Coordinates SPAWN;
	private final Integer FIELD_OF_VIEW;
	private final Integer FIELD_OF_VIEW_ALERTED;
	private final Integer SPEED;
	private final Integer SPEED_ALERTED;
	private final LinkedList<Coordinates> PATROLROUTE;
	
	/* Current stats */
	private Coordinates currentPosition;
	private Coordinates currentTarget;
	private Direction directionOfView;
	
	private Integer movesLeft;
	private boolean alerted;
	private Integer enemiesInSight;
	private Coordinates nearestEnemy;
	
	public Guard(Coordinates spawn, Integer fov, Integer fovA, Integer speed, Integer speedAlerted, LinkedList<Coordinates> route) {
		this.SPAWN = spawn;
		this.FIELD_OF_VIEW = fov;
		this.FIELD_OF_VIEW_ALERTED = fovA;
		this.SPEED = speed;
		this.SPEED_ALERTED = speedAlerted;
		this.PATROLROUTE = route;
		this.currentPosition = spawn;
		this.directionOfView = Direction.TOPLEFT; //TODO should be in direction of next waypoint.
		this.movesLeft = 0;
		this.alerted = false;		
	}
	
	public Coordinates move(Direction dir) {
		Coordinates targetField = new Coordinates(currentPosition.getQ() + dir.get_dq(), currentPosition.getR() + dir.get_dr());
		currentPosition = targetField; //TODO needs to check that move is allowed.
		movesLeft = movesLeft - 1;
		checkLineOfSight();
		return currentPosition;
	}
	
	public void respawn() {
		//TODO Logic for respawning after being killed.
	}
	
	public boolean checkLineOfSight() {
		boolean enemySpotted = false;
		//TODO check all fields in field of view for enemies
		if(enemySpotted == true) {
			alerted = true;
			movesLeft = SPEED_ALERTED - SPEED + movesLeft;
			checkLOS_ALERTED();
		}
		return enemySpotted;
	}
	
	public void checkLOS_ALERTED() {
		//TODO check all fields in alerted fov for enemies
	}
	
	public Direction calculatePath(Coordinates currentPosition, Coordinates targetPosition) {
		//TODO calculate actual path
		return Direction.TOPLEFT;
	}
	
	public void startRound() {
		
		/* Check if still alerted */
		if(alerted == true) {
			checkLOS_ALERTED();
		} else {
			checkLineOfSight();
		}
		
		if(enemiesInSight == 0) {
			alerted = false;
			movesLeft = SPEED;
			currentTarget = PATROLROUTE.get(0); //TODO must be the next one obviously, not always the first
		} else {
			alerted = true;
			movesLeft = SPEED_ALERTED;
			currentTarget = nearestEnemy;
		}
		
		/* Move along patrol or towards enemy */
		
		while(movesLeft > 0) {
			move(calculatePath(currentPosition, currentTarget));
		}
		
	}
}
