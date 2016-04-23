package dg.action;

import dg.GameException;

public class ExitGameAction extends Action {

	@Override
	public void execute() throws GameException {
		// maybe save game later...
		System.exit(0);
	}
	
}
