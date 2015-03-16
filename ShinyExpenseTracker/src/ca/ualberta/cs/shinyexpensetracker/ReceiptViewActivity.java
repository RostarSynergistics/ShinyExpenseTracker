package ca.ualberta.cs.shinyexpensetracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
/**
 * Covers Issue 30
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-16
 * View receipt of a referred Expense Item, if there is any 
 */
public class ReceiptViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null) {
			// if there is no index information sent, put the first item of first claim
			int claimId = intent.getIntExtra("itemIndex", 0);
			int expenseItemId = intent.getIntExtra("expenseIndex", 0);
			ExpenseClaimController controller = Application.getExpenseClaimController();
			ExpenseClaim claim = controller.getExpenseClaim(claimId);
			ExpenseItem item = claim.getItemById(expenseItemId);
			ImageView receiptView = (ImageView) findViewById(R.id.receiptImageView);
			Bitmap receiptImage = item.getReceiptPhoto();
			receiptView.setImageBitmap(receiptImage);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receipt_view, menu);
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
}
