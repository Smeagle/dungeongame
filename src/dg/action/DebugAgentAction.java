package dg.action;

import dg.Agent;
import dg.Coordinates;
import dg.GameException;
import dg.GameState;
import dg.event.SelectionEventHandler;
import dg.gui.input.Selection;

public class DebugAgentAction extends Action {

	private static Agent debugAgent = null;
	
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
	
	public static void clear() {
		debugAgent = null;
	}
	
}
