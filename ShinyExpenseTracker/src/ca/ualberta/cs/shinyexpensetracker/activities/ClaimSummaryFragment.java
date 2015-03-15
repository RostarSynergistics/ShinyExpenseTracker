package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.ExpenseTotalsAdapter;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;


/**
 * displays a summary of a claims information (name, status, start date, end date,
 * tags, and totals of currencies in expenses
 * 
 * Reached when a claim is selected from claimListView
 * 
 * Can add expenses, tags and destination through menu items
 * 
 * @author Sarah Morris
 *
 */
public class ClaimSummaryFragment extends Fragment {

	private ExpenseClaim claim;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	private View view;

	public ClaimSummaryFragment() {
	}
	
	public ClaimSummaryFragment(int sectionNumber) {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		setArguments(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tab_claim_summary,
				container, false);
		view = rootView;
		return rootView;
	}
	
	public void onResume() {
		super.onResume();

		try {
			setClaimInfo(view);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Fills in the claim name, status, start date, end date, tags and currency totals 
	 * of any expense items in the summary view.
	 * @param view
	 * @throws IOException 
	 */
	public void setClaimInfo(View view) throws IOException {
		ExpenseClaimController ecc = new ExpenseClaimController(getActivity()); 
		
		//get the claim that we are working with
		int claimIndex = getActivity().getIntent().getIntExtra("claimIndex", -1);
		claim = ecc.getExpenseClaim(claimIndex);

		// make sure that it is a valid claim
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
		TextView noExpenses = (TextView) view.findViewById(R.id.noExpensesTextView);
		
		claimName.setText(claim.getName());
		claimStatus.setText("Claim Status: " + claim.getStatus().getText());
		claimStartDate.setText("Start Date: " + claim.getStartDate());
		claimEndDate.setText("End Date: " + claim.getEndDate());
		
		// set the tags
		String tags;
		if (claim.getTagList() != null)	{
			//claim has tags, get them
			tags = claim.getTagList().toString();
		} else {
			//claim has no tags, don't display anything
			tags = "";
		}
		claimTags.setText("Tags: " + tags);
		
		//display the expense totals
		if(claim.getExpenses().size() != 0) {
			//Need to get a list currencies and their total amount of all expenses in claim
			ListView expenseTotals = (ListView) view.findViewById(R.id.claimExpenseTotalsListView);
			expenseTotals.setAdapter(new ExpenseTotalsAdapter(claim, getActivity().getBaseContext()));
			noExpenses.setVisibility(view.INVISIBLE);
		} else {
			// no expenses to list, show message saying "No expenses"
			noExpenses.setVisibility(view.VISIBLE);
		}
	}
	
}