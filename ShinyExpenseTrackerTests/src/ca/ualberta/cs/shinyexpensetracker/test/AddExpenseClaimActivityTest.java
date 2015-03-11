/**
 *  Test case for Issue 17
 *  AddExpenseClaimActivityTest: Testing the AddExpenseClaimAcitivity representing the UI for adding/editing an Expense Claim. 
 *  No outstanding issues.
 * 
 **/

package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;

public class AddExpenseClaimActivityTest extends ActivityInstrumentationTestCase2<AddExpenseClaimActivity> {
	
    private static final int TARGET_YEAR = 2008;
    private static final int TARGET_MONTH = 11;
    private static final int TARGET_DAY = 7;
    
	Instrumentation instrumentation;
	AddExpenseClaimActivity activity;
	DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	EditText startDate, endDate, name;
	Button doneButton;
	
	
	public AddExpenseClaimActivityTest(Class<AddExpenseClaimActivity> activityClass) {
		super(activityClass);
	}
	
	public AddExpenseClaimActivityTest() {
		super(AddExpenseClaimActivity.class);
	}
	
    private OnDateSetListener dateListener = new OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	int yr = year;
            int month = monthOfYear;
            int day = dayOfMonth;
        }
    };
    
    protected void setUp() throws Exception {
    	super.setUp();
        instrumentation = getInstrumentation();
        activity = getActivity();
    	
    	DatePickerDialog datePicker = new DatePickerDialog(instrumentation.getContext(), dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);
    	name = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextExpenseClaimName));
    	startDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextStartDate));
    	endDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextEndDate));
    	doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addExpenseClaimDoneButton);
    }
    
	public void teststartDate() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				assertFalse(((AddExpenseClaimActivity) activity).getStartDateDialog().isShowing());
				startDate.performClick();
				assertTrue(((AddExpenseClaimActivity) activity).getStartDateDialog().isShowing());
			}
		});
	}
	
	public void testendDate() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				assertFalse(((AddExpenseClaimActivity) activity).getEndDateDialog().isShowing());
				endDate.performClick();
				assertTrue(((AddExpenseClaimActivity) activity).getEndDateDialog().isShowing());
			}
		});
	}
	
	public void testText() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				name.setText("URoma Trip");
				startDate.setText("01-04-2015");
				endDate.setText("08-04-2015");
				
				assertEquals("URoma Trip", name.getText().toString());
				assertEquals("01-04-2015", startDate.getText().toString());
				assertEquals("08-04-2015", endDate.getText().toString());
			}
		});
	}
	
	public void testAddExpenseClaim() {
		
		String nameString = "URoma";
		SimpleDateFormat sdfFrom = new SimpleDateFormat();
		SimpleDateFormat sdfTo = new SimpleDateFormat();
		Date toDate = new Date();
		Date fromDate = new Date();
		sdfFrom.format(fromDate);
		sdfTo.format(fromDate);
		
		ExpenseClaim sampleExpenseClaim = new ExpenseClaim(nameString, fromDate, toDate, null, null);
		ExpenseClaimList claimList = new ExpenseClaimList();
		assertEquals(0, claimList.size());
		claimList.addClaim(sampleExpenseClaim);
		assertEquals(1, claimList.size());
		
		assertEquals("name != name", "URoma", sampleExpenseClaim.getName());
		assertNotSame("false positive, name", "Wrong Name", sampleExpenseClaim.getName());
		
		assertEquals("fromDate != fromDate", fromDate, sampleExpenseClaim.getStartDate());
		assertNotSame("false positive, startDate", "Wrong startDate", sampleExpenseClaim.getStartDate());
		
		assertEquals("endDate != endDate", toDate, sampleExpenseClaim.getEndDate());
		assertNotSame("false positive, endDate", "Wrong endDate", sampleExpenseClaim.getEndDate());
	}
	

}
