package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryApproverActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.ViewCommentsActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class TabbedSummaryApproverActivityTest extends ActivityInstrumentationTestCase2<TabbedSummaryApproverActivity> {

	public TabbedSummaryApproverActivityTest(Class<TabbedSummaryApproverActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public TabbedSummaryApproverActivityTest() {
		super(TabbedSummaryApproverActivity.class);
	}

	TabbedSummaryApproverActivity activity;

	ExpenseClaim claim;

	String claimName = "test claim name";
	Date startDate = new Date(1000);
	Date endDate = new Date(2000);
	ExpenseClaim.Status status = ExpenseClaim.Status.SUBMITTED;
	BigDecimal amount = new BigDecimal(10);
	final ExpenseItem expense = new ExpenseItem("expenseItemName",
			new Date(1000),
			Category.ACCOMODATION,
			amount,
			Currency.CAD,
			"expenseItemDescription");

	ExpenseClaimController controller;

	@Override
	/**
	 * Setup for each test.
	 * Creates and adds a claim and sets an intent
	 * that points to this claim.
	 */
	public void setUp() throws Exception {
		super.setUp();
		ExpenseClaimList list = new ExpenseClaimList();
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(list));
		Application.setExpenseClaimController(controller);

		// Add an expense claim to the expenseClaimController
		claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		claim.addExpenseItem(expense);
		list.addClaim(claim);

		// Source:
		// http://stackoverflow.com/questions/23728835/in-junit-test-activity-if-it-did-received-the-extra-from-intent
		// On March 14 2015
		// set up a mock intent to allow for passing the claimIndex
		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(intent);

		activity = getActivity();
	}

	/**
	 * Test to ensure when comment menu item is clicked that the dialog appears
	 * and the the string entered into it is saved as a comment to the expense
	 * claim
	 */
	public void testCommentMenuItem() {
		// Press the "Comment" menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.addComment, 0);

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		// check if the comment dialog shows up
		assertTrue("Comment dialog not showing", activity.getCommentDialog().isShowing());

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EditText commentTextBox = (EditText) activity.getCommentDialog()
						.findViewById(R.id.EditTextDialogComment);
				commentTextBox.setText("test comment");
				activity.getCommentDialog().getButton(DialogInterface.BUTTON_POSITIVE).performClick();
			}
		});

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		assertEquals("comment not saved correctly to expense claim",
				"test comment" + " — " + Application.getUser().getUserName(), controller.getExpenseClaimByID(claim.getID()).getComment(0));

	}

	/**
	 * Tests if claim is approved when a comment has already been added
	 */
	public void testApproveCommentedClaim() {

		String comment = "test comment";
		// add a comment to the claim
		controller.getExpenseClaimByID(claim.getID()).addComment(comment);

		// invoke the approve claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.approveClaim, 0);

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		assertTrue("No dialog to tell user claim was approved appeared", activity.getApprovedDialog().isShowing());

		assertEquals("claim status was not changed to approved",
				Status.APPROVED,
				controller.getExpenseClaimByID(claim.getID()).getStatus());

		// make sure 'Return claim' menu item is disabled
		assertFalse("'Return Claim' menu item still enabled",
				getInstrumentation().invokeMenuActionSync(activity, R.id.addDestination, 0));
	}

	/**
	 * Test if claim is not approved when there is no comment on it
	 * 
	 * @throws InterruptedException
	 */
	public void testApproveUncommentedClaim() {
		// make sure the claim has no comments
		controller.getExpenseClaimByID(claim.getID()).setComments(new ArrayList<String>());

		// invoke the approve claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.approveClaim, 0);

		getInstrumentation().waitForIdleSync();

		assertTrue("No dialog to tell user they need to add a comment appeared", activity
				.getCommentApproveNeededDialog().isShowing());

		assertEquals("claim status was changed", Status.SUBMITTED, controller.getExpenseClaimByID(claim.getID())
				.getStatus());
	}

	/**
	 * Tests if a claim is returned when 'Return claim' menu item is clicked and
	 * the claim has already been commented on
	 */
	public void testReturnCommentedClaim() {
		// make sure the claim has a comment
		String comment = "test comment";
		controller.getExpenseClaimByID(claim.getID()).addComment(comment);

		// invoke the return claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.returnClaim, 0);
		getInstrumentation().waitForIdleSync();

		// make sure the dialog was showing to tell the user the claim was
		// returned
		assertTrue("No dialog to tell the user the claim was returned", activity.getClaimReturnedDialog().isShowing());

		// make sure the claim status was changed to returned
		assertEquals("claim status is not Returned", Status.RETURNED, controller.getExpenseClaimByID(claim.getID())
				.getStatus());

		// make sure 'Approve claim' menu item is disabled
		assertFalse("'Approve Claim' menu item still enabled",
				getInstrumentation().invokeMenuActionSync(activity, R.id.addDestination, 0));
	}

	/**
	 * Tests if a claim is unable to be returned on 'Return claim' menu item
	 * click if there is no comment on it
	 */
	public void testReturnUncommentedClaim() {
		// make sure the claim doesn't have any comments
		controller.getExpenseClaimByID(claim.getID()).setComments(new ArrayList<String>());

		// invoke the return claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.returnClaim, 0);
		getInstrumentation().waitForIdleSync();

		// make sure the dialog that warns the user they need to comment on the
		// claim before returning it is showing
		assertTrue("No dialog to tell user they need to comment on claim first", activity
				.getCommentNeededReturnDialog().isShowing());

		// make sure the claim status didn't change
		assertEquals("claim status was changed", Status.SUBMITTED, controller.getExpenseClaimByID(claim.getID())
				.getStatus());

	}

	/**
	 * Tests that the viewCommentsAcitity is started on 'View Comments' menu
	 * item click
	 */
	public void testViewCommentsMenuItem() {

		// Monitor for AddTagsActivity
		ActivityMonitor viewCommentsActivityMonitor = getInstrumentation()
				.addMonitor(ViewCommentsActivity.class.getName(), null, false);

		// invoke 'View Comments' menu item click
		getInstrumentation().invokeMenuActionSync(activity, R.id.viewComments, 0);
		getInstrumentation().waitForIdleSync();

		// Get the view comments activity
		final ViewCommentsActivity viewCommentsActivity = (ViewCommentsActivity) getInstrumentation()
				.waitForMonitorWithTimeout(viewCommentsActivityMonitor, 1000);

		assertEquals(true, getInstrumentation().checkMonitorHit(viewCommentsActivityMonitor, 1));

		viewCommentsActivity.finish();
	}

}
