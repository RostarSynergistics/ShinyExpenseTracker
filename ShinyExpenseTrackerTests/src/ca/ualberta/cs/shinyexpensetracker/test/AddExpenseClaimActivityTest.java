package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.text.format.DateFormat;
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
	
	public AddExpenseClaimActivityTest(Class<AddExpenseClaimActivity> activityClass) {
		super(activityClass);
	}
	
	Instrumentation instrumentation;
	Activity activity;
	DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	EditText startDate, endDate, name;
	
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
        activity = (AddExpenseClaimActivity) getActivity();
    	
    	DatePickerDialog datePicker = new DatePickerDialog(instrumentation.getContext(), dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);
    	name = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextExpenseClaimName));
    	startDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextStartDate));
    	endDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextEndDate));
    }
    
	public void teststartDate() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				startDate.performClick();
				
				assertTrue("startDate dialog is working", ((AddExpenseClaimActivity) activity).getStartDateDialog().isShowing());
			}
		});
	}
	
	public void testendDate() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				endDate.performClick();
				
				assertTrue("endDate dialog is working", ((AddExpenseClaimActivity) activity).getEndDateDialog().isShowing());
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
	
	public void testExpenseClaim() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				String name = "URoma Trip";
				@SuppressWarnings("deprecation")
				Date startDate = new Date(2015, 01, 01);
				@SuppressWarnings("deprecation")
				Date endDate = new Date(2015, 01, 10);
				Status status = null;
				Tag tag = null;
				ExpenseClaim expenseClaim = new ExpenseClaim(name, startDate, endDate, status, tag);
				ExpenseClaimList claimList = new ExpenseClaimList();
				claimList.addClaim(expenseClaim);
				
				assertTrue("name is not 'URoma Trip'", expenseClaim.getName().toString() == "URoma Trip");
				assertTrue("startDate is not 'null'", expenseClaim.getStartDate().toString() == "2015-01-01");
				assertTrue("endDate is not 'null'", expenseClaim.getEndDate().toString() == "2015-01-10");
				assertTrue("Status is not 'null'", expenseClaim.getStatus() == null);
				assertTrue("Tag is not 'null'", expenseClaim.getTag() == null);
				
				assertTrue("ClaimList is empty", claimList.size() == 1);
			}
		});
	}
	

}
