package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryClaimantActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.fragments.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

//Source: http://stackoverflow.com/questions/21156463/junit-testing-for-android-app-with-fragments
//On March 12

/**
 * Tests for the ClaimSummaryFragment. This fragment displays details about a
 * given claim.
 */
public class ClaimSummaryFragmentTest extends ActivityInstrumentationTestCase2<TabbedSummaryClaimantActivity> {

	static ClaimSummaryFragment frag;
	TabbedSummaryActivity activity;

	public ClaimSummaryFragmentTest(Class<TabbedSummaryClaimantActivity> activityClass) {
		super(activityClass);
	}

	public ClaimSummaryFragmentTest() {
		super(TabbedSummaryClaimantActivity.class);
	}

	ExpenseClaim claim;

	String claimName = "test claim name";
	Date startDate = new Date(1000);
	Date endDate = new Date(2000);
	ExpenseClaim.Status status = ExpenseClaim.Status.IN_PROGRESS;

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
		list.addClaim(claim);

		// Source:
		// http://stackoverflow.com/questions/23728835/in-junit-test-activity-if-it-did-received-the-extra-from-intent
		// On March 14 2015
		// set up a mock intent to allow for passing the claim ID
		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(intent);

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
	 * Tests that setClaimInfo sets the correct information for the claim
	 * 
	 * @throws IOException
	 */
	public void testSetClaimInfo() throws Exception {
		final TagList tagList = new TagList();
		Tag tag = new Tag("testTag");
		tagList.addTag(tag);
		// Changing claim will induce adapter updates/UI updates
		// since "setTagList" calls notifyViews. So, we have to
		// run this in the UI thread.
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				claim.setTagList(tagList);
			}
		});
		// We also have to wait for the UI to become idle
		// so that we don't fail unnecessarily.
		getInstrumentation().waitForIdleSync();

		BigDecimal amount = new BigDecimal(10);
		final ExpenseItem expense = new ExpenseItem("expenseItemName",
				new Date(1000),
				Category.ACCOMODATION,
				amount,
				Currency.CAD,
				"expenseItemDescription");

		// Adding an expense updates the UI.
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				claim.addExpenseItem(expense);
				frag.setClaimInfo(frag.getView());
			}
		});
		// wait for sync
		getInstrumentation().waitForIdleSync();

		TextView claimNameText = (TextView) frag.getView().findViewById(R.id.claimNameTextView);
		assertEquals("claim name not set correctly", claimName, claimNameText.getText().toString());

		TextView claimStartDateText = (TextView) frag.getView().findViewById(R.id.claimStartDateTextView);
		assertEquals("claim Start date not set correctly", "Start Date: " + startDate.toString(), claimStartDateText
				.getText().toString());

		TextView claimEndDateText = (TextView) frag.getView().findViewById(R.id.claimEndDateTextView);
		assertEquals("claim end date not set correctly", "End Date: " + endDate.toString(), claimEndDateText.getText()
				.toString());

		TextView statusText = (TextView) frag.getView().findViewById(R.id.claimStatusTextView);
		assertEquals("Claim status not set correctly", "Claim Status: " + status.getText(), statusText.getText()
				.toString());

		TextView tagText = (TextView) frag.getView().findViewById(R.id.claimTagsTextView);

		assertEquals("Claim tags not set correctly", "Tags: " + tag + "  ", tagText.getText().toString());
	}

	/**
	 * Test for message displayed in the UI if there are no expenses to total.
	 */
	public void testNoExpenses() {
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}

		});
		getInstrumentation().waitForIdleSync();
		TextView noExpenses = (TextView) frag.getView().findViewById(R.id.noExpensesTextView);
		assertEquals("No Expenses not shown", "No Expenses", noExpenses.getText().toString());
	}

	/**
	 * Test that when a claim has no tags only the "Tags: " title is shown
	 */
	public void testNoTags() {
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
		});
		getInstrumentation().waitForIdleSync();
		TextView tags = (TextView) frag.getView().findViewById(R.id.claimTagsTextView);
		assertEquals("Tags showns", "Tags: ", tags.getText().toString());
	}

	/**
	 * Test that the expenseTotals displays the correct values after expenses
	 * are added.
	 * 
	 * @throws IOException
	 */
	public void testExpenseTotals() throws Exception {
		Date startDate = new Date(1000);

		BigDecimal amount = new BigDecimal(10);
		final ExpenseItem expense = new ExpenseItem("test Expense",
				startDate,
				Category.ACCOMODATION,
				amount,
				Currency.CAD);
		// Change the activity from somewhere else, since it'll
		// cause an update in the UI and cause thread issues
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				claim.addExpenseItem(expense);
				frag.setClaimInfo(frag.getView());
			}
		});

		// Wait for the UI to finish doing its thing
		getInstrumentation().waitForIdleSync();

		// Test
		ListView expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 10", expenseTotals.getItemAtPosition(0)
				.toString());

		amount = new BigDecimal(20);
		final ExpenseItem expense2 = new ExpenseItem("test expense 2",
				startDate,
				Category.AIR_FARE,
				amount,
				Currency.CHF);

		// Add another expense
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				claim.addExpenseItem(expense2);
				frag.setClaimInfo(frag.getView());
			}
		});
		// Wait for sync
		getInstrumentation().waitForIdleSync();

		// Test
		expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 10", expenseTotals.getItemAtPosition(0)
				.toString());
		assertEquals("expense was not added to expensesTotal list", "CHF 20", expenseTotals.getItemAtPosition(1)
				.toString());

		// Add one more expense
		final ExpenseItem expense3 = new ExpenseItem("test expense 3", startDate, Category.FUEL, amount, Currency.CAD);
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				claim.addExpenseItem(expense3);
				frag.setClaimInfo(frag.getView());
			}
		});
		// sync
		getInstrumentation().waitForIdleSync();

		// Test
		expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 30", expenseTotals.getItemAtPosition(0)
				.toString());
		assertEquals("expense was not added to expensesTotal list", "CHF 20", expenseTotals.getItemAtPosition(1)
				.toString());
	}

}
