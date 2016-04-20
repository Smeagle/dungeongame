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
	
	private Action action;
	
	private Shape shape;
	
	private Rectangle2D stringBounds = null;
	
	private int padding;
	
	private Color fgColor;
	private Color bgColor;
	private int font;
	
	public Button(Action action, Shape shape, int padding, Color fgColor, Color bgColor, int font) {
		this.action = action;
		this.shape = shape;
		this.padding = padding;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.font = font;
	}
	
	public Button(Action action, Shape shape) {
		this.action = action;
		this.shape = shape;
		this.padding = DEFAULT_PADDING;
		this.fgColor = Colors.BUTTON_DEFAULT_TEXT_COLOR;
		this.bgColor = Colors.BUTTON_DEFAULT_BACKGROUND_COLOR;
		this.font = Fonts.BUTTON_DEFAULT;
	}
	
	public void paint(Graphics2D g2) {
		g2.setColor(bgColor);
		g2.fill(shape);
		
		g2.setColor(fgColor);
		g2.setFont(Fonts.getFont(font));
		if (stringBounds == null) {
			stringBounds = Fonts.getFont(font).getStringBounds(action.getName(), g2.getFontRenderContext());
		}
		g2.drawString(action.getName(), 
				(int) (shape.getBounds2D().getX() + shape.getBounds2D().getWidth() / 2 - stringBounds.getWidth() / 2), 
				(int) (shape.getBounds2D().getY() + shape.getBounds2D().getHeight() - padding));
	}

	public boolean click(MouseEvent e) throws GameException {
		if (shape.contains(e.getX(), e.getY())) {
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
	
}
