package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemDetailActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Test suite to test activity that lets the user view information of the
 * selected ExpenseItem
 * 
 * Covers Issues 16, 36
 * 
 * @author Oleg Oleynikov
 * @version 1.1
 * @since 2015-03-16
 */
public class TestExpenseItemDetailView extends
		ActivityInstrumentationTestCase2<ExpenseItemDetailActivity> {

	public TestExpenseItemDetailView() {
		super(ExpenseItemDetailActivity.class);
	}

	ExpenseItemDetailActivity activity;
	ExpenseClaimController controller;
	ExpenseItem item;
	Bitmap imageSmall;
	Resources res;
	
	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		controller = new ExpenseClaimController(
				new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim("test claim");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);

		res = getInstrumentation().getTargetContext().getResources();
		imageSmall = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		
		item = new ExpenseItem("test item", newDate.getTime(),
				Category.fromString("air fare"), new BigDecimal("0.125"),
				Currency.CAD, "Test Item", imageSmall);

		claim.addExpense(item);
		claimList.addClaim(claim);

		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		intent.putExtra("expenseIndex", 0);
		setActivityIntent(intent);
		activity = getActivity();
	}
	
	/**
	 * Tests if the menu opens the expense for editing
	 */
	public void testEditExpense() {
		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseMonitor = getInstrumentation().addMonitor(ExpenseItemActivity.class.getName(), null, false);
		
		// Press "Edit Claim"
		getInstrumentation().invokeMenuActionSync(activity, R.id.editClaim, 0);
		
		// Get the activity
		final ExpenseItemActivity expenseActivity = (ExpenseItemActivity) getInstrumentation().waitForMonitorWithTimeout(expenseMonitor, 1000);
		assertEquals("Did not open the expense activity", true, getInstrumentation().checkMonitorHit(expenseMonitor, 1));
		
		// Check that the activity received valid intents
		assertEquals(0, expenseActivity.getIntent().getIntExtra("claimIndex", -1000));
		assertEquals(0, expenseActivity.getIntent().getIntExtra("expenseIndex", -1000));
		
		// Close the activity
		expenseActivity.finish();
		getInstrumentation().waitForIdleSync();
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
				"Present");
	}
	public void testRemoveReceipt(){
		assertNotNull("receipt not present in item", item.getReceiptPhoto());
		clickRemoveReceipt();
		assertNull("did not remove receipt photo", item.getReceiptPhoto());
	}
	
	private void clickRemoveReceipt() {
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				Button removeReceiptButton = (Button) activity
						.findViewById(R.id.removeReceiptButton);
				removeReceiptButton.callOnClick();
			}
		});
		getInstrumentation().waitForIdleSync();

	}
	
	/**
	 * Tests for a bug where the activity's view is not updated
	 * after an edit to the expense.
	 * @throws ParseException 
	 */
	public void testEditUpdatesViews() throws ParseException {
		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseMonitor = getInstrumentation().addMonitor(ExpenseItemActivity.class.getName(), null, false);

		
		// Press "Edit Claim"
		getInstrumentation().invokeMenuActionSync(activity, R.id.editClaim, 0);
		
		// Get the activity
		final ExpenseItemActivity expenseActivity = (ExpenseItemActivity) getInstrumentation().waitForMonitorWithTimeout(expenseMonitor, 1000);
		assertEquals("Did not open the expense activity", true, getInstrumentation().checkMonitorHit(expenseMonitor, 1));
		
		// Fill the activity
		final String veryDifferentName = "DIFFERENT NAME A9T1091A";
		expenseActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EditText nameText = (EditText) expenseActivity.findViewById(R.id.expenseItemNameEditText);
				
				// Change data
				nameText.setText(veryDifferentName);
				
				// Close the activity
				try {
					expenseActivity.doneExpenseItem(expenseActivity.findViewById(R.id.expenseItemDoneButton));
				} catch (ParseException e) {
					fail();
					e.printStackTrace();
				}
			}
		});
		// Sync
		getInstrumentation().waitForIdleSync();
		
		// Check that the data in the detail view was updated
		TextView name = (TextView) activity.findViewById(R.id.expenseItemNameValue);
		
		// Check that the data was updated
		assertEquals(veryDifferentName, name.getText().toString());
	}
}
