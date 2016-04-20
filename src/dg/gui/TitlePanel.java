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
import dg.action.StartGameAction;
import dg.gui.input.Button;

/**
 * Das Panel für den Titelbildschirm bzw. Hauptmenü.
 */
public class TitlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int BUTTON_OFFSET_PERCENT = 66;
	private static final int BUTTON_SPACING = 20;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 50;
	
	private static TitlePanel instance = null;
	
	private Button startButton;
	private Button endButton;
	
	public static TitlePanel getInstance() {
		if (instance == null) {
			instance = new TitlePanel();
		}
		return instance;
	}
	
	private TitlePanel() {
		this.setBackground(Colors.TITLE_PANEL_BACKGROUND);
		
		startButton = new Button(new StartGameAction(), getButtonShape(0));
		endButton = new Button(new ExitGameAction(), getButtonShape(1));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					if (startButton.click(e)) {
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
		
		Image image = ImageCache.getImage(ImageCache.ROGUERIGO);
		g2.drawImage(image, (int) (getWidth() / 2. - image.getWidth(null) / 2.), (int) (getHeight() / 100. * 40 - image.getHeight(null) / 2.), null);
		
		GUIUtils.setRenderingHints(g2);
		
		startButton.paint(g2);
		endButton.paint(g2);
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		resize();
	}
	
	public void resize() {
		if (startButton != null) {
			startButton.setShape(getButtonShape(0));
		}
		if (endButton != null) {
			endButton.setShape(getButtonShape(1));
		}
	}
	
}
