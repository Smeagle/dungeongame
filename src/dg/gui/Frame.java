package dg.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import dg.GameState;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Frame instance = null;
	
	public static Frame getInstance() {
		if (instance == null) {
			instance = new Frame();
		}
		return instance;
	}
	
	/**
	 * Beim Start des Spiel aufzurufen, um die GUI zu laden
	 */
	public static void init() throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private Frame() {
		this.add(BoardPanel.getInstance());
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		
		// exit on escape key
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				GameState.getActiveAgent().onKeyPressed(e);
			}
		});
		
		this.pack();
		this.setSize(GUIUtils.getFullScreenBounds());
		this.setVisible(true);
	}
	
}
