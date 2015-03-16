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

public class DestinationTest extends 
			ActivityInstrumentationTestCase2<AddDestinationActivity> {
	
	AddDestinationActivity activity;
	Instrumentation instrumentation;
	EditText nameInput, reasonInput;
	Button doneButton;
    private ExpenseClaimController controller;
    private ExpenseClaim expenseClaim;
	
		public DestinationTest(){
			super(AddDestinationActivity.class);
		}
		 
		public DestinationTest(Class<AddDestinationActivity> activityClass) {
			super(activityClass);
		}

	protected void setUp() throws Exception {
    	super.setUp();
        instrumentation = getInstrumentation();
        
        ExpenseClaimController controller = ExpenseClaimController.getInstance();
        controller.setClaimList(new ExpenseClaimList());
        controller.addExpenseClaim(new ExpenseClaim("Test Claim"));
        Intent intent = new Intent();
        intent.putExtra("claimIndex", 0);
        setActivityIntent(intent);
        
        activity = getActivity();
        
        nameInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText));
        reasonInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
        doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
    }
	
	/* test if a Destination is made*/
	public void testCreateDestination() {
		instrumentation.runOnMainSync(new Runnable(){
			public void run() {
				assertNotNull(doneButton);
				 nameInput.setText("Miami");
				 reasonInput.setText("Vacation");
				 
				 Destination destination = new Destination(nameInput.getText().toString(), 
						 reasonInput.getText().toString());
				 
				 assertEquals("Name does not equal name", "Miami", destination.getName());
				 assertNotSame("false positive name", "Wrong name", destination.getName());
				 assertEquals("Reason does not equal Reason", "Vacation", destination.getReasonForTravel());
				 assertNotSame("false positive reason", "Wrong reason", destination.getReasonForTravel());				 
			}
		});
	}
	
	/* tests if the data entered has been correctly saved to a Destination when the Done button is clicked */
	public void testDone() {
		//TODO: Test not done being implemented, needs to check to see if loaded destination is what was entered
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
		
		assertEquals("name length == 0", 9, nameInput.getText().length());
		assertEquals("nameInput == Destination.name", "Las Vegas", nameInput.getText().toString());
		
		assertEquals("reason length == 0", 8, reasonInput.getText().length());
		assertEquals("reasonInput == Destination.reason", "Vacation", reasonInput.getText().toString());
		
		fail();
	}
}
