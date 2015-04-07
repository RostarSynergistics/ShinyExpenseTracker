package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.math.BigDecimal;
import java.util.Date;

import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.test.R;

/**
 * Tests that verify that ExpenseItem.equals(...) works. ExpenseItems are equal
 * if and only if all of its fields are equal
 */
public class ExpenseItemEqualsTests extends AndroidTestCase {
	ExpenseItem item1;
	ExpenseItem item2;

	protected void setUp() throws Exception {
		super.setUp();

		item1 = getStartingItem();
		item2 = getStartingItem();
	}

	private ExpenseItem getStartingItem() throws ValidationException {
		return new ExpenseItem("test",
				new Date(5000),
				ExpenseItem.Category.ACCOMODATION,
				new BigDecimal("20.00"),
				ExpenseItem.Currency.CAD,
				"Description",
				BitmapFactory.decodeResource(getContext().getResources(), R.drawable.keyhole_nebula_hubble_1999),
				new Coordinate(1.0, -1.0));

	}

	public void testThatTwoIdenticalItemsAreEqual() {
		assertEquals();
	}

	public void testThatTwoItemsWithDifferentNamesAreNotEqual() throws ValidationException {
		item2.setName("test2");

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentDatesAreNotEqual() throws ValidationException {
		item2.setDate(new Date(100));

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentCategoriesAreNotEqual() {
		item2.setCategory(ExpenseItem.Category.GROUND_TRANSPORT);

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentAmountsSpentAreNotEqual() throws ValidationException {
		item2.setAmountSpent(new BigDecimal("30.00"));

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentCurrenciesAreNotEqual() {
		item2.setCurrency(ExpenseItem.Currency.JPY);

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentDescriptionsAreNotEqual() {
		item2.setDescription("FizzBuzzFizzBuzzFizzBuzz");

		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentReceiptPhotosAreNotEqual() {
		item2.setReceiptPhoto(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.corgi_running));

		assertNotEqual();
	}

	private void assertEquals() {
		assertEquals(item1, item2);
	}

	private void assertNotEqual() {
		assertNotEqual(item1, item2);
	}

	private void assertNotEqual(Object expected, Object actual) {
		assertFalse(expected.equals(actual));
	}
}
