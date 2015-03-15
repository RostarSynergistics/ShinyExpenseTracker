package ca.ualberta.cs.shinyexpensetracker.test.models;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.persistance.ExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistance.IPersistanceStrategy;
import junit.framework.TestCase;

public class ExpenseClaimListPersisterTests extends TestCase {
	public void testPersistanceOfExpenseClaims() {
		ExpenseClaimList list = new ExpenseClaimList();
		ExpenseClaim claim = new ExpenseClaim("test");
		list.addClaim(claim);
		
		ExpenseClaimListPersister persister = 
				new ExpenseClaimListPersister(new MockPersistanceStrategy());
		
		persister.saveExpenseClaims(list);
		ExpenseClaimList newList = persister.loadExpenseClaims();

		assertEquals(1, newList.getCount());
		assertEquals(claim.getName(), newList.getClaim(0).getName());
	}

	private class MockPersistanceStrategy implements IPersistanceStrategy {
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
