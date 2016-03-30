package dg.gui;

import dg.GameException;

public abstract class Action {

	private String name; // The Label to show on the button for the action
	
	public Action(String name) {
		this.name = name;
	}
	
	public abstract void execute() throws GameException;
	
	public String getName() {
		return name;
	}
	
}
