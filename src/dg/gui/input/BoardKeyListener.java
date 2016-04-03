package dg.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import dg.GameException;

public class BoardKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
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
