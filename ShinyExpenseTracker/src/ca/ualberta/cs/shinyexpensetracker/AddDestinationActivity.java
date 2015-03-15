package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.DestinationList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import android.net.NetworkInfo.DetailedState;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class AddDestinationActivity extends Activity {
	
	private EditText destinationEditText;
	private EditText reasonForTravelEditText;
	private ExpenseClaimController ecc;
	private DestinationList destList;
	int claimIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_destination);
		
		findViewsById();
		ecc = ExpenseClaimController.getInstance();
		destList = new DestinationList();
		claimIndex = getIntent().getExtras().getInt("claimIndex");
		
        final Button button = (Button) findViewById(R.id.addDestinationDoneButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	addNewDestination();
            }
        });

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
	
	public boolean addNewDestination() {
		
		String dest = "";
		String reason = "";
		
		dest = destinationEditText.getText().toString();
		reason = reasonForTravelEditText.getText().toString();
		
		Destination destination = new Destination(dest, reason);
		ecc.getExpenseClaim(claimIndex).addDestination(destination);
		
		return true;
		
	}
}
