package dg.dev;

import java.awt.event.KeyEvent;

import dg.Affiliation;
import dg.Agent;
import dg.Coordinates;
import dg.GameState;
import dg.Gameboard;
import dg.action.Action;
import dg.event.EventHandler;
import dg.gui.ImageCache;
import dg.gui.animation.AnimationQueue;
import dg.gui.input.Menu;

/**
 * Eine spielerartige Figur, für Entwicklungszwecke.
 */
public class Dummy extends Agent {

	public Dummy(Coordinates spawnpoint, Gameboard board) {
		super(spawnpoint, board);
		affiliation = Affiliation.DUNGEON;
		computerControlled = false;
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
						GameState.nextAgentsTurn();
					}
				});
			}
		});
	}

	@Override
	public String getImage() {
		return ImageCache.DUMMY;
	}

	@Override
	public void endTurn() {
		movesLeft = 0;
	}

}
