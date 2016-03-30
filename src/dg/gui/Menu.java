package dg.gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menu {

	public static final int MENU_WIDTH = 300;
	
	private static final int BUTTON_Y_OFFSET = 50; // buttons start y
	private static final int BUTTON_X_SPACING = 50; // left to button and button to right
	private static final int BUTTON_Y_SPACING = 20; // between buttons
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_PADDING = 10;
	
	private static final int BUTTON_FONT_TYPE = Font.TRUETYPE_FONT;
	private static final String BUTTON_FONT_FILE = "fonts/Amatic-Bold.ttf";
	private static final float BUTTON_FONT_SIZE = 40;
	
	private static Font buttonFont = null;
			
	private static List<Action> actions = new ArrayList<Action>();
	
	public static void init() throws Exception {
		buttonFont = Font.createFont(BUTTON_FONT_TYPE, new File(BUTTON_FONT_FILE)).deriveFont(BUTTON_FONT_SIZE);
	}
	
	public static void setActions(Action... actions) {
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
			g2.setFont(buttonFont);
			g2.drawString(action.getName(), buttonNameX, buttonNameY);
		}
	}

}
