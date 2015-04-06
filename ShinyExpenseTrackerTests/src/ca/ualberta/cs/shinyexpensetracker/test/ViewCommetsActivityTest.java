package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ViewCommentsActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class ViewCommetsActivityTest extends ActivityInstrumentationTestCase2<ViewCommentsActivity> {

	ViewCommentsActivity activity;
	ExpenseClaimController controller;
	ExpenseClaim claim;
	ExpenseClaimList claimList;
	ListView commentsListView;
	Intent intent;

	String claimName = "test claim name";
	Date startDate = new Date(1000);
	Date endDate = new Date(2000);
	ExpenseClaim.Status status = ExpenseClaim.Status.SUBMITTED;
	String comment = "test comment";

	public ViewCommetsActivityTest() {
		super(ViewCommentsActivity.class);
	}

	public ViewCommetsActivityTest(Class<ViewCommentsActivity> activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
		super.setUp();

		intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);

		// Add an expense claim to the expenseClaimController
		claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		claim.addComment(comment);

		claimList = new ExpenseClaimList();
		claimList.addClaim(claim);

		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);
	}

	/**
	 * Tests that the claims comments are shown in the listView
	 */
	public void testCommentsList() {

		activity = getActivity();

		ListView comments = (ListView) activity.findViewById(R.id.commentsListView);

		assertEquals("comments list view not set correctly", comment, comments.getItemAtPosition(0).toString());

		getInstrumentation().waitForIdleSync();

	}

	/**
	 * Test that when there are no comments 'No Comments' is displayed
	 */
	public void testNoComments() {
		controller.getExpenseClaimByID(claim.getID()).setComments(new ArrayList<String>());

		assertEquals("still comments in the claim", 0, controller.getExpenseClaimByID(claim.getID()).getComments()
				.size());

		activity = getActivity();

		TextView noComments = (TextView) activity.findViewById(R.id.noCommentsTextView);
		assertEquals("No Expenses not shown", "No Comments", noComments.getText().toString());

		getInstrumentation().waitForIdleSync();

	}
}
