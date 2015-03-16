package ca.ualberta.cs.shinyexpensetracker.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.R.id;
import ca.ualberta.cs.shinyexpensetracker.R.layout;
import ca.ualberta.cs.shinyexpensetracker.R.menu;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Activity that lets the user view 
 * information of the selected ExpenseItem
 * 
 * Covers Issue 16
 * @author Oleg Oleynikov 
 * @version 1.0
 * @since 2015-03-15
 */

public class ExpenseItemDetailActivity extends Activity {

	private ExpenseItem item;
	private Context context = ExpenseItemDetailActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_detail_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null) {
			int claimId = (Integer) bundle.get("claimIndex");
			int expenseItemId = (Integer) bundle.get("expenseIndex");
			ExpenseClaimController controller = Application.getExpenseClaimController();
			ExpenseClaim claim = controller.getExpenseClaim(claimId);
			item = claim.getItemById(expenseItemId);
			populateTextViews();
		}
		
	}

	private void setTextViewValue(int textViewId, String value){
		TextView tv = (TextView) findViewById(textViewId);
		tv.setText(value);
	}
	
	private void populateTextViews(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
		
		setTextViewValue(R.id.expenseItemNameValue, item.getName().toString());
		setTextViewValue(R.id.expenseItemDateValue, dateFormatter.format(item.getDate()));
		setTextViewValue(R.id.expenseItemCategoryValue, item.getCategory().toString());
		setTextViewValue(R.id.expenseItemDescriptionValue, item.getDescription().toString());
		setTextViewValue(R.id.expenseItemAmountValue, item.getAmountSpent().toString());
		setTextViewValue(R.id.expenseItemCurrencyValue, item.getCurrency().name());
		if (item.doesHavePhoto()){
			setTextViewValue(R.id.expenseItemReceiptValue,"Present");
		}
		else{
			setTextViewValue(R.id.expenseItemReceiptValue,"Not Present");
		}
		
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onBackButtonPress(View v)
	{
		finish();
	}

	public Context getContext() {
		return context;
	}
}
