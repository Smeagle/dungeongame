package dg;

import dg.dev.DevUtilities;
import dg.dev.Dummy;
import dg.gui.Frame;

/**
 * Die erste Klasse. Einstiegspunkt.
 */
public class DungeonGame {

	public static void main(String... args) throws Exception {
		
		// Spielbrett laden
		GameState.setBoard(DevUtilities.getRandomGameboard(5));
		
		// Figuren drauf stellen
		GameState.getBoard().addAgent(new Dummy(new Coordinates(1, 1), GameState.getBoard()));
		DevUtilities.addDevGuards();
		
		// GUI starten fï¿½r Anzeige des Bretts
		Frame.init();
		
		// Spielablauf an schmeißen
		GameState.startGame();
		
		// for demo and test a selection listener:
		GameState.addSelectionListener(new Callback() {
			@Override
			public void execute() {
				System.out.println("selected coordinates are " + GameState.getSelectionCoordinates());
			}
		});
		
	}
}
