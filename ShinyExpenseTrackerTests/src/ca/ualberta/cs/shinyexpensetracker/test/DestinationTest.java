package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.Application;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * @author ramishsyed
 * Class for testing addDestination and testing the AddDestinations button
 */
public class DestinationTest extends 
			ActivityInstrumentationTestCase2<AddDestinationActivity> {
	
	
	AddDestinationActivity activity;
	Instrumentation instrumentation;
	EditText nameInput, reasonInput;
	Button doneButton;
	
    private ExpenseClaim claim;
	private ExpenseClaimController controller;
	
	public DestinationTest() {
		super(AddDestinationActivity.class);
	}

	public DestinationTest(Class<AddDestinationActivity> activityClass) {
		super(activityClass);
	}
	
	protected void setUp() throws Exception {
		super.setUp();

		controller = new ExpenseClaimController(
				new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim("Example name");
		controller.addExpenseClaim(claim);

		instrumentation = getInstrumentation();

		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);

		activity = getActivity();
        
        nameInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText));
        reasonInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
        doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
    }
	
	/**
	 * Tests whether a destination is successfully created or not
	 */

	public void testCreateDestination() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				assertNotNull(doneButton);
				nameInput.setText("Miami");
				reasonInput.setText("Vacation");

				Destination destination = new Destination(nameInput.getText()
						.toString(), reasonInput.getText().toString());

				assertEquals("Name does not equal name", "Miami",
						destination.getName());
				assertNotSame("false positive name", "Wrong name",
						destination.getName());
				assertEquals("Reason does not equal Reason", "Vacation",
						destination.getReasonForTravel());
				assertNotSame("false positive reason", "Wrong reason",
						destination.getReasonForTravel());
			}
		});
	}
	
	public void testText() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				nameInput.setText("Germany");
				reasonInput.setText("Europe trip");
			}
		});
		
		assertEquals("Germany", nameInput.getText().toString());
		assertEquals("Europe trip", reasonInput.getText().toString());
	}
	
	/**
	 * Tests if the data entered has been correctly saved to a Destination when the Done button is clicked 
	 **/
	
	public void testDone() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				nameInput.setText("Las Vegas");
				reasonInput.setText("Vacation");
				doneButton.performClick();
				activity = getActivity();
			}
		});
		instrumentation.waitForIdleSync();
		assertTrue(activity != null);

		int claimIndex = controller.getIndexOf(claim) + 1;

		Destination dest = controller.getExpenseClaim(claimIndex)
				.getDestination(0);

		assertEquals("Destination name does not equal 'Las Vegas'",
				"Las Vegas", dest.getName());
		assertEquals("Deastination reason does not equal 'Vacation'",
				"Vacation", dest.getReasonForTravel());
	}
}
