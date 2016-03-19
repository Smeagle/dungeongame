package dg.dev;

import dg.Coordinates;
import dg.Direction;
import dg.Gameboard;
import dg.Terrain;

/**
 * Logik, die im finalen Produkt nicht enthalten ist, nur zu Entwicklungszwecken
 */
public class DevUtilities {

	/*-
	 * Grid is:
	 *   F F
	 *  F W W
	 *   F F
	 */
	public static Gameboard getDevGameboard() {
		Gameboard board = new Gameboard();

		Coordinates center = new Coordinates(0, 0);
		Coordinates topLeft = new Coordinates(0, -1);
		Coordinates topRight = new Coordinates(1, -1);
		Coordinates left = new Coordinates(-1, 0);
		Coordinates right = new Coordinates(1, 0);
		Coordinates bottomLeft = new Coordinates(-1, 1);
		Coordinates bottomRight = new Coordinates(0, 1);

		board.addField(topLeft, Terrain.FLOOR);
		board.addField(topRight, Terrain.FLOOR);
		board.addField(left, Terrain.FLOOR);
		board.addField(center, Terrain.WALL);
		board.addField(right, Terrain.WALL);
		board.addField(bottomLeft, Terrain.FLOOR);
		board.addField(bottomRight, Terrain.FLOOR);
		
		return board;
	}
	
}
