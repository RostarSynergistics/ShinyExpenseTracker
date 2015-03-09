package ca.ualberta.cs.shinyexpensetracker.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimsView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ExpenseClaimsViewTest extends
		ActivityInstrumentationTestCase2<ExpenseClaimsView> {
	
	ExpenseClaimsView activity;
	ExpenseClaimList claimsList;
	
	ListView claimListView;
	
	public ExpenseClaimsViewTest() {
		super(ExpenseClaimsView.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// Inject an empty list so that saving/loading doesn't interfere.
		ExpenseClaimController.DEBUGGING = true;
		claimsList = ExpenseClaimController.injectExpenseClaimList(new ExpenseClaimList());
		
		activity = getActivity();
		claimListView = (ListView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expense_claim_list);
	}
	
	public void testAddedClaimIsVisible() {
		final ExpenseClaim claim = new ExpenseClaim("Test Claim");
		ExpenseClaim visibleClaim;

		// Run on the activity thread.
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claimsList.addClaim(claim);
			}
		});
		
		// Get the last position in the list
		int lastPosition = claimListView.getCount() - 1;
		assertFalse("Claim List is empty", claimListView.getCount() == 0);
		assertFalse("Claim List has too many objects", claimListView.getCount() > 1);
		
		// Get the expense claim object
		visibleClaim = (ExpenseClaim) claimListView.getItemAtPosition(lastPosition);
		assertNotNull("No object found in the list", visibleClaim);
		
		// Ensure that the claim that was added to the list
		// is also the claim in the listview.
		assertTrue("Claim not visible", visibleClaim.equals(claim));
	}

}
