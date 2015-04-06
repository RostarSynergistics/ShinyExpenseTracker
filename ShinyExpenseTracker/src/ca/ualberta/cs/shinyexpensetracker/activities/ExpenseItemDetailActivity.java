package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Activity that lets the user view information of the selected ExpenseItem Used
 * when an expense item is selected from the ExpenseItemListFragment Covers
 * Issue 16
 * 
 * @version 1.0
 * @since 2015-03-15
 */

public class ExpenseItemDetailActivity extends Activity implements IView<ExpenseItem> {

	private ExpenseItem item;
	private int claimIndex;
	private int expenseItemIndex;
	private ExpenseClaimController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_detail_view);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			claimIndex = intent.getIntExtra("claimIndex", -1);
			expenseItemIndex = intent.getIntExtra("expenseIndex", -1);
			controller = Application.getExpenseClaimController();
			ExpenseClaim claim = controller.getExpenseClaim(claimIndex);
			// Fetch the relevant item
			item = claim.getExpense(expenseItemIndex);

			item.addView(this);
		} else {
			Log.wtf("Activity", "ExpenseItem Detail View - Did not receive an intent");
			throw new RuntimeException();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Update the text views
		Log.d("ExpenseItemDetailView", "Resuming activity.");
		populateViews();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("ExpenseItemDetailView", "Pausing activity.");
	}

	/**
	 * Sets the value of a textView
	 * 
	 * @param textViewId
	 *            Id of the text view
	 * @param value
	 *            string to put in the textView
	 */
	private void setTextViewValue(int textViewId, String value) {
		TextView tv = (TextView) findViewById(textViewId);
		tv.setText(value);
	}

	/**
	 * Sets expenseName, date, category, amount spent, currency, and the receipt
	 * value (has receipt or not) of the expenseItem being used
	 */
	private void populateViews() {
		Log.d("ExpenseItemDetailView", "Updating text views.");

		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);

		setTextViewValue(R.id.expenseItemNameValue, item.getName().toString());
		setTextViewValue(R.id.expenseItemDateValue, dateFormatter.format(item.getDate()));
		setTextViewValue(R.id.expenseItemCategoryValue, item.getCategory().toString());
		setTextViewValue(R.id.expenseItemDescriptionValue, item.getDescription().toString());
		setTextViewValue(R.id.expenseItemAmountValue, item.getValueString().toString());

		// Update the image button picture
		ImageButton img = (ImageButton) findViewById(R.id.expenseItemDetailImageButton);
		img.setImageBitmap(item.getReceiptPhoto());

		// Update the incompleteness indicator
		CheckBox flag = (CheckBox) findViewById(R.id.expenseItemCompletenessFlag);
		flag.setChecked(item.getIsMarkedIncomplete());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_item_detail_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.editExpenseItem:
			editExpense();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Opens the activity responsible for editing a claim
	 * 
	 * @param position
	 *            the position in the listview to edit.
	 */
	public void editExpense() {
		// Create an intent to edit an expense item
		Intent intent = new Intent(this, ExpenseItemActivity.class);
		// --> Tell it that we're editing the index at this position
		intent.putExtra("claimIndex", claimIndex);
		intent.putExtra("expenseIndex", expenseItemIndex);

		// Start the activity with our edit intent
		startActivity(intent);
	}

	@Override
	public void update(ExpenseItem m) {
		// If the claim changed, update the text views
		Log.d("ExpenseItemDetailView", "Received update.");
		populateViews();
	}

	/**
	 * Allows the user to click on the thumbnail of the recipt. Will pass on the
	 * claimIndex and expenseItemIndex to the ReceiptViewActivity
	 * 
	 * @param v
	 */
	public void onReceiptThumbnailClick(View v) {
		ImageView iv = (ImageView) findViewById(R.id.expenseItemDetailImageButton);
		if (iv.getDrawable() == null) {
			Toast.makeText(this, "No receipt to display", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(ExpenseItemDetailActivity.this, ReceiptViewActivity.class);
		intent.putExtra("claimIndex", claimIndex);
		intent.putExtra("expenseIndex", expenseItemIndex);
		startActivity(intent);
	}

	/**
	 * Remove receipt from item and thumbnail, update the fields
	 * 
	 * @param v
	 */
	public void onClickRemoveReceipt(View v) {
		item.setReceiptPhoto(null);
		ImageView iv = (ImageView) findViewById(R.id.expenseItemDetailImageButton);
		iv.setImageDrawable(null);
		try {
			controller.update();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Toggles the completeness flag for the item
	 * 
	 * @param v
	 *            CheckBox view that represents the flag
	 */
	public void onToggleCompletenessFlag(View v) {
		// Precondition: v is a CheckBox view.
		item.setIncompletenessMarker(((CheckBox) v).isChecked());
	}

}
