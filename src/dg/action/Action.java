package dg.action;

import dg.GameException;

public abstract class Action {

	private String name; // The Label to show on the button for the action
	private Integer keyCode = null;
	
	public Action(String name) {
		this.name = name;
	}
	
	public Action(String name, int keyCode) { // see VK_ constants in KeyEvent
		this.name = name;
		this.keyCode = keyCode;
	}
	
	public abstract void execute() throws GameException;
	
	public String getName() {
		return name;
	}

	public Integer getKeyCode() {
		return keyCode;
	}
	
}
