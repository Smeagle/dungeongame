package dg;

import java.util.LinkedList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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

	public static void playerKilled() {
		// TODO Auto-generated method stub

		throw new NotImplementedException();
	}

	public static Direction getPlayerMove(LinkedList<Coordinates> moveOptions) {
		// TODO Auto-generated method stub

		throw new NotImplementedException();
	}

	public static void levelComplete() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();

	}
}
