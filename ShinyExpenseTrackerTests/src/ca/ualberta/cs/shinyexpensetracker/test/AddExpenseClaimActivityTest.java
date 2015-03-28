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
import ca.ualberta.cs.shinyexpensetracker.activities.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.R;

/**
 * AddExpenseClaimActivityTest: Testing the AddExpenseClaimAcitivity
 * representing the UI for adding/editing an Expense Claim.
 * 
 * No outstanding issues.
 * 
 **/
public class AddExpenseClaimActivityTest extends ActivityInstrumentationTestCase2<AddExpenseClaimActivity> {
	Instrumentation			instrumentation;
	AddExpenseClaimActivity	activity;
	DatePickerDialog		fromDatePickerDialog, toDatePickerDialog;
	EditText				startDate, endDate, name;
	Button					doneButton;
	ExpenseClaimController	controller;

	public AddExpenseClaimActivityTest(Class<AddExpenseClaimActivity> activityClass) {
		super(activityClass);
	}

	public AddExpenseClaimActivityTest() {
		super(AddExpenseClaimActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);

		instrumentation = getInstrumentation();
		activity = (AddExpenseClaimActivity) getActivity();

		name = (EditText) activity.findViewById(R.id.editTextExpenseClaimName);
		startDate = (EditText) activity.findViewById(R.id.editTextStartDate);
		endDate = (EditText) activity.findViewById(R.id.editTextEndDate);
		doneButton = (Button) activity.findViewById(R.id.addExpenseClaimDoneButton);
	}

	public void testThatTappingOnStartDateOpensDateDialog() {
		assertFalse(activity.getStartDateDialog().isShowing());

		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				startDate.performClick();
			}
		});

		assertTrue(activity.getStartDateDialog().isShowing());
	}

	public void testThatTappingOnEndDateOpensDateDialog() {
		assertFalse(activity.getEndDateDialog().isShowing());

		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				endDate.performClick();
			}
		});

		assertTrue(activity.getEndDateDialog().isShowing());
	}

	@SuppressLint("SimpleDateFormat")
	public void testThatAddingANewExpenseClaimToModelUpdatesActivity() {
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

	public void testThatInputtingAnEndDateThatIsBeforeTheStartDateShowsAnAlert() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				startDate.setText("01-10-2015");
				endDate.setText("01-01-2015");
				name.setText("URoma Trip");
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
				startDate.setText("01-01-2015");
				endDate.setText("01-10-2015");
				name.setText("URoma Trip");
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

				startDate.setText("01-01-2015");
				endDate.setText("01-10-2015");
				name.setText(claimName);

				doneButton.performClick();

			}
		});

		instrumentation.waitForIdleSync();

		TabbedSummaryActivity nextActivity = (TabbedSummaryActivity) instrumentation.waitForMonitor(monitor);
		assertNotNull("Next activity not started", nextActivity);

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date startDateObject = sdf.parse(startDate.getText().toString());
		Date endDateObject = sdf.parse(endDate.getText().toString());

		ExpenseClaim claim = controller.getExpenseClaim(0);

		assertEquals("The two names do not equal each other", claimName, claim.getName());

		assertEquals("The two startDates do not equal each other", 
				startDateObject, 
				claim.getStartDate());

		assertEquals("The two endDates do not equal each other", 
				endDateObject, 
				claim.getEndDate());

		nextActivity.finish();
	}
}
