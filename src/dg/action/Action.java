package dg.action;

import dg.GameException;

public abstract class Action {

	public Action() {
	}
	
	public abstract void execute() throws GameException;
	
}
