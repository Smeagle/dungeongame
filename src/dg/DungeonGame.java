package dg;

import java.util.LinkedList;
import javax.swing.SwingUtilities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dg.dev.DevUtilities;
import dg.dev.Dummy;
import dg.gui.Frame;
import dg.gui.animation.AnimationQueue;
import dg.gui.animation.Repainter;
import dg.gui.input.Menu;

/**
 * Die erste Klasse. Einstiegspunkt.
 */
public class DungeonGame {

	public static void main(String... args) {
		// Spielbrett laden
		GameState.setBoard(DevUtilities.getRandomGameboard(10));
		
		// Figuren drauf stellen
		GameState.getBoard().addAgent(new Dummy(new Coordinates(1, 1), GameState.getBoard()));
		DevUtilities.addDevGuards();
		
		// GUI starten fï¿½r Anzeige des Bretts
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Menu.init();
					
					Frame.getInstance();
					
					AnimationQueue.init();
					Repainter.init();
					
					// Spielablauf an schmeißen
					GameState.startGame();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
