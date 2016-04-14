package dg.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import dg.GameException;
import dg.gui.BoardPanel;

public class GameKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		if (BoardPanel.getInstance().isVisible()) {
			if (Dialog.isVisible()) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Dialog.close();
				}
				return;
			}
			
			try {
				Menu.keyPressed(e.getKeyCode());
			} catch (GameException ex) {
				// TODO show nice message dialog
				ex.printStackTrace();
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
