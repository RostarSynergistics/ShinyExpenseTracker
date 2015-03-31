package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.activities.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Class for testing addDestination and testing the
 * AddDestinations button.
 * 
 * Tests the AddDestination Activity, which is responsible
 * for adding and editing destinations to a claim.
 */
public class AddDestinationTests extends
		ActivityInstrumentationTestCase2<AddDestinationActivity> {

	AddDestinationActivity activity;
	Instrumentation instrumentation;
	EditText nameInput, reasonInput;
	Button doneButton;

	private ExpenseClaimController controller;
	private MockExpenseClaimListPersister persister;

	public AddDestinationTests() {
		super(AddDestinationActivity.class);
	}

	public AddDestinationTests(Class<AddDestinationActivity> activityClass) {
		super(activityClass);
	}

	/**
	 * Setup for each test. Creates a new claim and
	 * sets up commonly used variables.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		persister = new MockExpenseClaimListPersister();
		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim("Example name");
		controller.addExpenseClaim(claim);

		instrumentation = getInstrumentation();

		Intent intent = new Intent();
		intent.putExtra(ExpenseClaimActivity.CLAIM_INDEX, 0);
		setActivityIntent(intent);

		activity = getActivity();

		nameInput = (EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText);
		reasonInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
		doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
	}

	/**
	 * Tests if the data entered has been correctly saved to a Destination when
	 * the Done button is clicked
	 **/
	public void testThatTappingDoneCreatesANewDestination() {
		final String name = "Las Vegas";
		final String reason = "Vacation";
		
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				nameInput.setText(name);
				reasonInput.setText(reason);
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		Destination dest = controller.getExpenseClaim(0).getDestination(0);

		assertEquals(name, dest.getName());
		assertEquals(reason, dest.getReasonForTravel());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}
}
