package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

//Source: http://stackoverflow.com/questions/21156463/junit-testing-for-android-app-with-fragments
//On March 12

//tests claimSummaryFragment

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
	
	ExpenseClaim claim;
	ExpenseClaimController ecc;
	ExpenseClaimList claimList;
	
	String claimName = "test claim name";
	Date startDate = new Date(1000);
	Date endDate = new Date(2000);
	ExpenseClaim.Status status = ExpenseClaim.Status.RETURNED;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		//Source: http://stackoverflow.com/questions/23728835/in-junit-test-activity-if-it-did-received-the-extra-from-intent
		//On March 14 2015
		//set up a mock intent to allow for passing the claimIndex
		Intent intent = new Intent();
		//intent.setClassName("ca.ualberta.cs.shinyexpensetracker.activities", 
		//		"ca.ualberta.cs.shinyexpensetracker.activities.ClaimSummaryFragment");
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);
		
		claimList = new ExpenseClaimList();
		ecc = ExpenseClaimController.getInstance();
		ecc.setClaimList(claimList);
		
		//Add an expense claim to the expenseClaimController
		ExpenseClaim claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		ecc.addExpenseClaim(claim);
		
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

	//Tests that setClaimInfo sets the correct information for the claim
	public void testSetClaimInfo() {
		
		claimList = new ExpenseClaimList();
		ecc = ExpenseClaimController.getInstance();
		ecc.setClaimList(claimList);
		
		//Add an expense claim to the expenseClaimController
		ExpenseClaim claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		ecc.addExpenseClaim(claim);
		
		TagList tagList = new TagList();
		Tag tag = new Tag("testTag");
		tagList.addTag(tag);
		claim.setTagList(tagList);
		BigDecimal amount = new BigDecimal(10);
		ExpenseItem expense = new ExpenseItem("expenseItemName", new Date(1000), Category.ACCOMODATION, 
				amount, Currency.CAD, "expenseItemDescription");
		
		claim.addExpense(expense);
		
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
			
		});
		
		TextView claimNameText = (TextView) frag.getView().findViewById(R.id.claimNameTextView);
		assertEquals("claim name not set correctly", claimName, claimNameText.getText().toString());
		
		TextView claimStartDateText = (TextView) frag.getView().findViewById(R.id.claimStartDateTextView);
		assertEquals("claim Start date not set correctly", 
				"Start Date: " + startDate.toString(), claimStartDateText.getText().toString());
		
		TextView claimEndDateText = (TextView) frag.getView().findViewById(R.id.claimEndDateTextView);
		assertEquals("claim end date not set correctly", 
				"End Date: " + endDate.toString(), claimEndDateText.getText().toString());
		
		TextView statusText = (TextView) frag.getView().findViewById(R.id.claimStatusTextView);
		assertEquals("Claim status not set correctly", 
				"Claim Status: " + status.getText(), statusText.getText().toString());
		
		TextView tagText = (TextView) frag.getView().findViewById(R.id.claimTagsTextView);
		assertEquals("Claim tags not set correctly", "Tags: " + tag, tagText.getText().toString());
	}
	
	//test that when a claim doesn't have any expenses a textView saying "No Expenses" is shown
	public void testNoExpenses() {
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}

		});
		TextView noExpenses = (TextView) frag.getView().findViewById(R.id.noExpensesTextView);
		assertEquals("No Expenses not shown", "No Expenses", noExpenses.getText().toString());
	}
	
	//Test that when a claim has no tags only the "Tags: " title is shown
	public void testNoTags() {
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
				
			}
		});
		TextView tags = (TextView) frag.getView().findViewById(R.id.claimTagsTextView);
		assertEquals("Tags showns", "Tags: ", tags.getText().toString());
	}
	

	
	//Test that the expeseTotals appear as expected
	public void testExpenseTotals() {
		Date startDate = new Date(1000);
		Date endDate = new Date(2000);
		
		ExpenseClaim claim = new ExpenseClaim("Test claim", startDate, endDate);
		
		BigDecimal amount = new BigDecimal(10);
		ExpenseItem expense = new ExpenseItem("test Expense", startDate, Category.ACCOMODATION, amount, Currency.CAD);
		
		ExpenseClaimList claimList = new ExpenseClaimList();
		ExpenseClaimController ecc = ExpenseClaimController.getInstance();
		ecc.setClaimList(claimList);
		
		ecc.addExpenseClaim(claim);
		ecc.addExpenseItem(expense, 0);

		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
		});
		
		ListView expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 10", 
				expenseTotals.getItemAtPosition(0).toString());
		
		amount = new BigDecimal(20);
		expense = new ExpenseItem ("test expense 2", startDate, Category.AIR_FARE, amount, Currency.CHF);
		ecc.addExpenseItem(expense, 0);
		
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
		});
		
		expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 10", 
				expenseTotals.getItemAtPosition(0).toString());
		assertEquals("expense was not added to expensesTotal list", "CHF 20", 
				expenseTotals.getItemAtPosition(1).toString());
		
		expense = new ExpenseItem("test expense 3", startDate, Category.FUEL, amount, Currency.CAD);
		ecc.addExpenseItem(expense, 0);
		
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				frag.setClaimInfo(frag.getView());
			}
		});
		
		expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		assertEquals("expense was not added to expensesTotal list", "CAD 30", 
				expenseTotals.getItemAtPosition(0).toString());
		assertEquals("expense was not added to expensesTotal list", "CHF 20", 
				expenseTotals.getItemAtPosition(1).toString());
	}
}