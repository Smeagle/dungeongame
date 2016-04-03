package dg.gui.input;

import dg.event.EventHandler;

public class Selection {

	private static boolean selectionMode = false;

	private static EventHandler eventHandler = null;
	
	private static String helpText = null;
	
	public static boolean isSelectionMode() {
		return selectionMode;
	}

	public static void waitForSelection(EventHandler eventHandler, String helpText) {
		Selection.selectionMode = true;
		Selection.eventHandler = eventHandler;
		Selection.helpText = helpText;
	}
	
	public static void disable() {
		selectionMode = false;
		eventHandler = null;
		Selection.helpText = null;
	}

	public static EventHandler getEventHandler() {
		return eventHandler;
	}

	public static String getHelpText() {
		return helpText;
	}
	
}
