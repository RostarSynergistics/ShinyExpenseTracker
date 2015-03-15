package ca.ualberta.cs.shinyexpensetracker.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;

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
		ExpenseClaim.Status status = ExpenseClaim.Status.SUBMITTED;
		Tag tag = new Tag("testTag");
		ExpenseClaim claim = new ExpenseClaim("testClaim", startDate, endDate, status, tag);
		ecc.addExpenseClaim(claim);
		ExpenseItem expense = new ExpenseItem("test Expense", startDate, Category.MEAL, 
				new BigDecimal(10.00), Currency.CAD, "description test");
		claim.addExpense(expense);
		
		//int claimIndex = getActivity().getIntent().getIntExtra("claimIndex", -1);
		//claim = ecc.getExpenseClaim(claimIndex);
		
		// This part is just for now need to actually get claim that was clicked on
		int claimIndex = 0;
		claim = ecc.getExpenseClaim(claimIndex);
		TextView claimName = (TextView) view.findViewById(R.id.claimNameTextView);
		TextView claimStatus = (TextView) view.findViewById(R.id.claimStatusTextView);
		TextView claimStartDate = (TextView) view.findViewById(R.id.claimStartDateTextView);
		TextView claimEndDate = (TextView) view.findViewById(R.id.claimEndDateTextView);
		TextView claimTags = (TextView) view.findViewById(R.id.claimTagsTextView);
		
		
		if(claim.getExpenses().size() != 0) {
			//Need to get a list currencies and their total amount of all expenses in claim
			ListView expenseTotals = (ListView) view.findViewById(R.id.claimExpenseTotalsListView);
			ArrayList<ExpenseItem> expenses = claim.getExpenses();
			int cad = 0, usd = 0, gbp = 0, chf = 0, cny = 0, eur = 0, jpy = 0;
			for (int i = 0; i < expenses.size(); i++) {
				switch (expenses.get(i).getCurrency()) {
				case CAD:
					cad += expenses.get(i).getAmountSpent().intValue();
				case USD:
					usd += expenses.get(i).getAmountSpent().intValue();
				case GBP:
					gbp += expenses.get(i).getAmountSpent().intValue();
				case CHF:
					chf += expenses.get(i).getAmountSpent().intValue();
					break;
				case CNY:
					cny += expenses.get(i).getAmountSpent().intValue();
					break;
				case EUR:
					eur += expenses.get(i).getAmountSpent().intValue();
					break;
				case JPY:
					jpy += expenses.get(i).getAmountSpent().intValue();
					break;
				default:
					break;
				}
			}
			ArrayList<String> list = new ArrayList<String>();
			if (cad != 0){
				list.add(Currency.CAD + " " + cad);
			} 
			if (usd != 0) {
				list.add(Currency.USD + " " + usd);
			} 
			if (gbp != 0) {
				list.add(Currency.GBP + " " + gbp);
			} 
			if (chf != 0) {
				list.add(Currency.CHF + " " + chf);
			} 
			if (cny != 0) { 
				list.add(Currency.CNY + " " + cny);
			} 
			if (eur != 0) {
				list.add(Currency.EUR + " " + eur);
			} 
			if (jpy != 0) {
				list.add(Currency.JPY + " " + jpy);
			}
		}
		
		//claimName.setText(cc.getExpenseClaim(claimIndex).getName());
		claimName.setText(claim.getName());
		claimStatus.setText("Claim Status: " + claim.getStatus().getText());
		claimStartDate.setText("Start Date: " + claim.getStartDate());
		claimEndDate.setText("End Date: " + claim.getEndDate());
		claimTags.setText("Tags: " + claim.getTag());
	}

}