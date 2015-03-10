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
 *  Test case for Issue 17
 *  AddExpenseClaimActivityTest: Testing the AddExpenseClaimAcitivity representing the UI for adding/editing an Expense Claim. 
 *  No outstanding issues.
 */

package ca.ualberta.cs.shinyexpensetracker.test;

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
	
	public AddExpenseClaimActivityTest(Class<AddExpenseClaimActivity> activityClass) {
		super(activityClass);
	}
	
	Instrumentation instrumentation;
	Activity activity;
	DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	EditText startDate, endDate, name;
	Button doneButton;
	
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
    	doneButton = ((Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addExpenseClaimDoneButton));
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
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				String claimName = "URoma Trip";
				@SuppressWarnings("deprecation")
				Date startDateString = new Date(2015, 01, 01);
				@SuppressWarnings("deprecation")
				Date endDateString = new Date(2015, 01, 10);
				Status status = null;
				Tag tag = null;
				ExpenseClaim expenseClaim = new ExpenseClaim(claimName, startDateString, endDateString, status, tag);
				ExpenseClaimList claimList = new ExpenseClaimList();
				assertEquals(0, claimList.size() == 0);
				doneButton.performClick();
				
				assertTrue("name is not 'URoma Trip'", expenseClaim.getName().toString() == "URoma Trip");
				assertTrue("startDate is not 'null'", expenseClaim.getStartDate().toString() == "2015-01-01");
				assertTrue("endDate is not 'null'", expenseClaim.getEndDate().toString() == "2015-01-10");
				assertTrue("Status is not 'null'", expenseClaim.getStatus() == null);
				assertTrue("Tag is not 'null'", expenseClaim.getTag() == null);
				assertTrue("ClaimList is empty", claimList.size() == 1);
				assertEquals(1, claimList.size() == 1);	
			}
		});
	}
	

}
