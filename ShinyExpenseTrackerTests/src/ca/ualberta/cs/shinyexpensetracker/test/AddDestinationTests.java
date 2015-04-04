package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.activities.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests various parts of the functionality of AddDestinationActivity that
 * relates to creating new Destinations.
 **/
public class AddDestinationTests extends ActivityInstrumentationTestCase2<AddDestinationActivity> {

	AddDestinationActivity activity;
	Instrumentation instrumentation;
	EditText nameInput, reasonInput;
	Button doneButton;

	private ExpenseClaimController controller;
	private MockExpenseClaimListPersister persister;
	private ExpenseClaim claim;

	public AddDestinationTests() {
		super(AddDestinationActivity.class);
	}

	public AddDestinationTests(Class<AddDestinationActivity> activityClass) {
		super(activityClass);
	}

	/**
	 * Setup for each test. Creates a new claim and sets up commonly used
	 * variables.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();

		claim = new ExpenseClaim("Example name");
		claimList.addClaim(claim);

		persister = new MockExpenseClaimListPersister(claimList);
		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		instrumentation = getInstrumentation();

		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(intent);

		activity = getActivity();

		nameInput = (EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText);
		reasonInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
		doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
	}

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

		Destination dest = controller.getExpenseClaimByID(claim.getID()).getDestinationAtPosition(0);

		assertEquals(name, dest.getName());
		assertEquals(reason, dest.getReasonForTravel());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}
}
