package ca.ualberta.cs.shinyexpensetracker.test.models;

import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import junit.framework.TestCase;

public class CoordinateTests extends TestCase {

	private Coordinate c1;
	private Coordinate c2;
	protected void setUp() throws Exception {
		super.setUp();
		c1 = new Coordinate(1.0, -1.0);
		c2 = new Coordinate(-1.0, 1.0);
	}

	public void testSetCoordinate() {
		c1.setLatitude(-1.0);
		c1.setLongitude(1.0);
		assertEquals("coordinates not equal, no overflow", c1, c2);
		c1.setLatitude(179.0);
		c1.setLongitude(361.0);
		assertEquals("coordinates not equal, positive overflow", c1, c2);
		c1.setLatitude(-181.0);
		c1.setLongitude(-359.0);
		assertEquals("coordinates not equal, negative overflow", c1, c2);
	}
	
	public void testDistance() {
		Coordinate c3 = new Coordinate(1.0, -1.0);

		assertEquals("wrong distance", 0.0, c1.distanceTo(c3));
		Coordinate c4 = new Coordinate(181, 359);
		assertEquals("wrong distance", 0.0, c1.distanceTo(c4));
	}
}