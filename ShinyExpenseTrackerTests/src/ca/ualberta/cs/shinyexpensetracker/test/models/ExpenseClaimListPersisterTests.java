package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.persistence.ExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IPersistenceStrategy;

/**
 * Tests ExpenseClaimListPersister's ability to save and load an
 * ExpenseClaimList.
 */
public class ExpenseClaimListPersisterTests extends TestCase {
	/**
	 * Tests ExpenseClaimListPersister's ability to save and load an
	 * ExpenseClaimList.
	 */
	public void testPersistanceOfExpenseClaims() {
		ExpenseClaimList list = new ExpenseClaimList();
		ExpenseClaim claim = getTestClaim();
		list.addClaim(claim);

		IExpenseClaimListPersister persister = new ExpenseClaimListPersister(
				new MockPersistenceStrategy());

		try {
			persister.saveExpenseClaims(list);
			ExpenseClaimList newList = persister.loadExpenseClaims();
			assertEquals(1, newList.getCount());
			ExpenseClaim loadedClaim = newList.getClaim(0);
			compareExpenseClaims(claim, loadedClaim);
		} catch (IOException e) {
			fail();
		}
	}

	private ExpenseClaim getTestClaim() {
		Date startDate = new Date(5000);
		Date endDate = new Date(6000);

		ExpenseClaim claim = new ExpenseClaim("test", startDate, endDate,
				ExpenseClaim.Status.IN_PROGRESS);
		
		
		return claim;
	}
	
	private void compareExpenseClaims(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		assertEquals(oldClaim.getName(), newClaim.getName());
	}

	private class MockPersistenceStrategy implements IPersistenceStrategy {
		private String value = "";

		@Override
		public void save(String value) {
			this.value = value;
		}

		@Override
		public String load() {
			return value;
		}
	}
}
