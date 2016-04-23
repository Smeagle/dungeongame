package dg.gui.input;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import dg.GameException;
import dg.action.Action;
import dg.gui.Colors;
import dg.gui.Fonts;

public class Button {

	private static final int DEFAULT_PADDING = 10;
	
	private Shape shape = null;
	
	private Rectangle2D stringBounds = null;
	private int padding;
	private Color fgColor;
	private Color bgColor;
	private int font;
	
	private Action action;
	private boolean visible = true; // wird nicht gezeichnet, aber funktioniert trotzdem genauso per Klick!
	private boolean keyPressOnly = false; // wird nicht gezeichnet und ist nicht klickbar, aber Taste funktioniert 
	private String name; // The Label to show on the button for the action
	private Integer keyCode = null;
	
	public Button(String name, Action action, Integer keyCode) {
		this.name = name;
		this.action = action;
		this.keyCode = keyCode;
		this.padding = DEFAULT_PADDING;
		this.fgColor = Colors.BUTTON_DEFAULT_TEXT_COLOR;
		this.bgColor = Colors.BUTTON_DEFAULT_BACKGROUND_COLOR;
		this.font = Fonts.BUTTON_DEFAULT;
	}
	
	public Button(String name, Action action) {
		this(name, action, null);
	}
	
	public void paint(Graphics2D g2) {
		if (!visible || keyPressOnly || shape == null) {
			return;
		}
		
		g2.setColor(bgColor);
		g2.fill(shape);
		
		g2.setColor(fgColor);
		g2.setFont(Fonts.getFont(font));
		if (stringBounds == null) {
			stringBounds = Fonts.getFont(font).getStringBounds(name, g2.getFontRenderContext());
		}
		g2.drawString(name, 
				(int) (shape.getBounds2D().getX() + shape.getBounds2D().getWidth() / 2 - stringBounds.getWidth() / 2), 
				(int) (shape.getBounds2D().getY() + shape.getBounds2D().getHeight() - padding));
	}

	public boolean click(MouseEvent e) throws GameException {
		if (keyPressOnly || shape == null) {
			return false;
		}
		
		if (shape.contains(e.getX(), e.getY())) {
			action.execute();
			return true;
		}
		return false;
	}
	
	public boolean keyPress(int keyCode) throws GameException {
		if (this.keyCode != null && this.keyCode.equals(keyCode)) {
			action.execute();
			return true;
		}
		return false;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Shape getShape() {
		return shape;
	}

	public Action getAction() {
		return action;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setKeyPressOnly(boolean keyPressOnly) {
		this.keyPressOnly = keyPressOnly;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isKeyPressOnly() {
		return keyPressOnly;
	}

}
