package ca.ualberta.cs.shinyexpensetracker.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;
import android.view.View;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemListFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;

public class ExpenseItemListActivityTest extends
	ActivityInstrumentationTestCase2<TabbedSummaryActivity> {
	
	static ExpenseItemListFragment frag;
	TabbedSummaryActivity activity;

	public ExpenseItemListActivityTest(Class<TabbedSummaryActivity> activityClass) {
		super(activityClass);
	}
	public ExpenseItemListActivityTest() {
		super(TabbedSummaryActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				// Get me a shiny expense list tab
				ExpenseItemListActivityTest.frag = activity.selectExpenseListTab();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		// Can't do anything without a view
		View view = frag.getView();
		assertNotNull("No view", view);
	}
	
	/**
	 * Checks for swag money in the CoolFragment.
	 */
	public void testNothing() {
		assertNotNull(frag
				.getView()
				.findViewById(R.id.expenseItemsListView));
		// If you get a runtime error here, everything is fantastic. :D
		frag.setClaim(0);
	}
	
}
