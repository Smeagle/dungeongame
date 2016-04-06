package dg.gui.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		Dialog.close();
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
