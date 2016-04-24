package dg;

import javax.swing.SwingUtilities;

import dg.gui.Frame;
import dg.gui.animation.AnimationQueue;
import dg.gui.animation.Repainter;
import dg.gui.input.Menu;

/**
 * Die erste Klasse. Einstiegspunkt.
 */
public class DungeonGame {

	public static void main(String... args) {
		// GUI starten fuer Anzeige des Bretts
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Menu.init();
					
					Frame.getInstance();
					
					AnimationQueue.init();
					Repainter.init();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
