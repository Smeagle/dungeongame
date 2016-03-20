package dg.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GUIUtils {

	public static Dimension getFullScreenBounds() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		return new Dimension(screenWidth, screenHeight);
	}
	
}
