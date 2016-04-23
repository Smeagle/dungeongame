package dg.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dg.gui.input.GameKeyListener;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "DungeonGame";
	
	private static Frame instance = null;
	
	private static JPanel activePanel = null;
	
	public static Frame getInstance() {
		if (instance == null) {
			instance = new Frame();
		}
		return instance;
	}
	
	private void hideAllPanels() {
		TitlePanel.getInstance().setVisible(false);
		BoardPanel.getInstance().setVisible(false);
	}
	
	public void showPanel(JPanel panel) {
		hideAllPanels();
		activePanel = panel;
		activePanel.setSize(this.getSize());
		activePanel.setVisible(true);
	}
	
	private Frame() {
		this.setLayout(null);
		this.setTitle(TITLE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setUndecorated(true);
		
		this.add(BoardPanel.getInstance());
		this.add(TitlePanel.getInstance());
		
		this.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		
		this.addKeyListener(new GameKeyListener());
		
		this.setSize(GUIUtils.getFullScreenBounds());
		
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		        activePanel.setSize(Frame.getInstance().getSize());
		    }
		});
		
		showPanel(TitlePanel.getInstance());
		
		// full screen
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = ge.getDefaultScreenDevice();
		if (device.isFullScreenSupported()) {
			device.setFullScreenWindow(this);
		}
		
		this.setVisible(true);
	}
	
}
