package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;

public class ESClaimManagerTest extends TestCase {
	
	ESClaimManager manager = new ESClaimManager();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testAddClaim() throws IllegalStateException, IOException {
		ExpenseClaim claim = new ExpenseClaim("Rajan sucks", null, null, Status.IN_PROGRESS, null, "1a");
		manager.insertClaim(claim);
		assertEquals("Expense claims are not equal", manager.getClaim("1a"), claim);
	}
	
	public void testDeleteClaim() throws IOException {
		manager.deleteClaim("1a");
		assertNull("Expense claim at index 1a is not null", manager.getClaim("1a"));
	}
}
