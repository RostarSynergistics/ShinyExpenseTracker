package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class AddDestinationActivity extends Activity {
	
	private EditText destinationEditText;
	private EditText reasonForTravelEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_destination);
		
		findViewsById();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_destination, menu);
		return true;
	}
	
	private void findViewsById() {
		destinationEditText = (EditText) findViewById(R.id.destinationEditText);
		reasonForTravelEditText = (EditText) findViewById(R.id.reasonEditText);
	}
	
	public boolean addDestination() {
		
		String dest = "";
		String reason = "";
		ExpenseClaimController.getInstance();
		
		dest = destinationEditText.getText().toString();
		reason = reasonForTravelEditText.getText().toString();
		
		Destination destination = new Destination(dest, reason);
		
		//Still needs to be implemented: Add Destination to ExpenseClaim
		
		return false;
		
	}

}
