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
import dg.action.TestDialogAction;
import dg.gui.BoardPanel;
import dg.gui.Colors;
import dg.gui.Fonts;

public class Menu {

	public static final int MENU_WIDTH = 300;
	
	private static final int BUTTON_Y_OFFSET = 50; // buttons start y
	private static final int BUTTON_X_SPACING = 50; // left to button and button to right
	private static final int BUTTON_Y_SPACING = 20; // between buttons
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_PADDING = 10;
	
	private static List<Action> actions = new ArrayList<Action>();
	
	private static List<Action> permanentActions = new ArrayList<Action>();
	
	public static void init() {
		permanentActions.add(new DebugAgentAction());
		permanentActions.add(new DebugPathfindingAction());
		permanentActions.add(new TestDialogAction());
	}
	
	public static void setActions(Action... actions) {
		clear();
		
		for (Action a : permanentActions) {
			Menu.actions.add(a);
		}
		for (Action a : actions) {
			Menu.actions.add(a);
		}
		
		BoardPanel.getInstance().repaint();
	}

	public static void clear() {
		actions.clear();
		BoardPanel.getInstance().repaint();
	}
	
	static List<Action> getActions() {
		return actions;
	}
	
	static Shape getButtonShape(int actionIndex) {
		int panelWidth = BoardPanel.getInstance().getWidth();
		int menuX = panelWidth - MENU_WIDTH;
		int buttonX = menuX + BUTTON_X_SPACING;
		int buttonY = BUTTON_Y_OFFSET + actionIndex * (BUTTON_HEIGHT + BUTTON_Y_SPACING);
		int buttonWidth = panelWidth - BUTTON_X_SPACING - buttonX;
		
		return new Rectangle(buttonX, buttonY, buttonWidth, BUTTON_HEIGHT);
	}

	public static void paintMenu(Graphics2D g2) {
		int panelWidth = BoardPanel.getInstance().getWidth();
		int panelHeight = BoardPanel.getInstance().getHeight();

		int menuX = panelWidth - MENU_WIDTH;
		int buttonX = menuX + BUTTON_X_SPACING;
		int buttonNameX = buttonX + BUTTON_PADDING;
		
		// background
		g2.setColor(Colors.MENU_BACKGROUND);
		g2.fillRect(menuX, 0, MENU_WIDTH, panelHeight);
		
		for (int i = 0; i < actions.size(); i++) {
			Action action = actions.get(i);

			int buttonY = BUTTON_Y_OFFSET + i * (BUTTON_HEIGHT + BUTTON_Y_SPACING);
			
			g2.setColor(Colors.MENU_BUTTON_BACKGROUND);
			g2.fill(getButtonShape(i));
			
			int buttonNameY = buttonY + BUTTON_HEIGHT - BUTTON_PADDING;
			
			g2.setColor(Colors.MENU_BUTTON_LABEL);
			g2.setFont(Fonts.getFont(Fonts.BUTTON));
			g2.drawString(action.getName(), buttonNameX, buttonNameY);
		}
	}

	public static void keyPressed(int keyCode) throws GameException {
		for (Action a : actions) {
			if (a.getKeyCode() != null && a.getKeyCode() == keyCode) {
				a.execute();
				break;
			}
		}
	}

}
