/**
 * Activity that lets the user view 
 * information of the selected ExpenseItem
 * 
 * Covers Issue 16
 * @author Oleg Oleynikov 
 * @version 1.0
 * @since 2015-03-15
 */

package ca.ualberta.cs.shinyexpensetracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimsRepository;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ExpenseItemDetailView extends Activity {

	private ExpenseItem item;
	private Context context = ExpenseItemDetailView.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_detail_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null){
			int claimId = (Integer) bundle.get("Claim ID");
			int expenseItemId = (Integer) bundle.get("Item ID");
			ExpenseClaimController controller = ExpenseClaimController.getInstance();
			ExpenseClaim claim = controller.getExpenseClaim(claimId);
			item = claim.getItemById(expenseItemId);
			populateTextViews();
		}
		// this one is just for testing purposes
		// because I can't figure out how to send an Intent from a test
		/*else{
			ExpenseClaimController controller = ExpenseClaimController.getInstance();
			ExpenseClaim claim = controller.getExpenseClaim(0);
			item = claim.getItemById(0);
			populateTextViews();
		}*/
		
	}

	private void populateTextViews(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
		
		TextView name = (TextView) findViewById(R.id.expenseItemNameValue);
		TextView date = (TextView) findViewById(R.id.expenseItemDateValue);
		TextView category = (TextView) findViewById(R.id.expenseItemCategoryValue);
		TextView description = (TextView) findViewById(R.id.expenseItemDescriptionValue);
		TextView amount = (TextView) findViewById(R.id.expenseItemAmountValue);
		TextView currency = (TextView) findViewById(R.id.expenseItemCurrencyValue);
		TextView receipt = (TextView) findViewById(R.id.expenseItemReceiptValue);
		TextView completeness = (TextView) findViewById(R.id.expenseItemCompletenessValue);
		
		name.setText(item.getName().toString());
		date.setText(dateFormatter.format(item.getDate()));
		category.setText(item.getCategory().getText());
		description.setText(item.getDescription().toString());
		amount.setText(item.getAmountSpent().toString());
		currency.setText(item.getCurrency().name());
		if (item.doesHavePhoto()){
			receipt.setText("Present");
		}
		else{
			receipt.setText("Not Present");
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
	
	public void pressBack(View v)
	{
		finish();
	}

	public Context getContext() {
		return context;
	}
}
