package dg.action;

import dg.GameException;
import dg.gui.input.Dialog;

public class TestDialogAction extends Action {

	public TestDialogAction() {
		super("Dialog testen");
	}
	
	@Override
	public void execute() throws GameException {
		Action closeAction = new Action("Schlieﬂen") {
			@Override
			public void execute() throws GameException {
				Dialog.close();
			}
		};
		
		Action okAction = new Action("OK") {
			@Override
			public void execute() throws GameException {
				Dialog.close();
				Dialog.open("Nochmal :D", new Action("Schlieﬂen") {
					@Override
					public void execute() throws GameException {
						Dialog.close();
					}
				});
			}
		};
		
		Dialog.open("Hallo, Welt", closeAction, okAction);
	}
	
}
