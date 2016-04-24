package dg.action;

import dg.GameException;
import dg.GameState;

public class StopGameAction extends Action {

	@Override
	public void execute() throws GameException {
		GameState.stopGame();
	}
	
}
