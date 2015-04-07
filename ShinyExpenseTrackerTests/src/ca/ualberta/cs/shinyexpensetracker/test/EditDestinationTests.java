package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.UUID;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.activities.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests various parts of the functionality of AddDestinationActivity that
 * relates to editing existing Destinations.
 **/
public class EditDestinationTests extends ActivityInstrumentationTestCase2<AddDestinationActivity> {

	AddDestinationActivity activity;
	Instrumentation instrumentation;
	EditText nameField, reasonField;
	Button doneButton;
	TextView geolocationValue;

	private ExpenseClaimController controller;
	private MockExpenseClaimListPersister persister;
	private Destination destination;
	private ExpenseClaim claim;

	public EditDestinationTests() {
		super(AddDestinationActivity.class);
	}

	public EditDestinationTests(Class<AddDestinationActivity> activityClass) {
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

		claim = new ExpenseClaim(UUID.randomUUID(), "Example name");
		claimList.addClaim(claim);

		destination = new Destination("Indianapolis", "Gen Con", new Coordinate(39.791, -86.1480));

		claim.addDestination(destination);

		persister = new MockExpenseClaimListPersister(claimList);
		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		instrumentation = getInstrumentation();

		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		intent.putExtra(IntentExtraIDs.DESTINATION_ID, destination.getID());
		setActivityIntent(intent);

		activity = getActivity();

		nameField = (EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText);
		reasonField = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
		doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
		geolocationValue = (TextView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.coordinatesValueTextView);
	}

	public void testThatFieldsWerePopulatedProperlyOnStart() {
		assertEquals(destination.getName(), nameField.getText().toString());
		assertEquals(destination.getReasonForTravel(), reasonField.getText().toString());
		assertEquals(destination.getGeolocation().toString(), geolocationValue.getText().toString());
	}

	public void testThatTappingDoneUpdatesTheExistingDestination() {
		final String name = "Las Vegas";
		final String reason = "Vacation";

		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				nameField.setText(name);
				reasonField.setText(reason);
				doneButton.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		Destination dest = controller.getExpenseClaimByID(claim.getID()).getDestinationByID(destination.getID());

		assertEquals(name, dest.getName());
		assertEquals(reason, dest.getReasonForTravel());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}
}
