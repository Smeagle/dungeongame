package dg.dev;

import java.util.LinkedList;

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

		board.addField(center, Terrain.FLOOR);
		board.addField(topLeft, Terrain.WALL);
		board.addField(topRight, Terrain.WALL);
		board.addField(left, Terrain.WALL);
		board.addField(right, Terrain.FLOOR);
		board.addField(bottomLeft, Terrain.FLOOR);
		board.addField(bottomRight, Terrain.WALL);
		
		// test: adding guard
		Coordinates guardCoordinates = new Coordinates(0, 0);
		LinkedList<Coordinates> route = new LinkedList<Coordinates>();
		route.add(new Coordinates(-1,  0));
		route.add(new Coordinates(0,  -1));
		route.add(new Coordinates(1,  -1));
		board.addGuard(guardCoordinates, route);
		
		return board;
	}
	
}
