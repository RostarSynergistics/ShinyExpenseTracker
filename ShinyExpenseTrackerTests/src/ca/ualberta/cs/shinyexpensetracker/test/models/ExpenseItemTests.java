package ca.ualberta.cs.shinyexpensetracker.test.models;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;

import android.graphics.Bitmap;

import junit.framework.TestCase;

public class ExpenseItemTests extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests whether an ExpenseItem's fields are properly set when its
	 * constructor is called.
	 */
	public void testCreateExpenseItem() {
		Date date = new Date();

		CurrencyUnit currencyUnit = CurrencyUnit.of("CAD");
		BigDecimal amount = new BigDecimal("10.0");

		Money amountSpent = Money.of(currencyUnit, amount);
		Bitmap bitmap = null;

		ExpenseItem expense = new ExpenseItem("name", date,
				Category.ACCOMODATION, amountSpent, "description", bitmap);

		assertEquals("name", expense.getName());
		assertEquals(date, expense.getDate());
		assertEquals(Category.ACCOMODATION, expense.getCategory());

		assertNotNull("amountSpent not initalized.", expense.getAmountSpent());
		assertEquals("CurrencyUnit part of amountSpent incorrect",
				currencyUnit, expense.getAmountSpent().getCurrencyUnit());
		
		/*
		 * When comparing BigDecimals, .equals() compares scale in addition to value.
		 * .compareTo() only checks their numeric values, which is desired here.
		 * More info: http://stackoverflow.com/a/6787166/14064
		 */
		assertTrue("Number part of amountSpent incorrect.",
				amount.compareTo(expense.getAmountSpent().getAmount()) == 0);

		assertEquals("description", expense.getDescription());
		assertEquals(bitmap, expense.getReceiptPhoto());
	}
}
