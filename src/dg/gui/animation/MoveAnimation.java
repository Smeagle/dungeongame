package dg.gui.animation;

import dg.Agent;
import dg.Coordinates;
import dg.Direction;

public class MoveAnimation {
	
	private Agent agent;
	private Coordinates start;
	private Direction direction;
	
	public MoveAnimation(Agent agent, Coordinates start, Direction direction) {
		this.agent = agent;
		this.start = start;
		this.direction = direction;
	}
	
	public MoveAnimation(Agent agent, Coordinates start, Coordinates end) {
		this.agent = agent;
		this.start = start;
		this.direction = Direction.getDirectionFromCoordinates(end, start);
	}
	
	public Coordinates getStart() {
		return start;
	}

	public Agent getAgent() {
		return agent;
	}

	public Direction getDirection() {
		return direction;
	}
	
	public String toString() {
		return agent + " from " + start + " towards " + direction;
	}

}
