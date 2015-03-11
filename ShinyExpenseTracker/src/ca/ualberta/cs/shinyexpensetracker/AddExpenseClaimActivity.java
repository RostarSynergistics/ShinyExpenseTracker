/*
 *	Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Covers issue 17. 
 *  AddExpenseClaimActivity: Activity representing the UI for adding/editing an Expense Claim. 
 *  No outstanding issues.
 */

package ca.ualberta.cs.shinyexpensetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

// Source for DatePicker: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event

public class AddExpenseClaimActivity extends Activity implements OnClickListener {
	
    private EditText startDate, endDate;
    private AlertDialog.Builder adb;
    public Dialog alertDialog;
    private DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense_claim);
		
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        
        findViewsById();
        
        setDateTimeField();
	}
	
    private void findViewsById() {
        startDate = (EditText) findViewById(R.id.editTextStartDate);    
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();
        
        endDate = (EditText) findViewById(R.id.editTextEndDate);
        endDate.setInputType(InputType.TYPE_NULL);
    }
    
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
 
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        
        toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
 
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }
 
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    
	@Override
	public void onClick(View view) {
		if(view == startDate) {
			fromDatePickerDialog.show();
		} else if(view == endDate) {
			toDatePickerDialog.show();
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_expense_claim, menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean createExpenseClaim(View v) throws ParseException {
		EditText nameText = (EditText) findViewById(R.id.editTextExpenseClaimName);
        EditText endDateText = (EditText) findViewById(R.id.editTextEndDate);
        EditText startDateText = (EditText) findViewById(R.id.editTextStartDate); 
        
        
        String name = "";
    	if (nameText.getText().length() == 0){
    		//display dialog if no name entered
    		adb.setMessage("Expense Claim requires a name");
    		adb.setCancelable(true);
    		adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) { }
    		});
    		alertDialog = adb.create();
    		alertDialog.show();
    		return false;
    		} else {
    		name = nameText.getText().toString();
    		}
        
        
        Date startDate = new Date();
    	if (startDateText.getText().length() == 0) {
    		//display dialog if no start date is entered
    		adb.setMessage("Expense Claim requires a start date");
    		adb.setCancelable(true);
    		adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) { }
    		});
    		alertDialog = adb.create();
    		alertDialog.show();
    		return false;
    		} else {
    		startDate = dateFormatter.parse(startDateText.getText().toString());
    		}
        
        Date endDate = dateFormatter.parse(endDateText.getText().toString());
    	if (endDateText.getText().length() == 0) {
    		//display dialog if no end date is entered
    		adb.setMessage("Expense Claim requires an end date");
    		adb.setCancelable(true);
    		adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) { }
    		});
    		alertDialog = adb.create();
    		alertDialog.show();
    		return false;
    		} else {
    		endDate = dateFormatter.parse(endDateText.getText().toString());
    		}
        
        ExpenseClaim expenseClaim = new ExpenseClaim(name, startDate, endDate, null, null);
        ExpenseClaimList claimList = ExpenseClaimController.getExpenseClaimList();
        claimList.addClaim(expenseClaim);
		return true;
	}
	
	public void doneExpenseItem(View v) throws ParseException{
		if (createExpenseClaim(v)){
			finish();
		}
	}
	
	// for tests
	public DatePickerDialog getStartDateDialog() {
		return fromDatePickerDialog;
	}
	
	public DatePickerDialog getEndDateDialog() {
		return toDatePickerDialog;  
	}
}
