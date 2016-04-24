package dg.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import dg.GameException;
import dg.action.ExitGameAction;
import dg.action.NewGameAction;
import dg.gui.input.Button;

/**
 * Das Panel für den Titelbildschirm bzw. Hauptmenü.
 */
public class TitlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int BUTTON_OFFSET_PERCENT = 82;
	private static final int BUTTON_SPACING = 10;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 50;
	
	private static TitlePanel instance = null;
	
	private Button newGameButton;
	private Button endButton;
	
	public static TitlePanel getInstance() {
		if (instance == null) {
			instance = new TitlePanel();
		}
		return instance;
	}
	
	private TitlePanel() {
		this.setBackground(Colors.TITLE_PANEL_BACKGROUND);
		
		newGameButton = new Button("Neues Spiel", new NewGameAction());
		newGameButton.setShape(getButtonShape(0));
		endButton = new Button("Verlassen", new ExitGameAction());
		endButton.setShape(getButtonShape(1));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					if (newGameButton.click(e)) {
						return;
					}
					endButton.click(e);
				}
				catch (GameException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	private Rectangle getButtonShape(int i) {
		return new Rectangle(
				(int) (getWidth() / 2. - BUTTON_WIDTH / 2.),
				(int) (getHeight() / 100. * BUTTON_OFFSET_PERCENT + i * (BUTTON_HEIGHT + BUTTON_SPACING)),
				BUTTON_WIDTH,
				BUTTON_HEIGHT);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Image image = ImageCache.getImage(ImageCache.TITLE_SCREEN);
		g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		GUIUtils.setRenderingHints(g2);
		
		newGameButton.paint(g2);
		endButton.paint(g2);
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		resize();
	}
	
	public void resize() {
		if (newGameButton != null) {
			newGameButton.setShape(getButtonShape(0));
		}
		if (endButton != null) {
			endButton.setShape(getButtonShape(1));
		}
	}
	
}
