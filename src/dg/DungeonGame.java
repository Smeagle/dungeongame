package dg;

import dg.dev.DevUtilities;
import dg.gui.Frame;

/**
 * Die erste Klasse. Einstiegspunkt.
 */
public class DungeonGame {

	public static void main(String... args) throws Exception {
		// Spielbrett laden
		GameState.setBoard(DevUtilities.getDevGameboard());
		
		// GUI starten f�r Anzeige des Bretts
		Frame.init();
	}
	
}
