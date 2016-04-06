package dg.gui.input;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import dg.action.Action;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Dialog {

	private static final int PADDING_X = 700; // can be improved later
	private static final int PADDING_Y = 300; // can be improved later
	
	private static final int SPACING = 50;
	
	private static final int LINE_HEIGHT = 50;
	
	private static String[] message;
	
	private static List<Action> actions = new ArrayList<Action>();
	
	private static boolean visible = false;
	
	public static void open(String message, Action... actions) {
		BoardPanel.getInstance().removeBoardListeners();
		BoardPanel.getInstance().addMouseListener(DialogMouseListener.getInstance());
		
		Dialog.message = message.split("\n");
		Dialog.actions.clear();
		for (Action a : actions) {
			Dialog.actions.add(a);
		}
		visible = true;
	}
	
	public static void close() {
		visible = false;
		
		BoardPanel.getInstance().removeMouseListener(DialogMouseListener.getInstance());
		BoardPanel.getInstance().addBoardListeners();
	}

	public static void paintDialog(Graphics2D g2) {
		if (!visible) {
			return;
		}

		g2.setColor(Colors.DIALOG_BLOCKER);
		g2.fillRect(0, 0, BoardPanel.getInstance().getWidth(), BoardPanel.getInstance().getHeight());
		
		g2.setColor(Colors.DIALOG_BACKGROUND);
		g2.fillRect(PADDING_X, PADDING_Y, BoardPanel.getInstance().getWidth() - 2 * PADDING_X, BoardPanel.getInstance().getHeight() - 2 * PADDING_Y);
		
		g2.setColor(Colors.DIALOG_TEXT);
		for (int i = 0; i < message.length; i++) {
			g2.drawString(message[i], PADDING_X + SPACING, PADDING_Y + SPACING + 30 + (i * LINE_HEIGHT));
		}
		
	}

	public static boolean isVisible() {
		return visible;
	}
	
}
