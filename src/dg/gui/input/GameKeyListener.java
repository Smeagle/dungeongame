package dg.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import dg.GameException;
import dg.GameState;
import dg.gui.BoardPanel;
import dg.gui.TitlePanel;

public class GameKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		// title
		if (TitlePanel.getInstance().isVisible()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
		}
		
		// board
		else if (BoardPanel.getInstance().isVisible()) {
			// dialog
			if (Dialog.isVisible()) {
				// escape closes dialog
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (Dialog.isVisible()) {
						Dialog.close();
					}
				}
				
				// button shortcut?
				else {
					try {
						Dialog.getTop().keyPressed(e.getKeyCode());
					} catch (GameException ex) {
						// TODO show nice message dialog
						ex.printStackTrace();
					}
				}
			}
			
			// board
			else {
				// escape opens exit game dialog
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					GameState.stopGame();
				}
				
				// button shortcut?
				else {
					try {
						Menu.getInstance().keyPressed(e.getKeyCode());
					} catch (GameException ex) {
						// TODO show nice message dialog
						ex.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
}
