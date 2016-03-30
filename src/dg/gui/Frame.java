package dg.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
		Menu.init();
		
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
		this.addKeyListener(new BoardKeyListener());
		
		this.pack();
		this.setSize(GUIUtils.getFullScreenBounds());
		this.setVisible(true);
	}
	
}
