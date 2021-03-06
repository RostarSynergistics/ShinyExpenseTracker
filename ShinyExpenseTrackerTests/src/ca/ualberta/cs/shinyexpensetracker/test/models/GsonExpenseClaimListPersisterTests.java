package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.Status;
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
	 * 
	 * @throws ValidationException
	 */
	public void testPersistenceOfExpenseClaims() throws ValidationException {
		ExpenseClaimList list = new ExpenseClaimList();
		ExpenseClaim claim = getTestClaim();
		list.addClaim(claim);

		IExpenseClaimListPersister persister = new GsonExpenseClaimListPersister(new MockPersistenceStrategy());

		try {
			persister.saveExpenseClaims(list);
			ExpenseClaimList newList = persister.loadExpenseClaims();
			assertEquals(1, newList.getCount());
			ExpenseClaim loadedClaim = newList.getClaimAtPosition(0);
			assertEquals(claim, loadedClaim);
			assertEquals(claim.getExpenseItemAtPosition(0), loadedClaim.getExpenseItemAtPosition(0));
		} catch (IOException e) {
			fail();
		}
	}

	private ExpenseClaim getTestClaim() throws ValidationException {
		Date startDate = new Date(5000);
		Date endDate = new Date(6000);

		ExpenseClaim claim = new ExpenseClaim("test", startDate, endDate, Status.IN_PROGRESS);

		int[] colors = new int[] { 1, 2, 3, 4 };

		claim.addExpenseItem(new ExpenseItem("test",
				new Date(5000),
				ExpenseItem.Category.ACCOMODATION,
				new BigDecimal("20.00"),
				ExpenseItem.Currency.CAD,
				"Description",
				Bitmap.createBitmap(colors, 2, 2, Config.ALPHA_8),
				new Coordinate(1.0, -1.0)));

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
