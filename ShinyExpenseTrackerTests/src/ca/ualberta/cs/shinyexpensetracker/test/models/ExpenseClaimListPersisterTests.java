package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.persistance.ExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistance.IExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistance.IPersistenceStrategy;
import junit.framework.TestCase;

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
			assertEquals(claim.getName(), newList.getClaim(0).getName());
		} catch (IOException e) {
			fail();
		}
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
