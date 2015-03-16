package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.activities.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class DestinationTest extends ActivityInstrumentationTestCase2<AddDestinationActivity> {
	AddDestinationActivity activity;
	 Instrumentation instrumentation;
	 EditText nameInput, reasonInput;
	 Button doneButton;
	
    public DestinationTest(Class<AddDestinationActivity> activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
    	super.setUp();

        ExpenseClaimController controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
        Application.setExpenseClaimController(controller);

        controller.addExpenseClaim(new ExpenseClaim("Test Claim"));
        
        instrumentation = getInstrumentation();
        activity = getActivity();
        
        nameInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.destinationEditText));
        reasonInput = ((EditText) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.reasonEditText));
        doneButton = (Button) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.addDestinationDoneButton);
    }
	
	/* test if a Destination is made*/
	public void testCreateExpenseDestination() {
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
}
