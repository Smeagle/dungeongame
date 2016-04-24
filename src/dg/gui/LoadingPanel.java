package dg.gui;

import javax.swing.JPanel;

public class LoadingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static LoadingPanel instance = null;
	
	public static LoadingPanel getInstance() {
		if (instance == null) {
			instance = new LoadingPanel();
		}
		return instance;
	}
	
	private LoadingPanel() {
		this.setBackground(Colors.LOADING_PANEL_BACKGROUND);
	}
	
}
