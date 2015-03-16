package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.cs.shinyexpensetracker.Application;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.ExpenseItemDetailView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import junit.framework.TestCase;

/**
 * Test suite to test activity that lets the user view information of the
 * selected ExpenseItem
 * 
 * Covers Issue 16
 * 
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-15
 */
public class TestExpenseItemDetailView extends
		ActivityInstrumentationTestCase2<ExpenseItemDetailView> {

	public TestExpenseItemDetailView() {
		super(ExpenseItemDetailView.class);
	}

	ExpenseItemDetailView activity;
	ExpenseClaimController controller;

	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		controller = new ExpenseClaimController(
				new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim("test claim");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);

		ExpenseItem item = new ExpenseItem("test item", newDate.getTime(),
				Category.fromString("air fare"), new BigDecimal("0.125"),
				Currency.CAD, "Test Item", null);

		claim.addExpense(item);
		claimList.addClaim(claim);

		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		intent.putExtra("itemIndex", 0);
		setActivityIntent(intent);
		activity = getActivity();
	}

	public void testNameValue() {
		TextView name = (TextView) activity
				.findViewById(R.id.expenseItemNameValue);
		assertEquals("name is not right", name.getText().toString(),
				"test item");
	}

	public void testDateValue() {
		TextView date = (TextView) activity
				.findViewById(R.id.expenseItemDateValue);
		assertEquals("date is not right", date.getText().toString(),
				"01-01-2000");
	}

	public void testCategoryValue() {
		TextView category = (TextView) activity
				.findViewById(R.id.expenseItemCategoryValue);
		assertEquals("category is not right", category.getText().toString(),
				"air fare");
	}

	public void testAmountValue() {
		TextView amount = (TextView) activity
				.findViewById(R.id.expenseItemAmountValue);
		assertEquals("amount is not right", amount.getText().toString(),
				"0.125");
	}

	public void testCurrencyValue() {
		TextView currency = (TextView) activity
				.findViewById(R.id.expenseItemCurrencyValue);
		assertEquals("currency is not right", currency.getText().toString(),
				"CAD");
	}

	public void testDescriptionValue() {
		TextView description = (TextView) activity
				.findViewById(R.id.expenseItemDescriptionValue);
		assertEquals("description is not right", description.getText()
				.toString(), "Test Item");
	}

	public void testReceiptValue() {

		TextView receipt = (TextView) activity
				.findViewById(R.id.expenseItemReceiptValue);
		assertEquals("receipt is not right", receipt.getText().toString(),
				"Not Present");
	}
}
