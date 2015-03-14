package ca.ualberta.cs.shinyexpensetracker.activities;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.ExpenseTotalsAdapter;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ClaimSummaryFragment extends Fragment {

	private ExpenseClaim claim;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	private View view;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ClaimSummaryFragment newInstance(int sectionNumber) {
		ClaimSummaryFragment fragment = new ClaimSummaryFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ClaimSummaryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tab_claim_summary,
				container, false);
		view = rootView;
		setClaimInfo(view);
		return rootView;
	}
	
	public void setClaimInfo(View view) {
		ExpenseClaimController ecc = ExpenseClaimController.getInstance(); 
		
		Date startDate = new Date(1000);
		Date endDate = new Date(2000);
		//ExpenseClaim.Status status = ExpenseClaim.Status.SUBMITTED;
		//TagList tagList = new TagList();
		//Tag tag = new Tag("testTag");
		//tagList.addTag(tag);
		ExpenseClaim claim = new ExpenseClaim("testClaim", startDate, endDate);
		ecc.addExpenseClaim(claim);
		//ExpenseItem expense = new ExpenseItem("test Expense", startDate, Category.MEAL, 
		//		new BigDecimal(10.00), Currency.CAD, "description test");
		//claim.addExpense(expense);
		
		int claimIndex = getActivity().getIntent().getIntExtra("claimIndex", -1);
		//claim = ecc.getExpenseClaim(claimIndex);
		
		// This part is just for now need to actually get claim that was clicked on
		claimIndex = 0;
		try {
			claim = ecc.getExpenseClaim(claimIndex);
		} catch (IndexOutOfBoundsException e){
			throw new RuntimeException();
		}
		TextView claimName = (TextView) view.findViewById(R.id.claimNameTextView);
		TextView claimStatus = (TextView) view.findViewById(R.id.claimStatusTextView);
		TextView claimStartDate = (TextView) view.findViewById(R.id.claimStartDateTextView);
		TextView claimEndDate = (TextView) view.findViewById(R.id.claimEndDateTextView);
		TextView claimTags = (TextView) view.findViewById(R.id.claimTagsTextView);
		
		
		if(claim.getExpenses().size() != 0) {
			//Need to get a list currencies and their total amount of all expenses in claim
			ListView expenseTotals = (ListView) view.findViewById(R.id.claimExpenseTotalsListView);
			expenseTotals.setAdapter(new ExpenseTotalsAdapter(claim, getActivity().getBaseContext()));

		} else {
			// no expenses to list, show message saying "No expenses"
			TextView noExpenses = (TextView) view.findViewById(R.id.noExpensesTextView);
			noExpenses.setVisibility(view.VISIBLE);
		}
		
		claimName.setText(claim.getName());
		claimStatus.setText("Claim Status: " + claim.getStatus().getText());
		claimStartDate.setText("Start Date: " + claim.getStartDate());
		claimEndDate.setText("End Date: " + claim.getEndDate());
		
		String tags;
		if (claim.getTagList() != null)	{
			tags = claim.getTagList().toString();
		} else {
			tags = "";
		}
		claimTags.setText("Tags: " + tags);
	}

}