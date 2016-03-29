package dg.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import dg.GameState;

public class BoardKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_D:
			GameState.setDebugMode(!GameState.isDebugMode());
			BoardPanel.getInstance().repaint();
			break;
		}
		
		GameState.getActiveAgent().onKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
}
