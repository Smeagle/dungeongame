package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.events.KeyEvent;

import dg.action.DebugAgentAction;
import dg.action.DebugPathfindingAction;
import dg.action.StopGameAction;
import dg.action.TestDialogAction;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Menu extends ButtonContainer {

	public static final int MENU_WIDTH = 300;
	
	private static final int BUTTON_Y_OFFSET = 50; // buttons start y
	private static final int BUTTON_X_SPACING = 50; // left to button and button to right
	private static final int BUTTON_Y_SPACING = 20; // between buttons
	private static final int BUTTON_HEIGHT = 50;
	
	private static Menu instance = new Menu();
	
	private static List<Button> permanentButtons = new ArrayList<Button>();
	
	private Menu() {
	}
	
	public static Menu getInstance() {
		return instance;
	}
	
	public static void init() {
		permanentButtons.add(new Button("d: Agent debuggen", new DebugAgentAction(), KeyEvent.VK_D));
		permanentButtons.add(new Button("w: Wegfindung debuggen", new DebugPathfindingAction(), KeyEvent.VK_W));
		permanentButtons.add(new Button("Dialog testen", new TestDialogAction()));
		permanentButtons.add(new Button("Spiel beenden", new StopGameAction()));
	}
	
	public static void setButtons(Button... buttons) {
		instance.clearButtons();
		instance.addButtons(permanentButtons);
		instance.addButtons(buttons);
		
		BoardPanel.getInstance().repaint();
	}

	@Override
	protected Shape getButtonShape(int index) {
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
		
		instance.paintButtons(g2);
	}

}
