/**
 *  Test case for Issue 17
 *  AddExpenseClaimActivityTest: Testing the AddExpenseClaimAcitivity representing the UI for adding/editing an Expense Claim. 
 *  No outstanding issues.
 * 
 **/

package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.activities.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

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

        Application.setExpenseClaimController(new ExpenseClaimController(new MockExpenseClaimListPersister()));

        instrumentation = getInstrumentation();
        activity = getActivity();
    	
    	DatePickerDialog datePicker = new DatePickerDialog(instrumentation.getContext(), dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);
    	name = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextExpenseClaimName));
    	startDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextStartDate));
    	endDate = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextEndDate));
    	doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addExpenseClaimDoneButton);
    }
    
	public void teststartDate() {
		assertFalse(((AddExpenseClaimActivity) activity).getStartDateDialog().isShowing());
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				startDate.performClick();
			}
		});
		assertTrue(((AddExpenseClaimActivity) activity).getStartDateDialog().isShowing());
	}
	
	public void testendDate() {
		assertFalse(((AddExpenseClaimActivity) activity).getEndDateDialog().isShowing());
		instrumentation.runOnMainSync(new Runnable() {
			public void run(){
				endDate.performClick();
			}
		});
		assertTrue(((AddExpenseClaimActivity) activity).getEndDateDialog().isShowing());
	}
	
	public void testText() {
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				name.setText("URoma");
				startDate.setText("01-04-2015");
				endDate.setText("08-04-2015");
			}
		});
		
		assertEquals("URoma", name.getText().toString());
		assertEquals("01-04-2015", startDate.getText().toString());
		assertEquals("08-04-2015", endDate.getText().toString());
	}
	
	public void testAddExpenseClaim() {
		
		final String nameString = "URoma";
		SimpleDateFormat sdf = new SimpleDateFormat();
		final Date toDate = new Date();
		final Date fromDate = new Date();
		sdf.format(fromDate);
		sdf.format(toDate);
		
		final ExpenseClaim sampleExpenseClaim = new ExpenseClaim(nameString, fromDate, toDate, null, null);
		final ExpenseClaimList claimList = Application.getExpenseClaimController().getExpenseClaimList();
		
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				claimList.addClaim(sampleExpenseClaim);	
			}
		});
		
		assertEquals(1, claimList.size());
		
		assertEquals("name != name", "URoma", sampleExpenseClaim.getName());
		assertNotSame("false positive, name", "Wrong Name", sampleExpenseClaim.getName());
		
		assertEquals("fromDate != fromDate", fromDate, sampleExpenseClaim.getStartDate());
		assertNotSame("false positive, startDate", "Wrong startDate", sampleExpenseClaim.getStartDate());
		
		assertEquals("endDate != endDate", toDate, sampleExpenseClaim.getEndDate());
		assertNotSame("false positive, endDate", "Wrong endDate", sampleExpenseClaim.getEndDate());
}
	
	public void testDoneButton() throws ParseException {
		
		ActivityMonitor monitor = instrumentation.addMonitor(TabbedSummaryActivity.class.getName(), null, false);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				startDate.setText("01-01-2015");
				endDate.setText("01-10-2015");
				name.setText("URoma");
	
				doneButton.performClick();
				
			}
		});
		instrumentation.waitForIdleSync();
		
		TabbedSummaryActivity nextActivity = (TabbedSummaryActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date startDateObject = sdf.parse(startDate.getText().toString());
		Date endDateObject = sdf.parse(endDate.getText().toString());
		
		ExpenseClaimController controller = Application.getExpenseClaimController();
		
		assertEquals("The two names do not equal each other", "URoma", controller.getExpenseClaim(0).getName());
		
		assertEquals("The two startDates do not equal each other", startDateObject, controller.getExpenseClaim(0).getStartDate());
		
		assertEquals("The two endDates do not equal each other", endDateObject, controller.getExpenseClaim(0).getEndDate());
		
		nextActivity.finish();
	}
	

}
