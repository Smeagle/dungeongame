package dg.gui.animation;

import dg.gui.BoardPanel;

public class Repainter extends Thread {

	private static final int SLEEP_TIME = 20;
	
	private static Repainter instance = null;
	
	public static void init() {
		instance = new Repainter();
		instance.start();
	}
	
	private Repainter() {}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
			}
			BoardPanel.getInstance().repaint();
		}
	}
	
}
