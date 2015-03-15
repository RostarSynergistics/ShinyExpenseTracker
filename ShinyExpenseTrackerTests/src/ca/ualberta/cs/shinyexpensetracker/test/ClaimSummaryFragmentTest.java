package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.activities.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.R;

//Source: http://stackoverflow.com/questions/21156463/junit-testing-for-android-app-with-fragments
//On March 12

public class ClaimSummaryFragmentTest extends
		ActivityInstrumentationTestCase2<TabbedSummaryActivity> {
	
	static ClaimSummaryFragment frag;
	TabbedSummaryActivity activity;
	
	public ClaimSummaryFragmentTest(Class<TabbedSummaryActivity> activityClass) {
		super(activityClass);
	}
	
	public ClaimSummaryFragmentTest() {
		super(TabbedSummaryActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				// Get me a shiny claim summary tab
				ClaimSummaryFragmentTest.frag = activity.selectClaimSummaryTab();
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
	/*public void testNothing() {
		assertNotNull(frag
				.getView()
				.findViewById(R.id.expenseItemsListView));
		// If you get a runtime error here, everything is fantastic. :D
		frag.setClaim(0);
	} */
	
	public void testSetClaimInfo() {
		ExpenseClaimList claimList = new ExpenseClaimList();
		ExpenseClaimController ecc = ExpenseClaimController.getInstance();
		ecc.setClaimList(claimList);
		String claimName = "test claim name";
		Date startDate = new Date(1000);
		Date endDate = new Date(2000);
		ExpenseClaim claim = new ExpenseClaim(claimName, startDate, endDate);
		ecc.addExpenseClaim(claim);
		
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
			
		});
		
		TextView claimNameText = (TextView) frag.getView().findViewById(R.id.claimNameTextView);
		assertEquals("claim name not set correctly", claimName, claimNameText.getText().toString());
		TextView claimStartDateText = (TextView) frag.getView().findViewById(R.id.claimStartDateTextView);
		assertEquals("claim Start date not set correctly", "Start Date: " + startDate.toString(), claimStartDateText.getText().toString());
		TextView claimEndDateText = (TextView) frag.getView().findViewById(R.id.claimEndDateTextView);
		assertEquals("claim end date not set correctly", "End Date: " + endDate.toString(), claimEndDateText.getText().toString());
	}
}