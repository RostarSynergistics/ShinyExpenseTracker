package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests various parts of the functionality of ExpenseClaimActivity that relates
 * to editing existing ExpenseClaims.
 **/
@SuppressLint("SimpleDateFormat")
public class EditExpenseClaimTests extends ActivityInstrumentationTestCase2<ExpenseClaimActivity> {
	static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

	MockExpenseClaimListPersister persister;
	ExpenseClaimController controller;
	Instrumentation instrumentation;
	ExpenseClaimActivity activity;

	EditText startDateField, endDateField, nameField;
	Button doneButton;

	ExpenseClaimList list;
	ExpenseClaim claim;

	public EditExpenseClaimTests(Class<ExpenseClaimActivity> activityClass) {
		super(activityClass);
	}

	public EditExpenseClaimTests() {
		super(ExpenseClaimActivity.class);
	}

	/**
	 * Loads the activity with a test existing ExpenseClaim set.
	 */
	protected void setUp() throws Exception {
		super.setUp();

		list = new ExpenseClaimList();
		claim = getStartingClaim();

		list.addClaim(claim);

		persister = new MockExpenseClaimListPersister(list);
		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		Intent i = new Intent();
		i.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(i);

		instrumentation = getInstrumentation();
		activity = getActivity();

		nameField = (EditText) activity.findViewById(R.id.editTextExpenseClaimName);
		startDateField = (EditText) activity.findViewById(R.id.editTextStartDate);
		endDateField = (EditText) activity.findViewById(R.id.editTextEndDate);
		doneButton = (Button) activity.findViewById(R.id.addExpenseClaimDoneButton);

		assertFalse(persister.wasSaveCalled());
	}

	public void testThatFieldsWerePopulatedProperlyOnStart() throws ParseException {
		assertEquals(claim.getName(), nameField.getText().toString());
		assertEquals(claim.getStartDate(), getDate(startDateField));
		assertEquals(claim.getEndDate(), getDate(endDateField));
	}

	public void testThatTappingDoneWhileEditingAnExistingExpenseClaimUpdatesThatExpenseClaim() throws ParseException {
		final String newName = "URoma";
		final String newStartDateText = "2015-03-01";
		final String newEndDateText = "2015-03-07";

		Date newStartDate = sdf.parse(newStartDateText);
		Date newEndDate = sdf.parse(newEndDateText);

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				nameField.setText(newName);
				startDateField.setText(newStartDateText);
				endDateField.setText(newEndDateText);

				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		ExpenseClaim updatedClaim = controller.getExpenseClaimAtPosition(0);

		assertEquals(newName, updatedClaim.getName());
		assertEquals(newStartDate, updatedClaim.getStartDate());
		assertEquals(newEndDate, updatedClaim.getEndDate());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}

	private ExpenseClaim getStartingClaim() throws ParseException, ValidationException {
		Date startDate = sdf.parse("2015-01-01");
		Date endDate = sdf.parse("2015-01-02");

		return new ExpenseClaim(UUID.randomUUID(), "test", startDate, endDate, ExpenseClaim.Status.IN_PROGRESS);
	}

	private Date getDate(EditText dateField) throws ParseException {
		return sdf.parse(dateField.getText().toString());
	}
}
