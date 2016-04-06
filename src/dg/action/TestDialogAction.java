package dg.action;

import dg.GameException;
import dg.gui.input.Dialog;

public class TestDialogAction extends Action {

	public TestDialogAction() {
		super("Dialog testen");
	}
	
	@Override
	public void execute() throws GameException {
		Dialog.open("Hallo, Welt\n\nbeliebige Taste zum fortfahren");
	}
	
}
