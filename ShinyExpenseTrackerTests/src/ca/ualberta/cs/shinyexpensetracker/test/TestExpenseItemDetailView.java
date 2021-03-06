package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemDetailActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.utilities.GlobalDateFormat;

/**
 * Test suite to test activity that lets the user view information of the
 * selected ExpenseItem
 * 
 * Covers Issues 16, 36
 * 
 * @version 1.1
 * @since 2015-03-16
 */
public class TestExpenseItemDetailView extends ActivityInstrumentationTestCase2<ExpenseItemDetailActivity> {

	public TestExpenseItemDetailView() {
		super(ExpenseItemDetailActivity.class);
	}

	ExpenseItemDetailActivity activity;
	ExpenseClaimController controller;
	ExpenseItem item;
	Bitmap imageSmall;
	Coordinate c;
	Resources res;
	private ExpenseClaim claim;

	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);

		claim = new ExpenseClaim("test claim", new Date(1000), new Date(2000));
		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);

		res = getInstrumentation().getTargetContext().getResources();
		imageSmall = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

		c = new Coordinate(1.0, -1.0);

		item = new ExpenseItem("test item",
				newDate.getTime(),
				Category.fromString("air fare"),
				new BigDecimal("0.125"),
				Currency.CAD,
				"Test Item",
				imageSmall,
				c,
				true);

		claim.addExpenseItem(item);
		claimList.addClaim(claim);

		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		intent.putExtra(IntentExtraIDs.EXPENSE_ITEM_ID, item.getID());
		setActivityIntent(intent);
		activity = getActivity();
	}

	/**
	 * Tests if the menu opens the expense for editing
	 */
	public void testEditExpense() {
		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseMonitor = getInstrumentation().addMonitor(ExpenseItemActivity.class.getName(),
				null,
				false);

		// Press "Edit Claim"
		getInstrumentation().invokeMenuActionSync(activity, R.id.editExpenseItem, 0);

		// Get the activity
		getInstrumentation().waitForIdleSync();
		final ExpenseItemActivity expenseActivity = (ExpenseItemActivity) getInstrumentation()
				.waitForMonitorWithTimeout(expenseMonitor, 1000);
		assertEquals("Did not open the expense activity", true, getInstrumentation().checkMonitorHit(expenseMonitor, 1));

		// Check that the activity received valid intents
		assertEquals(claim.getID(), (UUID) expenseActivity.getIntent().getSerializableExtra(IntentExtraIDs.CLAIM_ID));
		assertEquals(item.getID(),
				(UUID) expenseActivity.getIntent().getSerializableExtra(IntentExtraIDs.EXPENSE_ITEM_ID));

		// Close the activity
		expenseActivity.finish();
		getInstrumentation().waitForIdleSync();
	}

	public void testNameValue() {
		TextView name = (TextView) activity.findViewById(R.id.expenseItemNameValue);
		assertEquals("name is not right", name.getText().toString(), "test item");
	}

	public void testDateValue() {
		TextView date = (TextView) activity.findViewById(R.id.expenseItemDateValue);
		assertEquals("date is not right", date.getText().toString(), GlobalDateFormat.makeString(2000, 01, 01));
	}

	public void testCategoryValue() {
		TextView category = (TextView) activity.findViewById(R.id.expenseItemCategoryValue);
		assertEquals("category is not right", category.getText().toString(), "air fare");
	}

	public void testAmountValue() {
		TextView amount = (TextView) activity.findViewById(R.id.expenseItemAmountValue);
		assertEquals("amount is not right", amount.getText().toString(), item.getValueString());
	}

	public void testDescriptionValue() {
		TextView description = (TextView) activity.findViewById(R.id.expenseItemDescriptionValue);
		assertEquals("description is not right", description.getText().toString(), "Test Item");
	}

	public void testGeolocationValue() {
		TextView geoloc = (TextView) activity.findViewById(R.id.expenseItemGeolocationValue);
		assertEquals("geolcoation is not right", geoloc.getText().toString(), c.toString());
	}
	
	/**
	 * Checks if the indicator of a photo is present and behaves correctly. The
	 * indicator in this view is the existence of an attached photo.
	 */
	public void testRemoveReceipt() {
		assertNotNull("receipt not present in item", item.getReceiptPhoto());
		clickRemoveReceipt();
		assertNull("did not remove receipt photo", item.getReceiptPhoto());
		ImageView iv = (ImageView) activity.findViewById(R.id.expenseItemDetailImageButton);
		assertNull("did not remove receipt photo from screen", iv.getDrawable());
	}

	private void clickRemoveReceipt() {
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				Button removeReceiptButton = (Button) activity.findViewById(R.id.removeReceiptButton);
				removeReceiptButton.callOnClick();
			}
		});
		getInstrumentation().waitForIdleSync();

	}

	/**
	 * Tests for a bug where the activity's view is not updated after an edit to
	 * the expense.
	 * 
	 * @throws ParseException
	 */
	public void testEditUpdatesViews() throws ParseException {
		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseMonitor = getInstrumentation().addMonitor(ExpenseItemActivity.class.getName(),
				null,
				false);

		// Press "Edit Claim"
		getInstrumentation().invokeMenuActionSync(activity, R.id.editExpenseItem, 0);

		// Get the activity
		getInstrumentation().waitForIdleSync();
		final ExpenseItemActivity expenseActivity = (ExpenseItemActivity) getInstrumentation()
				.waitForMonitorWithTimeout(expenseMonitor, 1000);
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
				} catch (IOException e) {
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

	/**
	 * Checks if the incompleteness marker behaves correctly
	 */
	public void testMarkIncomplete() {
		final CheckBox incompletenessFlag = (CheckBox) activity.findViewById(R.id.expenseItemCompletenessFlag);

		// False positive check : Marked Incomplete = True
		assertTrue(item.isMarkedIncomplete());
		assertEquals(item.isMarkedIncomplete(), incompletenessFlag.isChecked());

		// Toggle the value
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				incompletenessFlag.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();

		// Check if value changed : MarkedIncomplete = False
		assertFalse(item.isMarkedIncomplete());
		assertEquals(item.isMarkedIncomplete(), incompletenessFlag.isChecked());
	}
}
