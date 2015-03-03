package ca.ualberta.cs.shinyexpensetracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ExpenseItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_expense_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_item, menu);
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
	
	public void createExpenseItem(View v) {
		EditText nameText = (EditText) findViewById(R.id.nameEditText);
		EditText dateText = (EditText) findViewById(R.id.dateEditText);
		Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		EditText amountText = (EditText) findViewById(R.id.amountEditText);
		Spinner currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
		EditText descriptionText = (EditText) findViewById(R.id.descriptionEditText);
		ImageButton reciptPhoto = (ImageButton) findViewById(R.id.reciptImageButton);
		
		/*ExpenseItem expense = new ExpenseItem(nameText.getText().toString(),
				dateText.getText().toString(), 
				categorySpinner.getSelectedItem().toString(),
				Double.parseDouble(amountText.getText().toString()),
				currencySpinner.getSelectedItem().toString(),
				descriptionText.getText().toString(),
				reciptPhoto.getDrawingCache()); */
	}
}
