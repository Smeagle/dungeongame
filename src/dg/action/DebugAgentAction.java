package dg.action;

import java.awt.event.KeyEvent;

import dg.Agent;
import dg.Coordinates;
import dg.GameException;
import dg.GameState;
import dg.event.SelectionEventHandler;
import dg.gui.input.Selection;

public class DebugAgentAction extends Action {

	private static Agent debugAgent = null;
	
	public DebugAgentAction() {
		super("d: Agent debuggen", KeyEvent.VK_D);
	}

	@Override
	public void execute() throws GameException {
		Selection.waitForSelection(new SelectionEventHandler() {
			@Override
			public void onEvent(Coordinates c) {
				debugAgent = null;
				for (Agent a : GameState.getBoard().getAgents()) {
					if (a.getPosition().equals(c)) {
						debugAgent = a;
						break;
					}
				}
				Selection.disable();
			}
		}, "Welchen Agenten wollen Sie debuggen?");
	}
	
	public static Agent getDebugAgent() {
		return debugAgent;
	}
	
}