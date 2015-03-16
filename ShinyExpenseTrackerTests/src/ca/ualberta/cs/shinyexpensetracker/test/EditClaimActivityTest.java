package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.Application;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.activities.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Test for editing an ExpenseClaim
 * @author Sarah Morris
 */
public class EditClaimActivityTest extends
		ActivityInstrumentationTestCase2<AddExpenseClaimActivity> {

	public EditClaimActivityTest(Class<AddExpenseClaimActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public EditClaimActivityTest() {
		super(AddExpenseClaimActivity.class);
	}

	Instrumentation instrumentation;
	AddExpenseClaimActivity activity;
	DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	EditText startDate, endDate, name;
	Button doneButton;
	String claimName = "test claim";
	SimpleDateFormat dateFormatter;
	Date claimStartDate = new Date(1000);
	Date claimEndDate = new Date(2000);

	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimController controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);

		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);
		dateFormatter.format(claimStartDate);
		dateFormatter.format(claimEndDate);
		ExpenseClaim claim = new ExpenseClaim(claimName, claimStartDate,
				claimEndDate);
		controller.addExpenseClaim(claim);

		instrumentation = getInstrumentation();

		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);

		activity = getActivity();
	}

	public void testDisplayExpenseClaim() {

		EditText name = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextExpenseClaimName));
		EditText startDate = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextStartDate));
		EditText endDate = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.editTextEndDate));

		assertEquals("Claim name set incorrectly", claimName, name.getText()
				.toString());
		assertEquals("claim start date set incorrectly",
				dateFormatter.format(claimStartDate), startDate.getText()
						.toString());
		assertEquals("claim end date set incorrectly",
				dateFormatter.format(claimEndDate), endDate.getText()
						.toString());
	}

}
