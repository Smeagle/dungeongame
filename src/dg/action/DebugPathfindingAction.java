package dg.action;

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
	
	@Override
	public void execute() throws GameException {
		clear();
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
	
	public static void clear() {
		start = null;
		active = false;
		path = null;
	}
	
}
