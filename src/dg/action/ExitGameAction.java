package dg.action;

import dg.GameException;

public class ExitGameAction extends Action {

	public ExitGameAction() {
		super("Spiel verlassen");
	}

	@Override
	public void execute() throws GameException {
		// maybe save game later...
		System.exit(0);
	}
	
}
