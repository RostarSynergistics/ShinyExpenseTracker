package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests various parts of the functionality of ExpenseClaimActivity that relates
 * to creating new ExpenseClaims.
 **/
public class AddExpenseClaimTests extends ActivityInstrumentationTestCase2<ExpenseClaimActivity> {
	ExpenseClaimController	controller;
	Instrumentation			instrumentation;
	ExpenseClaimActivity	activity;

	DatePickerDialog		fromDatePickerDialog, toDatePickerDialog;
	EditText				startDateField, endDateField, nameField;
	Button					doneButton;

	public AddExpenseClaimTests(Class<ExpenseClaimActivity> activityClass) {
		super(activityClass);
	}

	public AddExpenseClaimTests() {
		super(ExpenseClaimActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

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
		assertFalse(activity.getStartDateDialog().isShowing());

		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				startDateField.performClick();
			}
		});
		
		instrumentation.waitForIdleSync();

		assertTrue(activity.getStartDateDialog().isShowing());
	}

	public void testThatTappingOnEndDateOpensDateDialog() {
		assertFalse(activity.getEndDateDialog().isShowing());

		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				endDateField.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertTrue(activity.getEndDateDialog().isShowing());
	}

	public void testThatInputtingAnEndDateThatIsBeforeTheStartDateShowsAnAlert() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				startDateField.setText("01-10-2015");
				endDateField.setText("01-01-2015");
				nameField.setText("URoma Trip");
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertTrue("Date Range error AlertDialog is not showing", activity.getAlertDialog().isShowing());
	}

	public void testThatInputtingAnEndDateThatIsAfterTheStartDateIsValid() {
		ActivityMonitor monitor = instrumentation.addMonitor(TabbedSummaryActivity.class.getName(), null, false);

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				startDateField.setText("01-01-2015");
				endDateField.setText("01-10-2015");
				nameField.setText("URoma Trip");
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();
		TabbedSummaryActivity nextActivity = (TabbedSummaryActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);

		nextActivity.finish();
	}

	@SuppressLint("SimpleDateFormat")
	public void testThatTappingDoneWhileCreatingNewExpenseClaimCreatesANewExpenseClaim() throws ParseException {
		ActivityMonitor monitor = instrumentation.addMonitor(TabbedSummaryActivity.class.getName(), null, false);

		final String claimName = "URoma";
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				startDateField.setText("01-01-2015");
				endDateField.setText("01-10-2015");
				nameField.setText(claimName);

				doneButton.performClick();

			}
		});

		instrumentation.waitForIdleSync();

		TabbedSummaryActivity nextActivity = (TabbedSummaryActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date startDateObject = sdf.parse(startDateField.getText().toString());
		Date endDateObject = sdf.parse(endDateField.getText().toString());

		ExpenseClaim claim = controller.getExpenseClaim(0);

		assertEquals("The two names do not equal each other", claimName, claim.getName());

		assertEquals("The two startDates do not equal each other", startDateObject, claim.getStartDate());

		assertEquals("The two endDates do not equal each other", endDateObject, claim.getEndDate());

		nextActivity.finish();
	}
}
