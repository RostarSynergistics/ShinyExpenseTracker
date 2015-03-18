package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.io.IOException;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
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
		ExpenseClaim claim = new ExpenseClaim("test");
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
