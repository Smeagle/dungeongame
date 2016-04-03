package dg.event;

import dg.Coordinates;

public abstract class SelectionEventHandler implements EventHandler {

	public abstract void onEvent(Coordinates selectedCoordinates);
	
	public void onEvent() {
		onEvent(null);
	}
	
}
