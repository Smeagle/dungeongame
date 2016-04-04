package dg;

import java.util.LinkedList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dg.action.Action;
import dg.gui.input.Menu;

/**
 * Repräsentiert den aktuellen Spielzustand, enthält also das Brett, Figuren etc.
 */
public class GameState {

	private static Gameboard board;
	
	private static Agent activeAgent;
	
	private static Coordinates mouseoverCoordinates;
	
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
	 * Der nächste Agent kommt an die Reihe.
	 * Von Spieler-Agenten aufzurufen, die "fertig" sind.
	 * Nach einem Zug eines KI-Agenten kommt automatisch der nächste Agent an die Reihe.
	 */
	public static void nextAgentsTurn() {
		Menu.clear();
		
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

	public static Action getPlayerAction(LinkedList<Action> actionOptions) {
		// TODO Auto-generated method stub
	
		throw new NotImplementedException();
	}

}
