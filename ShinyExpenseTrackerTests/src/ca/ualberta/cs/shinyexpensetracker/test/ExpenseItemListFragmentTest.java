package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemListFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

public class ExpenseItemListFragmentTest extends
	ActivityInstrumentationTestCase2<TabbedSummaryActivity> {
	
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
		// Use a clean claimList
		ExpenseClaimController.getInstance().setClaimList(new ExpenseClaimList());
		
		claim = new ExpenseClaim(
				"My Cool Expense Claim",
				new Date(123456),
				new Date(234567)
				);
		// Add an expense that we can look at
		claim.addExpense(new ExpenseItem(
				"Really expensive thing",
				new Date(159371),
				ExpenseItem.Category.SUPPLIES,
				new BigDecimal(1000000),
				ExpenseItem.Currency.CAD,
				"Something really shiny",
				null));
		// Add the expense claim
		ExpenseClaimController.getInstance().addExpenseClaim(claim);
		
		// Inject an intent that we have full control of.
		// This MUST be called before the first call to getActivity()
		// Source: http://developer.android.com/reference/android/test/ActivityInstrumentationTestCase2.html#setActivityIntent%28android.content.Intent%29
		//		March 13, 2015
		
		// Request the claim we just added
		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
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
		assertNotNull(frag.getView()
				.findViewById(R.id.expenseItemsListView));
	}
	
	public void testEditExpenseShowsActivity() {
		final ListView listview = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);
		
		// http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
		// March 13, 2015
		
		// Use a monitor to listen for activity changes
		ActivityMonitor monitor = getInstrumentation().addMonitor(AddExpenseClaimActivity.class.getName(), null, false);

		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// Get the view at index 0 and create a new item to fill it.
				View view = listview.getAdapter().getView(0, null, listview);
				// Click the item view.
				listview.performItemClick(view, 0, listview.getAdapter().getItemId(0));
			}
		});
		
		// Wait up to 5 seconds for the next activity open, if available,
		// timing out if it blocks.
		AddExpenseClaimActivity nextActivity = (AddExpenseClaimActivity) getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
		assertNotNull("Next activity wasn't opened", nextActivity);
		nextActivity.finish();
	}
	
	/**
	 * Test deleting an existing expense.
	 */
	public void testDeleteExpense() {
		// Fake the functionality of long pressing the listview
		// because that method doesn't seem to be exposed.
		
		ListView expenseList = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);
		
		// Make sure we have 1 thing before
		assertEquals(1, expenseList.getCount());
		assertEquals(1, ExpenseClaimController.getInstance().getCount());
		
		// Delete the expense at index 0
		frag.deleteExpenseAt(0);
		
		// Check that the listview removed an item (UI -> UI)
		assertEquals(0, expenseList.getCount());
		// Check that the controller removed an item (UI -> Model)
		assertEquals(0, ExpenseClaimController.getInstance().getCount());
	}
	
	/**
	 * Tests that new expenses update the interface
	 */
	public void testNewExpensesAreAdded() {
		ListView expenseList = (ListView) frag.getView().findViewById(R.id.expenseItemsListView);
		// Make sure we still have that one claim from before
		// - Sanity check
		assertEquals(1, claim.getExpenses().size());
		// - Check the listview for the same number of things  
		assertEquals(1, expenseList.getCount());
		
		// Add another expense
		claim.addExpense(new ExpenseItem(
				"TDD things",
				new Date(123123),
				ExpenseItem.Category.ACCOMODATION,
				new BigDecimal(1000000),
				ExpenseItem.Currency.CAD,
				"Look out! It's TDD!",
				null));
		
		// Make sure we have a new claim
		// - Sanity check
		assertEquals(2, claim.getExpenses().size());
		// - Check the listview for the new number of things
		assertEquals(2, expenseList.getCount());
	}
}
