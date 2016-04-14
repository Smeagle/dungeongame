package dg.action;

import dg.GameException;
import dg.gui.Frame;
import dg.gui.TitlePanel;

public class EndGameAction extends Action {

	public EndGameAction() {
		super("Spiel beenden");
	}

	@Override
	public void execute() throws GameException {
		Frame.getInstance().showPanel(TitlePanel.getInstance());
	}
	
}
