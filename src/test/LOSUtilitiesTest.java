package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dg.Coordinates;
import dg.Gameboard;
import dg.Intersection;
import dg.LOSUtilities;

public class LOSUtilitiesTest {
	Gameboard board;
	Coordinates c1_1 = new Coordinates(1, 1);
	Coordinates c0_0 = new Coordinates(0, 0);
	Coordinates cm1_1 = new Coordinates(-1, 1);
	Coordinates c0_1 = new Coordinates(0, 1);
	Coordinates c1_0 = new Coordinates(1, 0);
	Coordinates c1_m1 = new Coordinates(1, -1);
	Coordinates c0_m1 = new Coordinates(0, -1);
	Coordinates cm1_0 = new Coordinates(-1, 0);
	Coordinates c1_m2 = new Coordinates(1, -2);
	Coordinates cm1_m2 = new Coordinates(-1, -2);
	Coordinates c0_m2 = new Coordinates(0, -2);
	Coordinates cm1_m1 = new Coordinates(-1, -1);
	Coordinates cm2_0 = new Coordinates(-2, 0);
	Coordinates cm2_1 = new Coordinates(-2, 1);
	Coordinates c2_0 = new Coordinates(2, 0);
	Coordinates cm1_2 = new Coordinates(-1, 2);
	Coordinates c2_m1 = new Coordinates(2, -1);
	Coordinates cm3_1 = new Coordinates(-3, 1);

	Coordinates rayOrigin = c1_0;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void straightLineTest() {
		// Ray in Row from (1,0) to (1,-2) intersects (1,0) (1,-1) (1,-2)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c1_m2, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c1_m2, c1_m1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c1_m2, c1_m2));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, c1_m2, c0_0));

		// Ray in Row from (1,0) to (-2,0) intersects (1,0) (0,0) (-1,0) (-2,0)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm2_0, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm2_0, c0_0));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm2_0, cm1_0));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm2_0, cm2_0));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, cm2_0, c0_m1));

		// Ray in Row from (1,0) to (-1,2) intersects (1,0) (0,1) (-1,2)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm1_2, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm1_2, c0_1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm1_2, cm1_2));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, cm1_2, c0_0));
	}

	@Test
	public void touchingLineTest() {
		// Ray in Row from (1,0) to (0,-1) intersects (1,0) and (0,-1), touches (1,-1) and (0,0)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m1, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m1, c0_m1));
		assertEquals(Intersection.TOUCHES, LOSUtilities.intersects(rayOrigin, c0_m1, c0_0));
		assertEquals(Intersection.TOUCHES, LOSUtilities.intersects(rayOrigin, c0_m1, c1_m1));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, c0_m1, cm1_0));
	}

	@Test
	public void crookedLineTest() {
		// Ray in Row from (1,0) to (0,-2) intersects (1,0) (1,-1) (0,-1) (0,-2), clears (1,-2) and (0,0)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m2, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m2, c1_m1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m2, c0_m1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, c0_m2, c0_m2));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, c0_m2, c1_m2));
		assertEquals(Intersection.CLEAR, LOSUtilities.intersects(rayOrigin, c0_m2, c0_0));
	}

	@Test
	public void longerCrookedLineTest() {
		// Ray from (1,0) to (-3,1) intersects (1,0) (0,0) (-1,1) (-1,0) (-2,1) (-3,1)
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, rayOrigin));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, c0_0));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, cm1_1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, cm1_0));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, cm2_1));
		assertEquals(Intersection.INTERSECTS, LOSUtilities.intersects(rayOrigin, cm3_1, cm3_1));

	}

}
