package dg.gui.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import dg.Coordinates;
import dg.GameState;
import dg.event.EventHandler;
import dg.event.SelectionEventHandler;
import dg.gui.BoardPanel;

public class BoardMouseListener implements MouseMotionListener, MouseListener, MouseWheelListener {

	private static BoardMouseListener instance = null;
	
	private static final double SCALE_STEP = 0.1;
	private static final double SCALE_MIN = 0.3;
	private static final double SCALE_MAX = 2;
	
	private static double oldTranslateX = 0;
	private static double oldTranslateY = 0;
	private static double pressX = 0;
	private static double pressY = 0;
	
	private static boolean pressOnMenu = false;
	
	public static BoardMouseListener getInstance() {
		if (instance == null) {
			instance = new BoardMouseListener();
		}
		return instance;
	}
	
	private BoardMouseListener() {};
	
	@Override
	public void mouseMoved(MouseEvent e) {
		BoardPanel.mouseX = e.getX();
		BoardPanel.mouseY = e.getY();
		
		if (e.getX() >= BoardPanel.getInstance().getWidth() - Menu.MENU_WIDTH) {
			GameState.setMouseoverCoordinates(null);
		}
		else {
			if (Selection.isSelectionMode()) {
				BoardPanel.updateMouseoverCoordinates();
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressOnMenu) {
			return;
		}
		
		// bei einem Rechts-Drag das Bild verschieben
		if ((e.getModifiersEx() & MouseEvent.getMaskForButton(MouseEvent.BUTTON3)) > 0) {
			BoardPanel.translateX = oldTranslateX + e.getX() - pressX;
			BoardPanel.translateY = oldTranslateY + e.getY() - pressY;
			BoardPanel.getInstance().repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() >= BoardPanel.getInstance().getWidth() - Menu.MENU_WIDTH) {
			pressOnMenu = true;
			return;
		}
		
		pressOnMenu = false;
		
		// bei einem Rechtsklick die Position merken, für das Draggen (Bild verschieben)
		if ((e.getModifiersEx() & MouseEvent.getMaskForButton(MouseEvent.BUTTON3)) > 0) {
			oldTranslateX = BoardPanel.translateX;
			oldTranslateY = BoardPanel.translateY;
			pressX = e.getX();
			pressY = e.getY();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getX() >= BoardPanel.getInstance().getWidth() - Menu.MENU_WIDTH) {
			return;
		}
		
		// Das Mausrad verändert den Zoom
		BoardPanel.scale -= e.getPreciseWheelRotation() * SCALE_STEP;
		if (BoardPanel.scale > SCALE_MAX) {
			BoardPanel.scale = SCALE_MAX;
		}
		if (BoardPanel.scale < SCALE_MIN) {
			BoardPanel.scale = SCALE_MIN;
		}
		BoardPanel.getInstance().repaint();
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
	public void mouseReleased(MouseEvent e) {
		if (e.getX() >= BoardPanel.getInstance().getWidth() - Menu.MENU_WIDTH) {
			return;
		}
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Coordinates c = GameState.getMouseoverCoordinates();
			if (c != null) {
				if (Selection.isSelectionMode()) {
					EventHandler handler = Selection.getEventHandler();
					if (handler instanceof SelectionEventHandler) {
						((SelectionEventHandler) handler).onEvent(c);
					}
					else {
						handler.onEvent();
					}
				}
			}
			BoardPanel.getInstance().repaint();
		}
	}

}
