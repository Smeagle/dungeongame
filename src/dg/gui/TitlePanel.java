package dg.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import dg.GameState;

/**
 * Das Panel für den Titelbildschirm bzw. Hauptmenü.
 */
public class TitlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static TitlePanel instance = null;
	
	public static TitlePanel getInstance() {
		if (instance == null) {
			instance = new TitlePanel();
		}
		return instance;
	}
	
	private TitlePanel() {
		this.setBackground(Colors.TITLE_PANEL_BACKGROUND);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Frame.getInstance().showPanel(BoardPanel.getInstance());
				GameState.startGame();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(ImageCache.getImage(ImageCache.TITLE_SCREEN), 0, 0, getWidth(), getHeight(), null);
	}
	
}
