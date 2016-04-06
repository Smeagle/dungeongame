package dg.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import dg.GameException;

public class BoardKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		if (Dialog.isVisible()) {
			Dialog.close();
			return;
		}
		
		try {
			Menu.keyPressed(e.getKeyCode());
		} catch (GameException ex) {
			// TODO show nice message dialog
			ex.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
}
