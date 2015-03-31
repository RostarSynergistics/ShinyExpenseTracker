/**
 *  Covers issue 17. 
 *  AddExpenseClaimActivity: Activity representing the UI for adding/editing an Expense Claim. 
 *  No outstanding issues.
 **/

package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

// Source for DatePicker: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event

public class AddExpenseClaimActivity extends Activity implements
		OnClickListener {

	private ExpenseClaimController controller;

	private EditText startDate, endDate;
	private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	private SimpleDateFormat dateFormatter;
	private AlertDialog.Builder adb;
	public Dialog alertDialog;
	private ExpenseClaim claim;
	private int claimIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense_claim);

		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
		adb = new AlertDialog.Builder(this);

		findViewsById();

		setDateTimeField();

		controller = Application.getExpenseClaimController();

		Intent intent = getIntent();
		claimIndex = intent.getIntExtra("claimIndex", -1);
		if (claimIndex == -1) {
			claim = new ExpenseClaim("");
		} else {
			claim = controller.getExpenseClaim(claimIndex);
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

		fromDatePickerDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						startDate.setText(dateFormatter.format(newDate
								.getTime()));
					}
				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		toDatePickerDialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						endDate.setText(dateFormatter.format(newDate.getTime()));
					}
				}, newCalendar.get(Calendar.YEAR), newCalendar
						.get(Calendar.MONTH), newCalendar
						.get(Calendar.DAY_OF_MONTH));
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
			adb.setMessage("Expense Claim requires a name");
			adb.setCancelable(true);

			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog = adb.create();
			alertDialog.show();
			return null;
		} else {
			name = nameText.getText().toString();
		}

		Date startDate = new Date();
		if (startDateText.getText().length() == 0) {
			adb.setMessage("Expense Claim requires a start date");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog = adb.create();
			alertDialog.show();
			return null;
		} else {
			startDate = dateFormatter.parse(startDateText.getText().toString());
		}

		Date endDate = new Date();
		if (endDateText.getText().length() == 0) {
			adb.setMessage("Expense Claim requires an end date");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog = adb.create();
			alertDialog.show();
			return null;
		} else {
			endDate = dateFormatter.parse(endDateText.getText().toString());
		}

		if (startDate.after(endDate)) {
			adb.setMessage("Invalid range of dates");
			adb.setMessage("Start Date cannot be set to after the End Date");
			adb.setCancelable(true);
			adb.setNeutralButton("Back", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog = adb.create();
			alertDialog.show();
			return null;
		}

		claim.setName(name);
		claim.setStartDate(startDate);
		claim.setEndDate(endDate);

		if (claimIndex == -1) {
			try {
				controller.addExpenseClaim(claim);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Sanity check
		if (controller.getIndexOf(claim) == -1) {
			Log.wtf("Add Claim", "Claim isn't in the claim list?");
			throw new RuntimeException();
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
			if (claimIndex == -1) {
				Intent intent = new Intent(this, TabbedSummaryActivity.class);
				intent.putExtra("claimIndex", controller.getIndexOf(claim));
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