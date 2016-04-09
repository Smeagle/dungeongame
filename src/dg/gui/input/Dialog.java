package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import dg.action.Action;
import dg.gui.BoardPanel;
import dg.gui.Colors;
import dg.gui.Fonts;

public class Dialog {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private static final int PADDING = 50;
	
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_PADDING = 10;
	
	private static final int LINE_HEIGHT = 50;
	
	private static String[] message;
	
	private static Action action = null;
	
	private static Shape buttonShape = null;
	
	private static Rectangle2D stringBounds = null;
	
	private static boolean visible = false;
	
	public static void open(String message, Action action) {
		BoardPanel.getInstance().removeBoardListeners();
		BoardPanel.getInstance().addMouseListener(DialogMouseListener.getInstance());
		
		Dialog.message = message.split("\n");
		Dialog.action = action;
		stringBounds = null;
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
		
		g2.setColor(Colors.DIALOG_BUTTON_BACKGROUND);
		g2.fill(buttonShape);
		
		g2.setColor(Colors.DIALOG_BUTTON_TEXT);
		g2.setFont(Fonts.getFont(Fonts.DIALOG_BUTTON));
		if (stringBounds == null) {
			stringBounds = Fonts.getFont(Fonts.DIALOG_BUTTON).getStringBounds(action.getName(), g2.getFontRenderContext());
		}
		g2.drawString(action.getName(), 
				(int) (BoardPanel.getInstance().getWidth() / 2 - stringBounds.getWidth() / 2), 
				(int) (buttonShape.getBounds2D().getY() + BUTTON_HEIGHT - BUTTON_PADDING));
		
		g2.setColor(Colors.DIALOG_TEXT);
		for (int i = 0; i < message.length; i++) {
			g2.drawString(message[i], BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2 + PADDING + 30 + (i * LINE_HEIGHT));
		}
		
	}

	public static boolean isVisible() {
		return visible;
	}

	public static Shape getButtonShape() {
		return buttonShape;
	}

	public static Action getAction() {
		return action;
	}

	public static void resize() {
		buttonShape = new Rectangle(
				BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, 
				BoardPanel.getInstance().getHeight() / 2 + HEIGHT / 2 - BUTTON_HEIGHT - PADDING,
				WIDTH - 2 * PADDING,
				BUTTON_HEIGHT);
	}
	
}
