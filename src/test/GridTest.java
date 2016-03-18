package test;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dg.*;

public class GridTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	/*-
	 * Grid is:
	 *   F F
	 *  F W W
	 *   F V
	 *   
	 * V is viewPoint.
	 */
	@Test
	public void isVisibleTest() throws IndexOutOfBoundsException {
		Gameboard grid = new Gameboard();

		Coordinates topLeft = new Coordinates(0, -1);
		Coordinates topRight = new Coordinates(1, -1);
		Coordinates left = new Coordinates(-1, 0);
		Coordinates center = new Coordinates(0, 0);
		Coordinates right = new Coordinates(1, 0);
		Coordinates bottomLeft = new Coordinates(-1, 1);
		Coordinates viewPoint = new Coordinates(0, 1);
		Coordinates outOfBounds = new Coordinates(0, 2);

		grid.addField(topLeft, Terrain.FLOOR);
		grid.addField(topRight, Terrain.FLOOR);
		grid.addField(left, Terrain.FLOOR);
		grid.addField(center, Terrain.WALL);
		grid.addField(right, Terrain.WALL);
		grid.addField(bottomLeft, Terrain.FLOOR);
		grid.addField(viewPoint, Terrain.FLOOR);

		assertEquals("topLeft is out of sight", false,
				grid.isVisible(viewPoint, topLeft));
		assertEquals("topRight is out of sight", false,
				grid.isVisible(viewPoint, topRight));
		assertEquals("left is visible", true, grid.isVisible(viewPoint, left));
		assertEquals("center is visible", true,
				grid.isVisible(viewPoint, center));
		assertEquals("right is visible", true, grid.isVisible(viewPoint, right));
		assertEquals("bottomLeft is visible", true,
				grid.isVisible(viewPoint, bottomLeft));

		exception.expect(IndexOutOfBoundsException.class);
		grid.isVisible(viewPoint, outOfBounds);

		fail("Not yet implemented");
	}
}
