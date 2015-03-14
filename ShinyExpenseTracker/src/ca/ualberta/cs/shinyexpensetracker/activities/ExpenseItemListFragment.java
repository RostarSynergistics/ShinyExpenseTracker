package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ExpenseItemListFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ExpenseClaim claim;
	private int claimIndex;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ExpenseItemListFragment newInstance(int sectionNumber) {
		ExpenseItemListFragment fragment = new ExpenseItemListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ExpenseItemListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.tab_expense_list,
				container, false);
		Intent intent = getActivity().getIntent();
		claimIndex = intent.getIntExtra("claimIndex", -1);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();

		claim = ExpenseClaimController.getInstance().getExpenseClaim(claimIndex);
		
		ListView expenses = (ListView) getView().findViewById(R.id.expenseItemsListView);
		expenses.setAdapter(new ExpenseItemAdapter(claim, getActivity().getBaseContext()));
		
		// -- On Click : Edit -- //
		expenses.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				editExpenseAt(position);
			}
		});
		
		// -- On Long Click : Delete -- //
//		expenses.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
////				askDeleteExpenseAt(position);
//				return true;
//			}
//		});
		
		// -- On Menu -> New Expense -- //
		
	}
	
	/**
	 * Sets the claim context for this fragment view. Provides no
	 * checks that the index is valid.
	 * @param index corresponds to an index in the ExpenseClaimController.
	 */
	public void setClaim(int index) {
		// Get a reference to a real claim.
		claim = ExpenseClaimController.getInstance().getExpenseClaim(index);
	}
	
}
	
