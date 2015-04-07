package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.ParseException;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryClaimantActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.User.Type;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.utilities.GlobalDateFormat;

/**
 * Tests various parts of the functionality of ExpenseClaimActivity that relates
 * to creating new ExpenseClaims.
 **/
public class AddExpenseClaimTests extends ActivityInstrumentationTestCase2<ExpenseClaimActivity> {
	ExpenseClaimController controller;
	Instrumentation instrumentation;
	ExpenseClaimActivity activity;

	DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
	EditText startDateField, endDateField, nameField;
	Button doneButton;

	public AddExpenseClaimTests(Class<ExpenseClaimActivity> activityClass) {
		super(activityClass);
	}

	public AddExpenseClaimTests() {
		super(ExpenseClaimActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Application.setUserType(Type.Claimant);

		controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);

		instrumentation = getInstrumentation();
		activity = getActivity();

		nameField = (EditText) activity.findViewById(R.id.editTextExpenseClaimName);
		startDateField = (EditText) activity.findViewById(R.id.editTextStartDate);
		endDateField = (EditText) activity.findViewById(R.id.editTextEndDate);
		doneButton = (Button) activity.findViewById(R.id.addExpenseClaimDoneButton);
	}

	public void testThatTappingOnStartDateOpensDateDialog() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				startDateField.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertNotNull(activity.getStartDatePickerDialog());
		assertTrue(activity.getStartDatePickerDialog().isShowing());
	}

	public void testThatTappingOnEndDateOpensDateDialog() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				endDateField.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertNotNull(activity.getEndDatePickerDialog());
		assertTrue(activity.getEndDatePickerDialog().isShowing());
	}

	public void testThatInputtingAnEndDateThatIsBeforeTheStartDateShowsAnAlert() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				startDateField.setText(GlobalDateFormat.makeString(2015, 10, 01));
				endDateField.setText(GlobalDateFormat.makeString(2015, 1, 1));
				nameField.setText("URoma Trip");
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertNotNull("Date Range error AlertDialog is not showing", activity.getAlertDialog());
	}

	public void testThatInputtingAnEndDateThatIsAfterTheStartDateIsValid() {

		ActivityMonitor monitor = instrumentation
													.addMonitor(TabbedSummaryClaimantActivity.class.getName(),
															null,
															false);

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				startDateField.setText(GlobalDateFormat.makeString(2015, 1,1));
				endDateField.setText(GlobalDateFormat.makeString(2015, 1, 10));
				nameField.setText("URoma Trip");
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();
		TabbedSummaryActivity nextActivity = (TabbedSummaryClaimantActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);

		nextActivity.finish();
	}

	public void testThatTappingDoneWhileCreatingNewExpenseClaimCreatesANewExpenseClaim() throws ParseException {
		ActivityMonitor monitor = instrumentation
													.addMonitor(TabbedSummaryClaimantActivity.class.getName(),
															null,
															false);

		final String claimName = "URoma";
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				startDateField.setText(GlobalDateFormat.makeString(2015, 1,1));
				endDateField.setText(GlobalDateFormat.makeString(2015, 1, 10));
				nameField.setText(claimName);

				doneButton.performClick();

			}
		});

		instrumentation.waitForIdleSync();

		TabbedSummaryActivity nextActivity = (TabbedSummaryClaimantActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);

		Date startDateObject = GlobalDateFormat.parse(startDateField.getText().toString());
		Date endDateObject = GlobalDateFormat.parse(endDateField.getText().toString());

		ExpenseClaim claim = controller.getExpenseClaimAtPosition(0);

		assertEquals("The two names do not equal each other", claimName, claim.getName());

		assertEquals("The two startDates do not equal each other", startDateObject, claim.getStartDate());

		assertEquals("The two endDates do not equal each other", endDateObject, claim.getEndDate());

		nextActivity.finish();
	}
}
