package dg.gui.animation;

import dg.Agent;
import dg.Coordinates;
import dg.Direction;

public class MoveAnimation {
	
	private Agent agent;
	private Coordinates start;
	private Direction moveDirection; // move direction
	private Direction directionOfView; // view direction
	private Direction oldDirectionOfView;
	
	public MoveAnimation(Agent agent, Coordinates start, Direction moveDirection, Direction directionOfView) {
		this.agent = agent;
		this.start = start;
		this.moveDirection = moveDirection;
		this.directionOfView = directionOfView;
		this.oldDirectionOfView = agent.getDirectionOfView();
	}
	
	public MoveAnimation(Agent agent, Coordinates start, Coordinates end, Direction directionOfView) {
		this.agent = agent;
		this.start = start;
		this.moveDirection = Direction.getDirectionFromCoordinates(end, start);
		this.directionOfView = directionOfView;
		this.oldDirectionOfView = agent.getDirectionOfView();
	}
	
	public Coordinates getStart() {
		return start;
	}

	public Agent getAgent() {
		return agent;
	}

	public Direction getDirectionOfView() {
		return directionOfView;
	}
	
	public Direction getOldDirectionOfView() {
		return oldDirectionOfView;
	}
	
	public Direction getMoveDirection() {
		return moveDirection;
	}
	
	@Override
	public String toString() {
		return agent + " from " + start + " towards " + moveDirection + " looking at " + directionOfView;
	}

}
