package ca.ualberta.cs.shinyexpensetracker.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Adapter for the list Views that display ExpenseItems.
 * It fills the expense_list_item.xml layout file to display
 * Expense Items.
 * 
 * You can use this in place of an ArrayAdapter for ExpenseItems.
 * This gives better flexibility for the adapter.
 * 
 * Example usage:
 * public class MyActivity extends Activity) {
 *  
 *  public setupListView() {
 *  	adapter = new Adapter( claimToWatch, this ) 
 * 		listview.setAdapter(adapter);
 *		...
 * }
 *
 */
// Note: This object watches claim.getExpenses()
// If this changes, functionality here must be updated.
public class ExpenseItemAdapter extends BaseAdapter implements ListAdapter {
	private ExpenseClaim claim;
	private Context context;

	/**
	 * @param claim Claim to fetch expenses from
	 * @param context activity context
	 */
	public ExpenseItemAdapter(ExpenseClaim claim, Context context) {
		this.claim = claim;
		this.context = context;
	}

	@Override
	public int getCount() {
		return claim.getExpenses().size();
	}

	@Override
	public ExpenseItem getItem(int position) {
		return claim.getExpenses().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Are we recycling an existing view?
		if (convertView == null) {
			// No.
			// Inflate a new one and store it in convertView
			convertView = LayoutInflater.from(context).inflate(R.layout.expense_list_item, parent, false);
		} else {
			// Yes.
			// Recycle the existing view
		}
		
		// Fetch the text views that we're going to fill
		TextView expenseItemName = (TextView) convertView.findViewById(R.id.expenseItemName);
		TextView expenseItemValue = (TextView) convertView.findViewById(R.id.expenseItemValue);
		TextView expenseItemDate = (TextView) convertView.findViewById(R.id.expenseItemDate);
		TextView expenseItemCategory = (TextView) convertView.findViewById(R.id.expenseItemCategory);
		
		// Get the expense item context for this view
		ExpenseItem expense = getItem(position);
		
		// Fill in the values
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
		expenseItemName.setText(expense.getName().toString());
		expenseItemValue.setText(expense.getValueString().toString());
		expenseItemDate.setText(df.format(expense.getDate()));
		expenseItemCategory.setText(expense.getCategory().toString());

		// Return the converted view
		return convertView;
	}

}
