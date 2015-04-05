package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryApproverActivity;
import ca.ualberta.cs.shinyexpensetracker.fragments.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class TabbedSummaryApproverActivityTest extends
		ActivityInstrumentationTestCase2<TabbedSummaryApproverActivity> {

	public TabbedSummaryApproverActivityTest(
			Class<TabbedSummaryApproverActivity> activityClass) {
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
	final ExpenseItem expense = new ExpenseItem("expenseItemName", new Date(
			1000), Category.ACCOMODATION, amount, Currency.CAD,
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

		controller = new ExpenseClaimController(
				new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);

		// Source:
		// http://stackoverflow.com/questions/23728835/in-junit-test-activity-if-it-did-received-the-extra-from-intent
		// On March 14 2015
		// set up a mock intent to allow for passing the claimIndex
		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);

		// Add an expense claim to the expenseClaimController
		claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		controller.addExpenseClaim(claim);
		controller.addExpenseItem(expense, intent.getIntExtra("claimIndex", 0));

		activity = getActivity();

	}
	
	/** 
	 * Test to ensure when comment menu item is clicked that the dialog appears and the the 
	 * string entered into it is saved as a comment to the expense claim
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
	        	EditText commentTextBox = (EditText) activity.getCommentDialog().findViewById(R.id.EditTextDialogComment);
	        	commentTextBox.setText("test comment");
	            activity.getCommentDialog().getButton(DialogInterface.BUTTON_POSITIVE).performClick();
	        }
		});
		
		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();
		
		assertEquals("comment not saved correctly to expense claim", "test comment", controller.getExpenseClaim(0).getComment(0));
		
	}
	
	/**
	 * Tests if claim is approved when a comment has already been added
	 */
	public void testApproveCommentedClaim() {
		
		String comment = "test comment";
		//add a comment to the claim
		controller.getExpenseClaim(0).addComment(comment);
		
		// invoke the approve claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.approveClaim, 0);
		
		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();
		
		assertTrue("No dialog to tell user claim was approved appeared", activity.getApprovedDialog().isShowing());
		
		assertEquals("claim status was not changed to approved", Status.APPROVED, controller.getExpenseClaim(0).getStatus());
	}
	
	/**
	 * Test if claim is not approved when there is no comment on it
	 * @throws InterruptedException 
	 */
	public void testApproveUncommentedClaim() {
		// make sure the claim has no comments
		controller.getExpenseClaim(0).setComments(null);
		
		// invoke the approve claim menu item
		getInstrumentation().invokeMenuActionSync(activity, R.id.approveClaim, 0);
		
		getInstrumentation().waitForIdleSync();
		
		assertTrue("No dialog to tell user they need to add a comment appeared", activity.getCommentNeededDialog().isShowing());
		
		assertEquals("claim status was changed", Status.SUBMITTED, controller.getExpenseClaim(0).getStatus());
	}
}
