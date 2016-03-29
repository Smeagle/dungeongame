package dg.dev;

import java.util.LinkedList;

import dg.Coordinates;
import dg.Direction;
import dg.GameState;
import dg.Gameboard;
import dg.Random;
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
	
	public static void addDevGuards() {
		Coordinates guardCoordinates = new Coordinates(0, 0);
		LinkedList<Coordinates> route = new LinkedList<Coordinates>();
		route.add(new Coordinates(-1,  0));
		route.add(new Coordinates(0,  -1));
		route.add(new Coordinates(1,  -1));
		GameState.getBoard().addGuard(guardCoordinates, route);
		
		guardCoordinates = new Coordinates(0, 1);
		route = new LinkedList<Coordinates>();
		route.add(new Coordinates(0,  0));
		route.add(new Coordinates(3,  -1));
		GameState.getBoard().addGuard(guardCoordinates, route);
	}
	
	public static Gameboard getRandomGameboard(int size) {
		Gameboard board = new Gameboard();
		
		int l = size * 2;
		
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < l; j++) {
				int q = i - l / 2;
				int r = j - l / 2;
				Coordinates c = new Coordinates(q, r);
				if (Coordinates.calculateDistance(c, new Coordinates(0, 0)) < size) {
					if (Math.abs(q) < 2 && Math.abs(r) < 2) {
						board.addField(c, Terrain.FLOOR);
					}
					else {
						board.addField(c, Random.getInstance().nextBoolean() ? Terrain.FLOOR : Terrain.WALL);
					}
				}
			}
		}
		
		return board;
	}
	
}
