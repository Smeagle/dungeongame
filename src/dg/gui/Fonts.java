package dg.gui;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Fonts {

	public static final int MENU_BUTTON = 0;
	public static final int DIALOG_BUTTON = 1;
	public static final int SELECTION_HELP = 2;
	
	private static final int BUTTON_FONT_TYPE = Font.TRUETYPE_FONT;
	private static final String BUTTON_FONT_FILE = "fonts/Amatic-Bold.ttf";
	private static final float BUTTON_FONT_SIZE = 40;
	
	private static final int SELECTION_HELP_FONT_TYPE = Font.TRUETYPE_FONT;
	private static final String SELECTION_HELP_FONT_FILE = "fonts/Amatic-Bold.ttf";
	private static final float SELECTION_HELP_FONT_SIZE = 40;
	
	private static Map<Integer, Font> cache = new HashMap<Integer, Font>();
	
	public static Font getFont(int fontKey) {
		try {
			if (!cache.containsKey(fontKey)) {
				Font font = null;
				switch (fontKey) {
				case MENU_BUTTON:
				case DIALOG_BUTTON:
					font = Font.createFont(BUTTON_FONT_TYPE, new File(BUTTON_FONT_FILE)).deriveFont(BUTTON_FONT_SIZE);
					break;
				case SELECTION_HELP:
					font = Font.createFont(SELECTION_HELP_FONT_TYPE, new File(SELECTION_HELP_FONT_FILE)).deriveFont(SELECTION_HELP_FONT_SIZE);
					break;
				}
				if (font != null) {
					cache.put(fontKey, font);
				}
			}
			return cache.get(fontKey);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
