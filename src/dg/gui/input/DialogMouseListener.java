package dg.gui.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import dg.GameException;

public class DialogMouseListener implements MouseListener {

	private static DialogMouseListener instance = null;
	
	public static DialogMouseListener getInstance() {
		if (instance == null) {
			instance = new DialogMouseListener();
		}
		return instance;
	}
	
	private DialogMouseListener() {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			try {
				Dialog.getTop().click(e);
			} catch (GameException ex) {
				// TODO show nice message
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

}
