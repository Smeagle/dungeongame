package dg.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import dg.GameException;

public class MenuMouseListener implements MouseMotionListener, MouseListener {

	private static MenuMouseListener instance = null;
	
	public static MenuMouseListener getInstance() {
		if (instance == null) {
			instance = new MenuMouseListener();
		}
		return instance;
	}
	
	private MenuMouseListener() {};
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getX() <= BoardPanel.getInstance().getWidth() - Menu.MENU_WIDTH) {
			return;
		}
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			// button clicked?
			List<Action> actions = Menu.getActions();
			for (int i = 0; i < actions.size(); i++) {
				if (Menu.getButtonShape(i).contains(e.getX(), e.getY())) {
					try {
						actions.get(i).execute();
					}
					catch (GameException ex) {
						//TODO show nice message dialog
						ex.printStackTrace();
					}
					break;
				}
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

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
