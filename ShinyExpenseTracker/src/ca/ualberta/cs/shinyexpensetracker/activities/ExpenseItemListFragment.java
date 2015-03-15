package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ExpenseItemListFragment extends Fragment implements IView<ExpenseClaim> {

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ExpenseClaim claim;
	private int claimIndex;
	private ExpenseItemAdapter adapter;
	private AlertDialog lastDialog;
	
	public ExpenseItemListFragment() {
	}
	
	public ExpenseItemListFragment(int sectionNumber) {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		setArguments(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.tab_expense_list,
				container, false);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();

		// Get the claim index to display
		Intent intent = getActivity().getIntent();
		claimIndex = intent.getIntExtra("claimIndex", -1);
		if (claimIndex == -1) {
			throw new RuntimeException("Intent not passed: Got claim index of -1");
		}
		
		// Get the claim context
		try {
			claim = new ExpenseClaimController(getActivity()).getExpenseClaim(claimIndex);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Inform the model that we're listening for updates.
		claim.addView(this);

		// Set up view visibility
		setPromptVisibility();
		
		// Set up the list view to display data
		ListView expenses = (ListView) getView().findViewById(R.id.expenseItemsListView);
		adapter = new ExpenseItemAdapter(claim, getActivity().getBaseContext());
		expenses.setAdapter(adapter);
		
		// -- On Click : Edit -- //
		expenses.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editExpenseAt(position);
			}
		});
		
		// -- On Long Click : Delete -- //
		expenses.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				askDeleteExpenseAt(position);
				return true;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * Prompts the user for deletion of an expense at a given position
	 * @param position the position in the list view to delete
	 */
	public void askDeleteExpenseAt(final int position) {
		// Construct a new dialog to be displayed.
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		// -- Delete this expense item?
		builder.setTitle( getString(R.string.deleteExpenseItemPromptTitle) );
		builder.setMessage( getString(R.string.deleteExpenseItemPromptMessage) );
		// -- OK -- //
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// remove the expense
					deleteExpenseAt(position);
					dialog.dismiss();
					// Nullify the last opened dialog so we can tell it was dismissed
					lastDialog = null;
				}
			});
		// -- Cancel -- //
		builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
					dialog.dismiss();
					// Nullify the last opened dialog so we can tell it was dismissed
					lastDialog = null;
				}
			});
		
		// Show the newly created dialog
		lastDialog = builder.create();
		lastDialog.show();
	}

	/**
	 * Opens the activity responsible for editing a claim
	 * @param position the position in the listview to edit.
	 */
	public void editExpenseAt(int position) {
		// Create an intent to edit an expense item
		Intent intent = new Intent(getActivity(), ExpenseItemActivity.class);
		// --> Tell it that we're editing the index at this position
		intent.putExtra("claimIndex", claimIndex);
		intent.putExtra("expenseIndex", position);
		
		// Start the activity with our edit intent
		startActivity(intent);
	}
	
	/**
	 * Deletes the expense at position without prompt
	 * @param position
	 */
	public void deleteExpenseAt(int position) {
		claim.removeExpense(position);
	}

	@Override
	public void update(ExpenseClaim m) {
		// Model was updated, inform the views
		adapter.notifyDataSetChanged();
		setPromptVisibility();
	}
	
	/**
	 * Sets visibility for the prompt informing the user that
	 * there are no expenses. Should be called any time the list
	 * needs refreshing.
	 */
	private void setPromptVisibility() {
		TextView noExpenses = (TextView) getView().findViewById(R.id.noExpensesTextView);
		
		// Are there expenses to display?
		if (claim.getExpenseCount() == 0) {
			// No.
			// Set the prompt to be visible
			noExpenses.setVisibility(View.VISIBLE);
		} else {
			// Yes.
			// Set the list to be visible
			noExpenses.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * The last displayed dialog of the fragment
	 * @return Returns the last displayed dialog that is open, or null if it was closed
	 */
	public AlertDialog getLastDialog() {
		return lastDialog;
	}
}
	
