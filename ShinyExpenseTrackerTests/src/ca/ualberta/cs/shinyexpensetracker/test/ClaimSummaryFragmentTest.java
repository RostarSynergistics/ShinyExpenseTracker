package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

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
		ExpenseClaim.Status status = ExpenseClaim.Status.RETURNED;
		ExpenseClaim claim = new ExpenseClaim(claimName, startDate, endDate);
		claim.setStatus(status);
		TagList tagList = new TagList();
		Tag tag = new Tag("testTag");
		tagList.addTag(tag);
		claim.setTagList(tagList);
		ecc.addExpenseClaim(claim);
		BigDecimal amount = new BigDecimal(10);
		ExpenseItem expense = new ExpenseItem("expenseItemName", startDate, Category.ACCOMODATION, 
				amount, Currency.CAD, "expenseItemDescription");
		claim.addExpense(expense);
		String CADexpenseTotal = amount.toString() + Currency.CAD.toString();
		
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
		
		//ListView expenseTotals = (ListView) frag.getView().findViewById(R.id.claimExpenseTotalsListView);
		//assertEquals("Claim expense totals not set correctly", CADexpenseTotal, expenseTotals.getChildAt(0).toString());
	}
	
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
	
	public void testExpenseTotals() {
		getInstrumentation().runOnMainSync(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}