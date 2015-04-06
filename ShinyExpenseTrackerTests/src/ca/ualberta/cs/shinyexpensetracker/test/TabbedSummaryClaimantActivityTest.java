package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.AddTagToClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.RemoveTagFromClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryClaimantActivity;
import ca.ualberta.cs.shinyexpensetracker.fragments.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class TabbedSummaryClaimantActivityTest extends
		ActivityInstrumentationTestCase2<TabbedSummaryClaimantActivity> {

	public TabbedSummaryClaimantActivityTest(
			Class<TabbedSummaryClaimantActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public TabbedSummaryClaimantActivityTest() {
		super(TabbedSummaryClaimantActivity.class);
	}

	static ClaimSummaryFragment frag;
	TabbedSummaryActivity activity;

	ExpenseClaim claim;

	String claimName = "test claim name";
	Date startDate = new Date(1000);
	Date endDate = new Date(2000);
	ExpenseClaim.Status status = ExpenseClaim.Status.IN_PROGRESS;
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
	 * Test if a claim is marked as 'Incomplete' and the 'Submit Claim' menu
	 * item is selected that a dialog appears and that the claim's status is not
	 * changed
	 */
	public void testSubmitClaimOnIncompleteClaim() {

		expense.setIncompletenessMarker(ExpenseItem.INCOMPLETE);

		// Press the "Submit claim" button
		getInstrumentation()
				.invokeMenuActionSync(activity, R.id.submitClaim, 0);

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		assertTrue("cannot sumbit incomplete claim dialog not showing",
				activity.getDialog().isShowing());

		assertTrue("Claim status was changed to submitted", !claim.getStatus()
				.equals(Status.SUBMITTED));
		assertEquals("status has been changed", status, claim.getStatus());
	}

	/**
	 * Test if a claim status is changed to submitted when the 'Submit claim'
	 * menu item is selected and all expenses are marked as 'Complete'
	 */
	public void testSubmitClaimOnCompleteClaim() {

		expense.setIncompletenessMarker(ExpenseItem.COMPLETE);

		// Monitor for AddTagsActivity
		ActivityMonitor addTagsActivityMonitor = getInstrumentation()
				.addMonitor(AddTagToClaimActivity.class.getName(), null, false);
		// Monitor for RemoveTagActivity
		ActivityMonitor removeTagsActivityMonitor = getInstrumentation()
				.addMonitor(RemoveTagFromClaimActivity.class.getName(), null, false);

		// Press the "Submit claim" button
		getInstrumentation()
				.invokeMenuActionSync(activity, R.id.submitClaim, 0);

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		assertEquals("Claim not submitted", Status.SUBMITTED, claim.getStatus());

		// make sure menu items to edit claim are disabled
		assertFalse("'Submit claim' menu item still enabled", getInstrumentation().invokeMenuActionSync(activity, R.id.submitClaim, 0));
		assertFalse("'edit claim' menu item still enabled", getInstrumentation().invokeMenuActionSync(activity,	R.id.editClaim, 0));
		assertFalse("'Add Expense Item' menu item still enabled", getInstrumentation().invokeMenuActionSync(activity, R.id.addExpenseItem, 0));
		assertFalse("'Add Destination' menu item still enabled", getInstrumentation().invokeMenuActionSync(activity, R.id.addDestination, 0));

		// make sure you can still add a tag to the claim
		assertTrue("'Add Tag' menu item disabled", getInstrumentation().invokeMenuActionSync(activity, R.id.addTag, 0));
		
		// Get the view expense claims activity
		final AddTagToClaimActivity addTagsActivity = (AddTagToClaimActivity) getInstrumentation()
				.waitForMonitorWithTimeout(addTagsActivityMonitor, 1000);
		assertEquals(true, getInstrumentation().checkMonitorHit(addTagsActivityMonitor, 1));

		addTagsActivity.finish();
		
		assertTrue("'Remove Tag' menu item disabled", getInstrumentation().invokeMenuActionSync(activity, R.id.RemoveTag, 0));
		
		final RemoveTagFromClaimActivity removeTagsActivity = (RemoveTagFromClaimActivity) getInstrumentation()
				.waitForMonitorWithTimeout(removeTagsActivityMonitor, 1000);
		assertEquals(true, getInstrumentation().checkMonitorHit(addTagsActivityMonitor, 1));

		removeTagsActivity.finish();
	}

}
