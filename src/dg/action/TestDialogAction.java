package dg.action;

import dg.GameException;
import dg.gui.input.Button;
import dg.gui.input.Dialog;

public class TestDialogAction extends Action {

	@Override
	public void execute() throws GameException {
		Button closeButton = new Button("Schlieﬂen", new Action() {
			@Override
			public void execute() throws GameException {
				Dialog.close();
			}
		}, null);
		
		Button okButton = new Button("OK", new Action() {
			@Override
			public void execute() throws GameException {
				Dialog.close();
				Dialog.open("Nochmal :D", new Button("Schlieﬂen", new Action() {
					@Override
					public void execute() throws GameException {
						Dialog.close();
					}
				}, null));
			}
		}, null);
		
		Dialog.open("Hallo, Welt", closeButton, okButton);
	}
	
}
