package dg.gui.input;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dg.GameException;

public abstract class ButtonContainer {

	private List<Button> buttons = new ArrayList<Button>();
	private int visibleButtonIndex = 0;
	
	protected void addButtons(Collection<Button> button) {
		for (Button b : button) {
			addButton(b);
		}
	}
	
	protected void addButtons(Button... button) {
		for (Button b : button) {
			addButton(b);
		}
	}
	
	protected void addButton(Button button) {
		if (button.isVisible() && !button.isKeyPressOnly()) {
			button.setShape(getButtonShape(visibleButtonIndex));
			visibleButtonIndex++;
		}
		buttons.add(button);
	}
	
	public void clearButtons() {
		buttons.clear();
		visibleButtonIndex = 0;
	}
	
	public void resizeButtons() {
		int i = 0;
		for (Button b : buttons) {
			Shape shape = null;
			if (b.isVisible() && !b.isKeyPressOnly()) {
				shape = getButtonShape(i);
				i++;
			}
			b.setShape(shape);
		}
	}
	
	public void paintButtons(Graphics2D g2) {
		for (Button button : buttons) {
			button.paint(g2);
		}
	}
	
	public boolean keyPressed(int keyCode) throws GameException {
		for (Button b : buttons) {
			if (b.keyPress(keyCode)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean click(MouseEvent e) throws GameException {
		for (Button b : buttons) {
			if (b.click(e)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Button> getButtons() {
		return buttons;
	}
	
	protected abstract Shape getButtonShape(int visibleButtonIndex);
	
}
