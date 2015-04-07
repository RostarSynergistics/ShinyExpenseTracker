package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Tests that verify that ExpenseClaim.equals(...) works. ExpenseClaims are
 * equal if and only if all of its fields are equal
 */
public class ExpenseClaimEqualsTests extends TestCase {
	ExpenseClaim claim1;
	ExpenseClaim claim2;
	private UUID userID;

	private ExpenseClaim getStartingClaim(UUID claimID, UUID userID) {
		Date startDate = new Date(5000);
		Date endDate = new Date(6000);

		return new ExpenseClaim(userID, claimID, "test", startDate, endDate, ExpenseClaim.Status.IN_PROGRESS);
	}

	private ExpenseItem getTestExpenseItem() {
		return new ExpenseItem("test", new Date(500),
				ExpenseItem.Category.ACCOMODATION, new BigDecimal("20.00"),
				ExpenseItem.Currency.CAD);
	}

	protected void setUp() throws Exception {
		super.setUp();

		UUID claimID = UUID.randomUUID();
		userID = UUID.randomUUID();

		claim1 = getStartingClaim(claimID, userID);
		claim2 = getStartingClaim(claimID, userID);
	}

	public void testThatTwoIdenticalClaimsAreEqual() {
		assertEquals();
	}

	public void testThatTwoClaimsWithDifferentIDsAreNotEqual() {
		claim2 = getStartingClaim(UUID.randomUUID(), userID);

		assertNotEqual();
	}

	public void testThatTwoClaimsWithDifferentNamesAreNotEqual() {
		claim2.setName("a different name");

		assertNotEqual();
	}

	public void testThatTwoClaimsWithDifferentStartDatesAreNotEqual() {
		claim2.setStartDate(new Date(5500));

		assertNotEqual();
	}

	public void testThatTwoClaimsWithDifferentEndDatesAreNotEqual() {
		claim2.setStartDate(new Date(6500));

		assertNotEqual();
	}

	public void testThatTwoClaimsWithDifferentStatusesAreNotEqual() {
		claim2.setStatus(ExpenseClaim.Status.SUBMITTED);

		assertNotEqual();
	}

	public void testThatTwoClaimsWithIdenticalNonEmptyExpenseItemsListsAreEqual() {
		claim1.addExpenseItem(getTestExpenseItem());
		claim2.addExpenseItem(getTestExpenseItem());

		assertEquals();
	}

	public void testThatOneClaimWithOneWithAnEmptyExpenseItemsListAndTheOtherNotEmptyAreNotEqual() {
		claim2.addExpenseItem(getTestExpenseItem());

		assertNotEqual();
	}

	public void testThatTwoClaimsWithNonIdenticalNonEmptyExpenseItemsListsAreNotEqual() {
		claim1.addExpenseItem(getTestExpenseItem());
		ExpenseItem item = getTestExpenseItem();
		item.setName("foobarbaz");
		claim2.addExpenseItem(item);

		assertNotEqual();
	}

	public void testThatTwoClaimsWithIdenticalNonEmptyTagListsAreEqual() {
		// Tag association is not fully implemented yet.
		fail();
	}

	public void testThatOneClaimWithOneWithAnEmptyTagListAndTheOtherNotEmptyAreNotEqual() {
		// Tag association is not fully implemented yet.
		fail();
	}

	public void testThatTwoClaimsWithNonIdenticalNonEmptyTagListsAreNotEqual() {
		// Tag association is not fully implemented yet.
		fail();
	}

	private void assertEquals() {
		assertEquals(claim1, claim2);
	}

	private void assertNotEqual() {
		assertNotEqual(claim1, claim2);
	}

	private void assertNotEqual(Object expected, Object actual) {
		assertFalse(expected.equals(actual));
	}
}
