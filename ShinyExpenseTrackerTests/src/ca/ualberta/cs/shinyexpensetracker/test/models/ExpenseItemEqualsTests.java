package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.math.BigDecimal;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import junit.framework.TestCase;

public class ExpenseItemEqualsTests extends TestCase {
	ExpenseItem item1;
	ExpenseItem item2;

	protected void setUp() throws Exception {
		super.setUp();
		
		item1 = getStartingItem();
		item2 = getStartingItem();
	}
	
	private ExpenseItem getStartingItem() {
		int[] colors = new int[] { 1, 2, 3, 4 };
		
		return new ExpenseItem("test", new Date(5000),
				ExpenseItem.Category.ACCOMODATION, new BigDecimal("20.00"),
				ExpenseItem.Currency.CAD, "Description", Bitmap.createBitmap(colors, 2, 2, Config.ALPHA_8));
	}
	
	public void testThatTwoIdenticalItemsAreEqual() {
		assertEquals();
	}
	
	public void testThatTwoItemsWithDifferentNamesAreNotEqual() {
		item2.setName("test2");
		
		assertNotEqual();
	}
	
	public void testThatTwoItemsWithDifferentDatesAreNotEqual() {
		item2.setDate(new Date(100));
		
		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentCategoriesAreNotEqual() {
		item2.setCategory(ExpenseItem.Category.GROUND_TRANSPORT);
		
		assertNotEqual();
	}

	public void testThatTwoItemsWithDifferentAmountsSpentAreNotEqual() {
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
		int[] colors = new int[] { 1 };
		
		item2.setReceiptPhoto(Bitmap.createBitmap(colors, 1, 1, Config.ALPHA_8));
		
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
