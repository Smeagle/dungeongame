package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dg.Coordinates;
import dg.Direction;
import dg.GameBoardUtils;
import dg.Gameboard;
import dg.Terrain;

public class GameBoardUtilsTest {
	Gameboard board;
	Coordinates center;
	Coordinates topLeft;
	Coordinates topRight;
	Coordinates left;
	Coordinates right;
	Coordinates bottomLeft;
	Coordinates bottomRight;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		board = new Gameboard();
		center = new Coordinates(0, 0);
		topLeft = new Coordinates(center, Direction.TOPLEFT);
		topRight = new Coordinates(center, Direction.TOPRIGHT);
		left = new Coordinates(center, Direction.LEFT);
		right = new Coordinates(center, Direction.RIGHT);
		bottomLeft = new Coordinates(center, Direction.BOTTOMLEFT);
		bottomRight = new Coordinates(center, Direction.BOTTOMRIGHT);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void generateSimpleBoardTest() {
		assertEquals("Center was not added, should be out of bounds.", false, board.isInBounds(center));
		String boardString = "F F $ F W W $ F F $";
		board = GameBoardUtils.boardGenerator(boardString);
		assertEquals("center was added, should be in bounds.", true, board.isInBounds(center));
		assertEquals("topLeft was added, should be in bounds.", true, board.isInBounds(topLeft));
		assertEquals("topRight was added, should be in bounds.", true, board.isInBounds(topRight));
		assertEquals("left was added, should be in bounds.", true, board.isInBounds(left));
		assertEquals("right was added, should be in bounds.", true, board.isInBounds(right));
		assertEquals("bottomLeft was added, should be in bounds.", true, board.isInBounds(bottomLeft));
		assertEquals("bottomRight was added, should be in bounds.", true, board.isInBounds(bottomRight));
		
		assertEquals(Terrain.WALL, board.getTerrain(center));
		assertEquals(Terrain.FLOOR, board.getTerrain(topLeft));
		assertEquals(Terrain.FLOOR, board.getTerrain(topRight));
		assertEquals(Terrain.FLOOR, board.getTerrain(left));
		assertEquals(Terrain.WALL, board.getTerrain(right));
		assertEquals(Terrain.FLOOR, board.getTerrain(bottomLeft));
		assertEquals(Terrain.FLOOR, board.getTerrain(bottomRight));
	}
	
	public void emptyBoardTest() {
		String boardString = "$";
		exception.expect(IllegalArgumentException.class);
		board = GameBoardUtils.boardGenerator(boardString);

		boardString = "";
		exception.expect(IllegalArgumentException.class);
		board = GameBoardUtils.boardGenerator(boardString);
	}

}
