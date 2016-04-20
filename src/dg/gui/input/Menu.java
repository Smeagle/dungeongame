package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import dg.GameException;
import dg.action.Action;
import dg.action.DebugAgentAction;
import dg.action.DebugPathfindingAction;
import dg.action.StopGameAction;
import dg.action.TestDialogAction;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Menu {

	public static final int MENU_WIDTH = 300;
	
	private static final int BUTTON_Y_OFFSET = 50; // buttons start y
	private static final int BUTTON_X_SPACING = 50; // left to button and button to right
	private static final int BUTTON_Y_SPACING = 20; // between buttons
	private static final int BUTTON_HEIGHT = 50;
	
	private static List<Button> buttons = new ArrayList<Button>();
	
	private static List<Action> permanentActions = new ArrayList<Action>();
	
	public static void init() {
		permanentActions.add(new DebugAgentAction());
		permanentActions.add(new DebugPathfindingAction());
		permanentActions.add(new TestDialogAction());
		permanentActions.add(new StopGameAction());
	}
	
	public static void setActions(Action... actions) {
		clear();
		
		int i = 0;
		for (Action a : permanentActions) {
			Menu.buttons.add(new Button(a, getButtonShape(i++)));
		}
		for (Action a : actions) {
			Menu.buttons.add(new Button(a, getButtonShape(i++)));
		}
		
		BoardPanel.getInstance().repaint();
	}

	public static void clear() {
		buttons.clear();
		BoardPanel.getInstance().repaint();
	}
	
	static List<Button> getButtons() {
		return buttons;
	}
	
	static Shape getButtonShape(int index) {
		int panelWidth = BoardPanel.getInstance().getWidth();
		int menuX = panelWidth - MENU_WIDTH;
		int buttonX = menuX + BUTTON_X_SPACING;
		int buttonY = BUTTON_Y_OFFSET + index * (BUTTON_HEIGHT + BUTTON_Y_SPACING);
		int buttonWidth = panelWidth - BUTTON_X_SPACING - buttonX;
		
		return new Rectangle(buttonX, buttonY, buttonWidth, BUTTON_HEIGHT);
	}

	public static void paintMenu(Graphics2D g2) {
		int panelWidth = BoardPanel.getInstance().getWidth();
		int panelHeight = BoardPanel.getInstance().getHeight();

		int menuX = panelWidth - MENU_WIDTH;
		
		// background
		g2.setColor(Colors.MENU_BACKGROUND);
		g2.fillRect(menuX, 0, MENU_WIDTH, panelHeight);
		
		for (Button b : buttons) {
			b.paint(g2);
		}
	}

	public static void keyPressed(int keyCode) throws GameException {
		for (Button b : buttons) {
			if (b.getAction().getKeyCode() != null && b.getAction().getKeyCode() == keyCode) {
				b.getAction().execute();
				break;
			}
		}
	}
	
	public static void resize() {
		if (buttons != null) {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).setShape(getButtonShape(i));
			}
		}
	}

}
