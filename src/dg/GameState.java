package dg;

/**
 * Repräsentiert den aktuellen Spielzustand, enthält also das Brett, Figuren etc.
 */
public class GameState {

	private static Gameboard board;
	
	private static Agent activeAgent;
	
	private static Coordinates mouseoverCoordinates;
	
	private static Coordinates selectionCoordinates;
	
	/**
	 * Startet das Spiel. Das Brett und Figuren etc. müssen schon aufgestellt sein.
	 */
	public static void startGame() {
		if (board != null && board.getAgents().size() > 0) { 
			activeAgent = board.getAgents().get(0);
			activeAgent.takeTurn();
		}
	}
	
	/**
	 * Der nächste Agent kommt an die Reihe.
	 * Von Agenten aufzurufen, die "fertig" sind. KI-Agenten rufen selbstständig auf,
	 * Spieler-Agenten per Knopfdruck oder automatisch wenn alle Aktionen aufgebraucht sind,
	 * z.B. die maximale Anzahl Schritte gegangen wurden.
	 */
	public static void finishTurn() {
		int i = board.getAgents().indexOf(activeAgent);
		if (i >= board.getAgents().size() - 1) {
			i = 0;
		}
		else {
			i++;
		}
		activeAgent = board.getAgents().get(i);
		activeAgent.takeTurn();
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

	public static Coordinates getSelectionCoordinates() {
		return selectionCoordinates;
	}

	public static void setSelectionCoordinates(Coordinates selectionCoordinates) {
		GameState.selectionCoordinates = selectionCoordinates;
	}
	
}
