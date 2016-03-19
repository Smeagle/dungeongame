package dg;

/**
 * Repräsentiert den aktuellen Spielzustand, enthält also das Brett, Figuren etc.
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
