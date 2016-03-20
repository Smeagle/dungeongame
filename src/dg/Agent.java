package dg;

public abstract class Agent {
	protected Coordinates position;
	protected Affiliation affiliation;
	protected Coordinates spawn;
	protected Integer movesPerTurn;
	protected Integer movesLeft;

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
	public abstract void takeTurn(Gameboard board);
	
	public Integer getDistance(Agent agent) {
		return Coordinates.calculateDistance(position, agent.getPosition());
	}

}
