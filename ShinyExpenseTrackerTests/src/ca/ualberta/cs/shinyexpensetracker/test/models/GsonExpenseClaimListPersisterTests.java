package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.persistence.GsonExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IPersistenceStrategy;

/**
 * Tests GsonExpenseClaimListPersister's ability to save and load an
 * ExpenseClaimList.
 */
public class GsonExpenseClaimListPersisterTests extends TestCase {
	/**
	 * Tests GsonExpenseClaimListPersister's ability to save and load an
	 * ExpenseClaimList.
	 */
	public void testPersistenceOfExpenseClaims() {
		ExpenseClaimList list = new ExpenseClaimList();
		ExpenseClaim claim = getTestClaim();
		list.addClaim(claim);

		IExpenseClaimListPersister persister = new GsonExpenseClaimListPersister(
				new MockPersistenceStrategy());

		try {
			persister.saveExpenseClaims(list);
			ExpenseClaimList newList = persister.loadExpenseClaims();
			assertEquals(1, newList.getCount());
			ExpenseClaim loadedClaim = newList.getClaim(0);
			assertEquals(claim, loadedClaim);
			assertEquals(claim.getExpense(0), loadedClaim.getExpense(0));
		} catch (IOException e) {
			fail();
		}
	}

	private ExpenseClaim getTestClaim() {
		Date startDate = new Date(5000);
		Date endDate = new Date(6000);

		ExpenseClaim claim = new ExpenseClaim("test", startDate, endDate,
				ExpenseClaim.Status.IN_PROGRESS);
		
		int[] colors = new int[] { 1, 2, 3, 4 };
		claim.addExpense(new ExpenseItem("test", new Date(5000),
				ExpenseItem.Category.ACCOMODATION, new BigDecimal("20.00"),
				ExpenseItem.Currency.CAD, "Description", Bitmap.createBitmap(colors, 2, 2, Config.ALPHA_8)));
		
		return claim;
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
