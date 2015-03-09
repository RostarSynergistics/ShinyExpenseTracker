/* Source for DatePicker: http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
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
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddExpenseClaimActivity extends Activity implements OnClickListener {
	
    private EditText startDate, endDate;
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
	
	public void createExpenseClaim(View v) throws ParseException {
		EditText name = (EditText) findViewById(R.id.editTextExpenseClaimName);
        EditText endDateText = (EditText) findViewById(R.id.editTextEndDate);
        EditText startDateText = (EditText) findViewById(R.id.editTextStartDate); 
        
        Date startDate = dateFormatter.parse(startDateText.getText().toString());
        Date endDate = dateFormatter.parse(endDateText.getText().toString());
        
        ExpenseClaim expenseClaim = new ExpenseClaim(name.getText().toString(), startDate, endDate, null, null);
	}

	// for tests
	public DatePickerDialog getStartDateDialog() {
		return fromDatePickerDialog;
	}
	
	public DatePickerDialog getEndDateDialog() {
		return toDatePickerDialog;  
	}
}
