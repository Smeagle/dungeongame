package dg.action;

import dg.GameException;
import dg.GameState;
import dg.gui.BoardPanel;
import dg.gui.Frame;

public class StartGameAction extends Action {

	public StartGameAction() {
		super("Spiel starten");
	}

	@Override
	public void execute() throws GameException {
		Frame.getInstance().showPanel(BoardPanel.getInstance());
		GameState.startGame();
	}
	
}
