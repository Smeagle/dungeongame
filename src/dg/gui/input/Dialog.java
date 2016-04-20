package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import dg.action.Action;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Dialog {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private static final int PADDING = 50;
	
	private static final int BUTTON_HEIGHT = 50;
	
	private static final int LINE_HEIGHT = 50;
	
	private static String[] message;
	
	private static Button button = null;
	
	private static boolean visible = false;
	
	public static void open(String message, Action action) {
		BoardPanel.getInstance().removeBoardListeners();
		BoardPanel.getInstance().addMouseListener(DialogMouseListener.getInstance());
		
		Dialog.message = message.split("\n");
		
		Dialog.button = new Button(
				action, 
				new Rectangle(
						BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, 
						BoardPanel.getInstance().getHeight() / 2 + HEIGHT / 2 - BUTTON_HEIGHT - PADDING,
						WIDTH - 2 * PADDING,
						BUTTON_HEIGHT));
		
		resize();
		
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
		g2.fillRect(BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		
		button.paint(g2);
		
		g2.setColor(Colors.DIALOG_TEXT);
		for (int i = 0; i < message.length; i++) {
			g2.drawString(message[i], BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2 + PADDING + 30 + (i * LINE_HEIGHT));
		}
		
	}

	public static boolean isVisible() {
		return visible;
	}
	
	public static Button getButton() {
		return button;
	}

	public static void resize() {
		if (button != null) {
			button.setShape(new Rectangle(
					BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, 
					BoardPanel.getInstance().getHeight() / 2 + HEIGHT / 2 - BUTTON_HEIGHT - PADDING,
					WIDTH - 2 * PADDING,
					BUTTON_HEIGHT));
		}
	}
	
}
