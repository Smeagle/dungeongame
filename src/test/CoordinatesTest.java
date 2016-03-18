package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dg.Coordinates;
import dg.Direction;

public class CoordinatesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void equalityTest() {
		Coordinates origin = new Coordinates(0, 0);
		Coordinates moreOrigin = new Coordinates(0, 0);
		assertEquals(origin, moreOrigin);

		Coordinates topLeft = new Coordinates(-1, 0);
		assertNotEquals(topLeft, origin);
	}
	
	@Test
	public void directionConstructorTest() {
		Coordinates origin = new Coordinates(0,0);
		
		Coordinates topLeft = new Coordinates(-1, 0);
		assertEquals(topLeft, new Coordinates(origin, Direction.TOPLEFT));
		
		assertNotEquals(topLeft, origin);
		
		Coordinates topRight = new Coordinates(-1, 1);
		assertEquals(topRight, new Coordinates(origin, Direction.TOPRIGHT));
	}
}
