package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.ExpenseTotalsAdapter;
import ca.ualberta.cs.shinyexpensetracker.IView;
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
public class ClaimSummaryFragment extends Fragment implements IView<ExpenseClaim> {

	private ExpenseClaim claim;
	private int claimIndex;
	private ExpenseTotalsAdapter adapter;
	
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
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//get the Claim we are working with
		Intent intent = getActivity().getIntent();
		claimIndex = intent.getIntExtra("claimIndex", -1);
		if (claimIndex == -1) {
			throw new RuntimeException("Intent not passed: Got claim index of -1");
		}
		
		// Get the claim context
		claim = ExpenseClaimController.getInstance().getExpenseClaim(claimIndex);
		
		// Inform the model that we're listening for updates.
		claim.addView(this);

		// Set up the list view to display data
		ListView expenses = (ListView) getView().findViewById(R.id.claimExpenseTotalsListView);
		adapter = new ExpenseTotalsAdapter(claim, getActivity().getBaseContext());
		expenses.setAdapter(adapter);
	}
	
	public void onResume(){
		super.onResume();
		setClaimInfo(view);
	}
	
	/**
	 * Fills in the claim name, status, start date, end date, tags and currency totals 
	 * of any expense items in the summary view.
	 * @param view
	 */
	public void setClaimInfo(View view) {
		// make sure that it is a valid claim
		try {
			claim = ExpenseClaimController.getInstance().getExpenseClaim(claimIndex);
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
			noExpenses.setVisibility(View.INVISIBLE);
		} else {
			// no expenses to list, show message saying "No expenses"
			noExpenses.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void update(ExpenseClaim m) {
		adapter.notifyDataSetChanged();
	}
	
}