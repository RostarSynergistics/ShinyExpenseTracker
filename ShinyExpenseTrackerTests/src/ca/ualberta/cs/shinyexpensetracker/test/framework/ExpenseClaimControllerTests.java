package ca.ualberta.cs.shinyexpensetracker.test.framework;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests for ExpenseClaimController.
 */
public class ExpenseClaimControllerTests extends TestCase {
	private ExpenseClaimController controller;
	private ExpenseClaimList claimList;
	private MockExpenseClaimListPersister persister;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		claimList = new ExpenseClaimList();
		persister = new MockExpenseClaimListPersister(claimList);
		controller = new ExpenseClaimController(persister);
	}

	public void testThatAddingANewExpenseClaimWorks() throws IOException {
		final String name = "FooBar";
		final Date startDate = new Date(6000);
		final Date endDate = new Date(7000);

		ExpenseClaim claim = controller.addExpenseClaim(name, startDate, endDate);

		assertEquals(claimList.getCount(), 1);
		assertEquals(claim, claimList.getClaim(0));
		assertEquals(name, claim.getName());
		assertEquals(startDate, claim.getStartDate());
		assertEquals(endDate, claim.getEndDate());

		assertTrue("Persister's .save() wasn't called.", persister.wasSaveCalled());
	}
}
