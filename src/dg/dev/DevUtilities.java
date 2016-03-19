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
		Coordinates topLeft = new Coordinates(center, Direction.TOPLEFT);
		Coordinates topRight = new Coordinates(center, Direction.TOPRIGHT);
		Coordinates left = new Coordinates(center, Direction.LEFT);
		Coordinates right = new Coordinates(center, Direction.RIGHT);
		Coordinates bottomLeft = new Coordinates(center, Direction.BOTTOMLEFT);
		Coordinates bottomRight = new Coordinates(center, Direction.BOTTOMRIGHT);

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
