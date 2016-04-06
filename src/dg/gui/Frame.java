package dg.gui;

import javax.swing.JFrame;

import dg.gui.input.BoardKeyListener;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static Frame instance = null;
	
	public static Frame getInstance() {
		if (instance == null) {
			instance = new Frame();
		}
		return instance;
	}
	
	private Frame() {
		this.add(BoardPanel.getInstance());
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		
		this.addKeyListener(new BoardKeyListener());
		
		this.pack();
		this.setSize(GUIUtils.getFullScreenBounds());
		this.setVisible(true);
	}
	
}
