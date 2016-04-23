package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Stack;

import dg.GameException;
import dg.action.Action;
import dg.gui.BoardPanel;
import dg.gui.Colors;

public class Dialog extends ButtonContainer {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private static final int PADDING = 50;
	
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_SPACING = 10;
	
	private static final int LINE_HEIGHT = 50;
	
	private static Stack<Dialog> stack = new Stack<Dialog>();
	
	private String[] message;
	
	
	public Dialog(String[] message, List<Button> buttons) {
		this.addButtons(buttons);
		this.message = message;
	}
	
	public Dialog(String[] message, Button... buttons) {
		this.addButtons(buttons);
		this.message = message;
	}
	
	public static void open(String message, Button... buttons) {
		if (stack.isEmpty()) {
			BoardPanel.getInstance().removeBoardListeners();
			BoardPanel.getInstance().addMouseListener(DialogMouseListener.getInstance());
		}
		
		if (buttons == null || buttons.length == 0) {
			buttons = new Button[] {
				new Button("Schlieﬂen", new Action() {
					@Override
					public void execute() throws GameException {
						Dialog.close();
					}
				})
			};
		}
		
		Dialog dialog = new Dialog(
				message.split("\n"),
				buttons);
		
		stack.push(dialog);
	}
	
	/**
	 * Schlieﬂt den obersten Dialog
	 */
	public static void close() {
		stack.pop();
		if (stack.isEmpty()) {
			BoardPanel.getInstance().removeMouseListener(DialogMouseListener.getInstance());
			BoardPanel.getInstance().addBoardListeners();
		}
	}
	
	/**
	 * Schlieﬂt den gesamten Dialog
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
		
		getTop().paintButtons(g2);
		
		g2.setColor(Colors.DIALOG_TEXT);
		String[] message = getTop().message;
		for (int i = 0; i < message.length; i++) {
			g2.drawString(message[i], BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, BoardPanel.getInstance().getHeight() / 2 - HEIGHT / 2 + PADDING + 30 + (i * LINE_HEIGHT));
		}
		
	}

	@Override
	protected Rectangle getButtonShape(int i) {
		return new Rectangle(
				BoardPanel.getInstance().getWidth() / 2 - WIDTH / 2 + PADDING, 
				BoardPanel.getInstance().getHeight() / 2 + HEIGHT / 2 - BUTTON_HEIGHT - PADDING - i * (BUTTON_HEIGHT + BUTTON_SPACING),
				WIDTH - 2 * PADDING,
				BUTTON_HEIGHT);
	}
	
	public static boolean isVisible() {
		return !stack.isEmpty();
	}

	public static Dialog getTop() {
		return stack.peek();
	}

	public static void resize() {
		for (Dialog d : stack) {
			d.resizeButtons();
		}
	}
	
}
