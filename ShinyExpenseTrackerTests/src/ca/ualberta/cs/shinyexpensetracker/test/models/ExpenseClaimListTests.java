package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.util.UUID;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ExpenseClaimListTests extends TestCase {

	private ExpenseClaimList list;

	protected void setUp() throws Exception {
		super.setUp();

		list = new ExpenseClaimList();
	}

	public void testThatGetClaimByIDFindsAClaimIfOneExistsWithThatID() throws ValidationException {
		ExpenseClaim claim = new ExpenseClaim(UUID.randomUUID(), "foo");

		ExpenseClaim retrievedClaim = list.getClaimByID(claim.getID());
		assertEquals(claim, retrievedClaim);
	}

	public void testThatGetClaimByIDFindsNothingIfTheListIsEmpty() {
		ExpenseClaim retrievedClaim = list.getClaimByID(UUID.randomUUID());
		assertNull(retrievedClaim);
	}

	public void testThatGetClaimByIDFindsNothingIfNoClaimExistsWithThatID() throws ValidationException {
		ExpenseClaim claim = new ExpenseClaim(UUID.randomUUID(), "foo");
		list.addClaim(claim);

		ExpenseClaim retrievedClaim = list.getClaimByID(UUID.randomUUID());
		assertNull(retrievedClaim);
	}
}
