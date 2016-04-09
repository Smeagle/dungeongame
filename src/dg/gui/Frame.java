package dg.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import dg.gui.input.BoardKeyListener;
import dg.gui.input.Dialog;

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
		
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		        Dialog.resize();    
		    }
		});
		
		this.setVisible(true);
	}
	
}
