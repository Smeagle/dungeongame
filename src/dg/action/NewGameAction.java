package dg.action;

import dg.GameException;
import dg.GameState;

public class NewGameAction extends Action {

	@Override
	public void execute() throws GameException {
		GameState.newGame();
	}
	
}
