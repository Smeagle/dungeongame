package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dg.Coordinates;
import dg.Direction;
import dg.Gameboard;
import dg.Guard;
import dg.Terrain;

public class AgentTest {
	Gameboard board;
	Coordinates center;
	Coordinates topLeft;
	Coordinates topRight;
	Coordinates left;
	Coordinates right;
	Coordinates bottomLeft;
	Coordinates bottomRight;
	Coordinates viewPoint;
	Coordinates outOfBounds;

	Guard guard;

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
		viewPoint = bottomRight;
		outOfBounds = new Coordinates(0, 2); // NOT added to grid

		board.addField(topLeft, Terrain.FLOOR);
		board.addField(topRight, Terrain.FLOOR);
		board.addField(left, Terrain.FLOOR);
		board.addField(center, Terrain.WALL);
		board.addField(right, Terrain.WALL);
		board.addField(bottomLeft, Terrain.FLOOR);
		board.addField(viewPoint, Terrain.FLOOR);

		LinkedList<Coordinates> route = new LinkedList<>();
		route.add(viewPoint);
		route.add(topRight);
		board.addGuard(viewPoint, route);
		guard = (Guard) board.getAgents().getFirst();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void pathFindingTest() {
		LinkedList<Coordinates> target = new LinkedList<Coordinates>();

		LinkedList<Coordinates> calculatedPath = guard.calculatePath(viewPoint, viewPoint);
		assertEquals(0, calculatedPath.size());
		assertEquals("There should be no path to same field.", target, calculatedPath);

		calculatedPath = guard.calculatePath(viewPoint, center);
		assertEquals(0, calculatedPath.size());
		assertEquals("There should be no path to CENTER wall field.", target, calculatedPath);

		calculatedPath = guard.calculatePath(viewPoint, right);
		assertEquals(0, calculatedPath.size());
		assertEquals("There should be no path to RIGHT, because it is a wall field.", target, calculatedPath);

		target.add(bottomLeft);
		calculatedPath = guard.calculatePath(viewPoint, bottomLeft);
		assertEquals(1, calculatedPath.size());
		assertEquals("The path to BOTTOMLEFT should just be BOTTOMLEFT.", target, calculatedPath);

		target.add(left);
		calculatedPath = guard.calculatePath(viewPoint, left);
		assertEquals(2, calculatedPath.size());
		assertEquals("The path to LEFT should be BOTTOMLEFT + LEFT.", target, calculatedPath);

		target.add(topLeft);
		calculatedPath = guard.calculatePath(viewPoint, topLeft);
		assertEquals(3, calculatedPath.size());
		assertEquals("The path to TOPLEFT should be BOTTOMLEFT + LEFT + TOPLEFT.", target, calculatedPath);

		target.add(topRight);
		calculatedPath = guard.calculatePath(viewPoint, topRight);
		assertEquals(4, calculatedPath.size());
		assertEquals("The path to TOPRIGHT should be BOTTOMLEFT + LEFT + TOPLEFT + TOPRIGHT.", target, calculatedPath);

		exception.expect(IllegalArgumentException.class);
		guard.calculatePath(viewPoint, outOfBounds);
	}


	@Test
	public void walkAroundOtherGuardTest() {
		fail("Not yet implemented");
	}

	@Test
	public void killPlayerTest() {
		fail("Not yet implemented");
	}


	@Test
	public void noticePlayerTest() {
		fail("Not yet implemented");
	}

	@Test
	public void noticeAdditionalPlayerTest() {
		fail("Not yet implemented");
	}
	
}
