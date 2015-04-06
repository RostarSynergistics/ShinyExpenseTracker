package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.ValidationErrorAlertDialog;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * Activity that handles the creation of new ExpenseClaims and the editing of
 * existing ExpenseClaims.
 * 
 * Source for DatePicker:
 * http://androidopentutorials.com/android-datepickerdialog
 * -on-edittext-click-event
 */
public class ExpenseClaimActivity extends Activity implements OnClickListener {
	private ExpenseClaimController controller;

	private EditText startDate, endDate;
	private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	private SimpleDateFormat dateFormatter;
	public Dialog alertDialog;
	private ExpenseClaim claim;
	private UUID claimID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense_claim);

		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);

		findViewsById();

		setDateTimeField();

		controller = Application.getExpenseClaimController();

		Intent intent = getIntent();
		claimID = (UUID) intent.getSerializableExtra(IntentExtraIDs.CLAIM_ID);

		if (claimID != null) {
			claim = controller.getExpenseClaimByID(claimID);
			displayExpenseClaim(claim);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		controller = Application.getExpenseClaimController();
	}

	/**
	 * Sets all the views and widgets in on create
	 */
	private void findViewsById() {
		startDate = (EditText) findViewById(R.id.editTextStartDate);
		startDate.setInputType(InputType.TYPE_NULL);
		startDate.requestFocus();

		endDate = (EditText) findViewById(R.id.editTextEndDate);
		endDate.setInputType(InputType.TYPE_NULL);
	}

	/**
	 * Creates a date picker and sets it to the edit text views for picking
	 * dates
	 */
	private void setDateTimeField() {
		startDate.setOnClickListener(this);
		endDate.setOnClickListener(this);

		Calendar newCalendar = Calendar.getInstance();

		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				startDate.setText(dateFormatter.format(newDate.getTime()));
			}
		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				endDate.setText(dateFormatter.format(newDate.getTime()));
			}
		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_expense_claim, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		if (view == startDate) {
			fromDatePickerDialog.show();
		} else if (view == endDate) {
			toDatePickerDialog.show();
		}
	}

	/**
	 * gets name, endDate, and startDate values from their respective EditTexts
	 * handles exception in the case of invalid values for all three returns
	 * claim if all conditions are passed
	 * 
	 * @param v
	 * @return
	 * @throws ParseException
	 */
	public ExpenseClaim saveExpenseClaim(View v) throws ParseException {
		EditText nameText = (EditText) findViewById(R.id.editTextExpenseClaimName);
		EditText endDateText = (EditText) findViewById(R.id.editTextEndDate);
		EditText startDateText = (EditText) findViewById(R.id.editTextStartDate);

		String name = "";
		if (nameText.getText().length() == 0) {
			new ValidationErrorAlertDialog(this, "Expense Claim requires a name.").show();
			return null;
		} else {
			name = nameText.getText().toString();
		}

		Date startDate = new Date();
		if (startDateText.getText().length() == 0) {
			new ValidationErrorAlertDialog(this, "Expense Claim requires a start date.").show();
			return null;
		} else {
			startDate = dateFormatter.parse(startDateText.getText().toString());
		}

		Date endDate = new Date();
		if (endDateText.getText().length() == 0) {
			new ValidationErrorAlertDialog(this, "Expense Claim requires a end date.").show();
			return null;
		} else {
			endDate = dateFormatter.parse(endDateText.getText().toString());
		}

		if (startDate.after(endDate)) {
			new ValidationErrorAlertDialog(this, "Invalid range of dates.",
					"Start Date cannot be set to after the End Date.").show();
			return null;
		}

		try {
			if (claimID == null) {
				claim = controller.addExpenseClaim(name, startDate, endDate);
			} else {
				claim = controller.updateExpenseClaim(claim.getID(), name, startDate, endDate);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return claim;
	}

	/**
	 * presets EditText with already existing claim for editing
	 * 
	 * @param claim
	 */

	public void displayExpenseClaim(ExpenseClaim claim) {
		EditText claimName = (EditText) findViewById(R.id.editTextExpenseClaimName);

		claimName.setText(claim.getName().toString());
		startDate.setText(dateFormatter.format(claim.getStartDate()));
		endDate.setText(dateFormatter.format(claim.getEndDate()));
	}

	/**
	 * returns finish() method on successful added/edited claim
	 * 
	 * @param v
	 * @throws ParseException
	 */

	public void doneExpenseItem(View v) throws ParseException {
		ExpenseClaim claim = saveExpenseClaim(v);
		if (claim != null) {
			if (claimID == null) {
				Intent intent = new Intent(this, TabbedSummaryClaimantActivity.class);
				intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
				finish();
				startActivity(intent);
			} else {
				finish();
			}
		}
	}

	/**
	 * for handling test arguments. See ExpenseItemActivityTest
	 * 
	 * @return fromDatePickerDialog
	 */
	public DatePickerDialog getStartDateDialog() {
		return fromDatePickerDialog;
	}

	/**
	 * for handling test arguments. See ExpenseItemActivityTest
	 * 
	 * @return toDatePickerDialog
	 */

	public DatePickerDialog getEndDateDialog() {
		return toDatePickerDialog;
	}

	/**
	 * for handling test arguments. See ExpenseItemActivityTest
	 * 
	 * @return alertDialog;
	 */
	public Dialog getAlertDialog() {
		return alertDialog;
	}
}
