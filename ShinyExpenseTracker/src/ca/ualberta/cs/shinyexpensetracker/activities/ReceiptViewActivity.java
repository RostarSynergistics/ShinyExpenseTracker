package ca.ualberta.cs.shinyexpensetracker.activities;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.utilities.InAppHelpDialog;

/**
 * View receipt of an Expense Item. Access through view expense item details by
 * clicking on the receipt thumbnail.
 * 
 * Covers Issue 30
 * 
 * @version 1.0
 * @since 2015-03-16
 */
public class ReceiptViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_view);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			UUID claimID = (UUID) intent.getSerializableExtra(IntentExtraIDs.CLAIM_ID);
			UUID expenseItemID = (UUID) intent.getSerializableExtra(IntentExtraIDs.EXPENSE_ITEM_ID);
			ExpenseClaimController controller = Application.getExpenseClaimController();
			ExpenseClaim claim = controller.getExpenseClaimByID(claimID);
			ExpenseItem item = claim.getExpenseItemByID(expenseItemID);
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
		if (id == R.id.action_help) {
			InAppHelpDialog.showHelp(this, R.string.help_receipt_view);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
