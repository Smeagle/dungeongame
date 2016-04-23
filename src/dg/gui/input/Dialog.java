package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import dg.action.Action;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Dialog {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private static final int PADDING = 50;
	
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_SPACING = 10;
	
	private static final int LINE_HEIGHT = 50;
	
	private static Stack<Dialog> stack = new Stack<Dialog>();
	
	private String[] message;
	private List<Button> buttons = null;
	
	
	public Dialog(String[] message, List<Button> buttons) {
		this.buttons = buttons;
		this.message = message;
		resize();
	}
	
	public static void open(String message, Action... actions) {
		if (stack.isEmpty()) {
			BoardPanel.getInstance().removeBoardListeners();
			BoardPanel.getInstance().addMouseListener(DialogMouseListener.getInstance());
		}
		
		List<Button> buttons = new ArrayList<Button>();
		
		for (int i = 0; i < actions.length; i++) {
			buttons.add(new Button(
					actions[i], 
					getButtonShape(actions.length - 1 - i)));
		}
		
		Dialog dialog = new Dialog(
				message.split("\n"),
				buttons);
		
		stack.push(dialog);
	}
	
	/**
	 * Schließt den obersten Dialog
	 */
	public static void close() {
		stack.pop();
		if (stack.isEmpty()) {
			BoardPanel.getInstance().removeMouseListener(DialogMouseListener.getInstance());
			BoardPanel.getInstance().addBoardListeners();
		}
	}
	
	/**
	 * Schließt den gesamten Dialog
	 */
	public static void closeAll() {
		stack.clear();
		BoardPanel.getInstance().removeMouseListener(DialogMouseListener.getInstance());
		BoardPanel.getInstance().addBoardListeners();
	}

	public static void paintDialog(Graphics2D g2) {
		if (stack.isEmpty()) {
			return;
		}
		
		g2.setColor(Colors.DIALOG_BLOCKER);
		g2.fillRect(0, 0, BoardPanel.getInstance().getWidth(), BoardPanel.getInstance().getHeight());
		
		g2.setColor(Colors.DIALOG_BACKGROUND);
		g2.fillRect(BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		
		for (Button button : getButtons()) {
			button.paint(g2);
		}
		
		g2.setColor(Colors.DIALOG_TEXT);
		String[] message = getMessage();
		for (int i = 0; i < message.length; i++) {
			g2.drawString(message[i], BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2 + PADDING + 30 + (i * LINE_HEIGHT));
		}
		
	}

	private static Rectangle getButtonShape(int i) {
		return new Rectangle(
				BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, 
				BoardPanel.getInstance().getHeight() / 2 + HEIGHT / 2 - BUTTON_HEIGHT - PADDING - i * (BUTTON_HEIGHT + BUTTON_SPACING),
				WIDTH - 2 * PADDING,
				BUTTON_HEIGHT);
	}
	
	private static String[] getMessage() {
		return stack.peek().message;
	}

	public static List<Button> getButtons() {
		return stack.peek().buttons;
	}
	
	public static void resize() {
		for (Dialog d : stack) {
			if (d.buttons != null) {
				for (int i = 0; i < d.buttons.size(); i++) {
					d.buttons.get(i).setShape(getButtonShape(d.buttons.size() - 1 - i));
				}
			}
		}
	}

	public static boolean isVisible() {
		return !stack.isEmpty();
	}
	
}
