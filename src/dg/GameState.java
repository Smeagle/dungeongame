package dg;

import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dg.action.Action;
import dg.action.DebugAgentAction;
import dg.action.DebugPathfindingAction;
import dg.dev.DevUtilities;
import dg.dev.Dummy;
import dg.gui.BoardPanel;
import dg.gui.Frame;
import dg.gui.GUIUtils;
import dg.gui.ImageCache;
import dg.gui.LoadingPanel;
import dg.gui.TitlePanel;
import dg.gui.input.Button;
import dg.gui.input.Dialog;
import dg.gui.input.Menu;

/**
 * Repräsentiert den aktuellen Spielzustand, enthält also das Brett, Figuren etc.
 */
public class GameState {

	private static Gameboard board;
	
	private static Agent activeAgent;
	
	private static Coordinates mouseoverCoordinates;
	
	/**
	 * Erstellt ein neues Spiel und startet dieses.
	 */
	public static void newGame() {
		// Ladebildschirm zeigen
		Frame.getInstance().showPanel(LoadingPanel.getInstance());
		
		// Status und Caches zurücksetzen
		clearStatesAndCaches();
		
		// Spielbrett laden
		setBoard(DevUtilities.getRandomGameboard(10));
		
		// Figuren drauf stellen
		board.addAgent(new Dummy(new Coordinates(1, 1), GameState.getBoard()));
		DevUtilities.addDevGuards();
		
		// Brett zeigen
		Frame.getInstance().showPanel(BoardPanel.getInstance());
		
		// Spiel starten
		startGame();
	}
	
	/**
	 * Setzt alle spielbrett/-status-relevanten Caches zurueck.
	 */
	private static void clearStatesAndCaches() {
		GUIUtils.clearCaches();
		ImageCache.clearCaches();
		
		DebugPathfindingAction.clear();
		DebugAgentAction.clear();
		
		Dialog.closeAll(); // sicher ist sicher
	}
	
	/**
	 * Lädt den Speicherstand und setzt das Spiel fort.
	 */
	public static void resumeGame() {
		//TODO
	}
	
	/**
	 * Startet das Spiel. Das Brett und Figuren etc. müssen schon aufgestellt sein.
	 */
	public static void startGame() {
		if (board != null && board.getAgents().size() > 0) {
			// es fängt immer der Spieler an
			for (Agent a : board.getAgents()) {
				if (!a.isComputerControlled()) {
					activeAgent = a;
					activeAgent.takeTurn();
					return;
				}
			}
			
			// falls es keinen Spieler-Agenten gibt...
			nextAgentsTurn();
		}
	}
	
	/**
	 * Geht nach Sicherheitsabfrage zum Titelbildschirm, eventuell wird hier später gespeichert
	 */
	public static void stopGame() {
		Dialog.open("Möchtest du das Spiel wirklich beenden?",
				new Button("Abbrechen", new Action() {
					@Override
					public void execute() throws GameException {
						Dialog.close();
					}
				}),
				new Button("Ja", new Action() {
					@Override
					public void execute() throws GameException {
						Dialog.close();
						Frame.getInstance().showPanel(TitlePanel.getInstance());
					}
				})
		);
	}
	
	/**
	 * Der nächste Agent kommt an die Reihe.
	 * Von Spieler-Agenten aufzurufen, die "fertig" sind.
	 * Nach einem Zug eines KI-Agenten kommt automatisch der nächste Agent an die Reihe.
	 */
	public static void nextAgentsTurn() {
		Menu.getInstance().clearButtons();
		
		int i = 0;
		if (activeAgent != null) {
			board.getAgents().indexOf(activeAgent);
		}
		
		while (true) {
			if (i >= board.getAgents().size() - 1) {
				i = 0;
			}
			else {
				i++;
			}
			activeAgent = board.getAgents().get(i);
			activeAgent.takeTurn();
			if (!activeAgent.isComputerControlled()) {
				break;
			}
		}
	}
	
	public static void setBoard(Gameboard board) {
		GameState.board = board;
	}
	
	public static Gameboard getBoard() {
		return GameState.board;
	}

	public static Agent getActiveAgent() {
		return activeAgent;
	}

	public static void setMouseoverCoordinates(Coordinates c) {
		mouseoverCoordinates = c;
	}

	public static Coordinates getMouseoverCoordinates() {
		return mouseoverCoordinates;
	}

	public static void playerKilled() {
		// TODO Auto-generated method stub
	
		throw new NotImplementedException();
	}

	public static void levelComplete() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	
	}

	public static Action getPlayerAction(List<Button> playerButtons) {
		// TODO Auto-generated method stub
	
		throw new NotImplementedException();
	}

}
