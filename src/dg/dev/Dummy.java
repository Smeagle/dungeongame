package dg.dev;

import java.awt.event.KeyEvent;

import dg.Affiliation;
import dg.Agent;
import dg.Coordinates;
import dg.Gameboard;
import dg.action.Action;
import dg.event.EventHandler;
import dg.gui.ImageCache;
import dg.gui.animation.AnimationQueue;
import dg.gui.input.Menu;

/**
 * Eine spielerartige Figur, f�r Entwicklungszwecke.
 */
public class Dummy extends Agent {

	public Dummy(Coordinates spawnpoint, Gameboard board) {
		super(spawnpoint, board);
		affiliation = Affiliation.DUNGEON;
	}

	@Override
	public void kill() {
	}

	@Override
	public void takeTurn() {
		// wait for animations to end
		AnimationQueue.onAnimationEnd(new EventHandler() {
			@Override
			public void onEvent() {
				// set menu buttons
				Menu.setActions(new Action("SPACE: Zug beenden", KeyEvent.VK_SPACE) {
					@Override
					public void execute() {
						finishTurn();
					}
				});
			}
		});
	}

	@Override
	public String getImage() {
		return ImageCache.DUMMY;
	}

}
