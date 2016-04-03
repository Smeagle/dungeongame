package dg.action;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

import dg.Coordinates;
import dg.GameException;
import dg.Guard;
import dg.event.SelectionEventHandler;
import dg.gui.input.Selection;

public class DebugPathfindingAction extends Action {

	private static Coordinates start = null;
	private static boolean active = false;
	private static List<Coordinates> path = null;
	
	public DebugPathfindingAction() {
		super("w: Wegfindung debuggen", KeyEvent.VK_W);
	}
	
	@Override
	public void execute() throws GameException {
		start = null;
		active = false;
		Selection.waitForSelection(new SelectionEventHandler() {
			@Override
			public void onEvent(Coordinates c) {
				start = c;
				Selection.waitForSelection(new SelectionEventHandler() {
					@Override
					public void onEvent(Coordinates c2) {
						path = new Guard().calculatePath(start, c2);
						if (path.size() > 0) {
							path.add(start);
							active = true;
						}
						Selection.disable();
					}
				}, "Wegfindung debuggen: Wählen Sie das Zielfeld...");
			}
		}, "Wegfindung debuggen: Wählen Sie das Startfeld...");
	}
	
	public static boolean isActive() {
		return active;
	}
	
	public static Collection<Coordinates> getPath() {
		return path;
	}
	
}
