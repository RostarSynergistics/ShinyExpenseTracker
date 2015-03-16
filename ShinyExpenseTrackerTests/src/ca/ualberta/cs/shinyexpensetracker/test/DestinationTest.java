package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.DestinationList;
import android.test.InstrumentationTestCase;

public class DestinationTest extends InstrumentationTestCase {
	DestinationList destList;
	
	public void setUp() {
		destList = new DestinationList();
	}
	
	public void testAddDestination() {
		assertEquals("DestinationList not empty: ", 0, destList.getCount());
		
		String destName = "Miami";
		String destReason = "Vacation";
		
		Destination sampleDest = new Destination(destName, destReason);
		destList.addDestination(sampleDest);
		assertEquals("DestinationList is empty: ", 1, destList.getCount());
	}
	
	public void testRemoveDestination() {
		String n = "Miami";
		String r = "Vacation";
		destList.removeDestination(n, r);
		assertEquals("DestinationList not empty: ", 0, destList.getCount());
	}
	
	
}
