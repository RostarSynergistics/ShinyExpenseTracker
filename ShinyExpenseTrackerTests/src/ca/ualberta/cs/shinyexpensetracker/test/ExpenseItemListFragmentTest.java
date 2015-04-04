package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemDetailActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.fragments.ExpenseItemListFragment;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class ExpenseItemListFragmentTest extends ActivityInstrumentationTestCase2<TabbedSummaryActivity> {

	static ExpenseItemListFragment frag;
	TabbedSummaryActivity activity;
	ExpenseClaim claim;

	public ExpenseItemListFragmentTest(Class<TabbedSummaryActivity> activityClass) {
		super(activityClass);
	}

	public ExpenseItemListFragmentTest() {
		super(TabbedSummaryActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList list = new ExpenseClaimList();

		final ExpenseClaimController controller = new ExpenseClaimController(new MockExpenseClaimListPersister(list));
		Application.setExpenseClaimController(controller);

		claim = new ExpenseClaim("My Cool Expense Claim", new Date(123456), new Date(234567));
		// Add an expense that we can look at
		claim.addExpense(new ExpenseItem("Really expensive thing",
				new Date(159371),
				ExpenseItem.Category.SUPPLIES,
				new BigDecimal(33000),
				ExpenseItem.Currency.CAD,
				"Something really shiny",
				null));
		// Add the expense claim
		list.addClaim(claim);

		// Inject an intent that we have full control of.
		// This MUST be called before the first call to getActivity()
		// Source:
		// http://developer.android.com/reference/android/test/ActivityInstrumentationTestCase2.html#setActivityIntent%28android.content.Intent%29
		// March 13, 2015

		// Request the claim we just added
		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(intent);

		// Now it's safe to get the activity
		activity = getActivity();
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// Get me a shiny expense list tab
				ExpenseItemListFragmentTest.frag = activity.selectExpenseListTab();
			}
		});
		getInstrumentation().waitForIdleSync();

		// Can't do anything without a view
		View view = frag.getView();
		assertNotNull("No view", view);
	}

	/**
	 * Checks that we can access views within the fragment.
	 */
	public void testFragViewsNotNothing() {
		// Try to fetch the list view in the fragment.
		assertNotNull(frag.getView().findViewById(R.id.expenseItemsListView));
	}

	/**
	 * Checks that tapping an item in the list view will open the appropriate
	 * activity
	 */
	public void testViewExpenseShowsActivity() {
		final ListView listview = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);

		// http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
		// March 13, 2015

		// Use a monitor to listen for activity changes
		ActivityMonitor monitor = getInstrumentation().addMonitor(ExpenseItemDetailActivity.class.getName(),
				null,
				false);

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// Get the view at index 0 and create a new item to fill it.
				View view = listview.getAdapter().getView(0, null, listview);
				// Click the item view.
				listview.performItemClick(view, 0, listview.getAdapter().getItemId(0));
			}
		});

		getInstrumentation().waitForIdleSync();

		// Wait up to 5 seconds for the next activity open, if available,
		// timing out if it blocks.
		Activity nextActivity = (Activity) getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
		assertTrue("Next activity wasn't opened", getInstrumentation().checkMonitorHit(monitor, 1));
		nextActivity.finish();
	}

	/**
	 * Test deleting an existing expense.
	 * 
	 * @throws InterruptedException
	 */
	public void testDeleteExpense() throws InterruptedException {
		// Fake the functionality of long pressing the listview
		// because that method doesn't seem to be exposed.

		ListView expenseList = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);

		// Make sure we have 1 thing before
		assertEquals(1, expenseList.getCount());
		assertEquals(1, claim.getExpenseCount());

		// Make sure the dialog isn't real
		assertNull(frag.getLastDialog());

		// Delete the expense at index 0
		frag.askDeleteExpenseAt(0);

		// Get a dialog
		AlertDialog deleteDialog = frag.getLastDialog();

		// Make sure the dialog is real and is showing
		assertNotNull(frag.getLastDialog());
		assertTrue("Dialog not showing", deleteDialog.isShowing());

		// (Fake) click OK. (The most painful thing for some reason).
		// --> All this seems to do is tell us if there was something that
		// could be clicked and that it could be performed. It still
		// fails to do anything for no apparent reason
		Button button = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		assertTrue(button.performClick());

		// If "performClick" doesn't perform click...
		if (frag.getLastDialog() != null) {
			// Forcefully induce the event that was supposed to be called
			// because nothing seems to do that.
			getInstrumentation().runOnMainSync(new Runnable() {

				@Override
				public void run() {
					frag.deleteExpenseAt(0);
				}
			});
		}

		// Check that the controller removed an item (UI -> Model)
		assertEquals(0, claim.getExpenseCount());
		// Check that the list view removed an item (UI -> UI)
		assertEquals(0, expenseList.getCount());
	}

	/**
	 * Tests that new expenses update the interface
	 */
	public void testNewExpensesAreAdded() {
		ListView expenseList = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);
		// Make sure we still have that one claim from before
		// - Sanity check
		assertEquals(1, claim.getExpenseCount());
		// - Check the list view for the same number of things
		assertEquals(1, expenseList.getCount());

		// Add another expense
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claim.addExpense(new ExpenseItem("TDD things",
						new Date(123123),
						ExpenseItem.Category.ACCOMODATION,
						new BigDecimal(1000000),
						ExpenseItem.Currency.CAD,
						"Look out! It's TDD!",
						null));
			}
		});
		getInstrumentation().waitForIdleSync();
		// Make sure we have a new claim
		// - Sanity check
		assertEquals(2, claim.getExpenseCount());
		// - Check the list view for the new number of things
		assertEquals(2, expenseList.getCount());
	}

	public void testExpenseListVisibility() {
		TextView noExpensePrompt = (TextView) frag.getView().findViewById(R.id.noExpensesTextView);

		// Sanity check
		assertEquals(1, claim.getExpenseCount());

		// Stuff to display:
		// --> Check that the list is visible
		assertEquals(View.INVISIBLE, noExpensePrompt.getVisibility());

		// Remove the item
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				claim.removeExpense(0);
			}
		});
		getInstrumentation().waitForIdleSync();

		// Sanity check
		assertEquals(0, claim.getExpenseCount());

		// Nothing to display:
		// --> Check that the prompt is visible
		assertEquals(View.VISIBLE, noExpensePrompt.getVisibility());
	}
}
