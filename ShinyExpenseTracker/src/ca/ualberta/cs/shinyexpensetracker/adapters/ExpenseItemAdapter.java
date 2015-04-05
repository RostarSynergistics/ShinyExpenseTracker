package ca.ualberta.cs.shinyexpensetracker.adapters;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Adapter for the list Views that display ExpenseItems. It fills the
 * expense_list_item.xml layout file to display Expense Items.
 * 
 * You can use this in place of an ArrayAdapter for ExpenseItems. This gives
 * better flexibility for the adapter.
 * 
 * Example usage: public class MyActivity extends Activity) {
 * 
 * public setupListView() { adapter = new Adapter( claimToWatch, this )
 * listview.setAdapter(adapter); ... }
 * 
 */
// Note: This object watches claim.getExpenses()
// If this changes, functionality here must be updated.
public class ExpenseItemAdapter extends BaseAdapter implements ListAdapter {
	private ExpenseClaim claim;
	private Context context;

	/**
	 * @param claim
	 *            Claim to fetch expenses from
	 * @param context
	 *            activity context
	 */
	public ExpenseItemAdapter(ExpenseClaim claim, Context context) {
		this.claim = claim;
		this.context = context;
	}

	@Override
	public int getCount() {
		return claim.getExpenseCount();
	}

	@Override
	public ExpenseItem getItem(int position) {
		return claim.getExpenseItemAtPosition(position);
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
		TextView expenseItemDescTextView = (TextView) convertView.findViewById(R.id.expenseItemDescription);
		ImageView expenseItemReceiptIndicator = (ImageView) convertView.findViewById(R.id.expenseItemReceiptIndicator);
		CheckBox expenseItemFlagCheckBox = (CheckBox) convertView.findViewById(R.id.expenseItemCompletenessFlag);

		// Get the expense item context for this view
		final ExpenseItem expense = getItem(position);

		// Handle the incompleteness flag here
		expenseItemFlagCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox ch = (CheckBox) v;
				expense.setIncompletenessMarker(ch.isChecked());
			}
		});

		expenseItemFlagCheckBox.setChecked(expense.getIsMarkedIncomplete());

		// Fill in the values
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);

		expenseItemName.setText(expense.getName().toString());
		expenseItemValue.setText(expense.getValueString().toString());
		expenseItemDate.setText(df.format(expense.getDate()));
		expenseItemCategory.setText(expense.getCategory().toString());
		expenseItemDescTextView.setText(expense.getDescription().toString());

		// Is expense marked incomplete?
		if (expense.hasAnAttachedReceipt()) {
			// Yes. Display indicator
			expenseItemReceiptIndicator.setImageResource(android.R.drawable.ic_menu_camera);
		} else {
			// No. Hide indicator
			expenseItemReceiptIndicator.setImageBitmap(null);
		}

		// Return the converted view
		return convertView;
	}

}
