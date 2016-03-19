package dg;

/**
 * Repr�sentiert den aktuellen Spielzustand, enth�lt also das Brett, Figuren etc.
 */
public class GameState {

	private static Gameboard board;
	
	public static void setBoard(Gameboard board) {
		GameState.board = board;
	}
	
	public static Gameboard getBoard() {
		return GameState.board;
	}
	
}
