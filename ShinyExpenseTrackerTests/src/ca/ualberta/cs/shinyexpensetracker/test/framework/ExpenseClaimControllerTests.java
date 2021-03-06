package ca.ualberta.cs.shinyexpensetracker.test.framework;

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

	public void testThatAddingANewExpenseClaimWorks() throws Exception {
		final String name = "FooBar";
		final Date startDate = new Date(6000);
		final Date endDate = new Date(7000);

		ExpenseClaim claim = controller.addExpenseClaim(name, startDate, endDate);

		assertEquals(claimList.getCount(), 1);
		assertEquals(claim, claimList.getClaimAtPosition(0));
		assertEquals(name, claim.getName());
		assertEquals(startDate, claim.getStartDate());
		assertEquals(endDate, claim.getEndDate());

		assertTrue("Persister's .save() wasn't called.", persister.wasSaveCalled());
	}

	public void testThatUpdatingAnExistingExpenseClaimWorks() throws Exception {
		ExpenseClaim oldClaim = new ExpenseClaim("FooBar", new Date(6000), new Date(7000));
		claimList.addClaim(oldClaim);

		final String name = "Baz";
		final Date startDate = new Date(8000);
		final Date endDate = new Date(9000);

		ExpenseClaim claim = controller.updateExpenseClaim(oldClaim.getID(), name, startDate, endDate);

		assertNotNull(claim);
		assertEquals(claimList.getCount(), 1);
		assertEquals(claim, claimList.getClaimAtPosition(0));
		assertEquals(name, claim.getName());
		assertEquals(startDate, claim.getStartDate());
		assertEquals(endDate, claim.getEndDate());

		assertTrue("Persister's .save() wasn't called.", persister.wasSaveCalled());
	}
}
