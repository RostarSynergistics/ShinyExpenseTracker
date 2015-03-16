package ca.ualberta.cs.shinyexpensetracker;

import java.text.ParseException;

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
	
	private EditText destinationEditText, reasonForTravelEditText;
	private Button doneButton;
	int claimIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_destination);
		
		doneButton = (Button) findViewById(R.id.addDestinationDoneButton);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_destination, menu);
		return true;
	}
	
	public boolean createDestination(View v) throws ParseException {
		
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex", -1);
		ExpenseClaimController ecc = ExpenseClaimController.getInstance();
		ExpenseClaim claim = ecc.getExpenseClaim(claimIndex);
		
		destinationEditText = (EditText) findViewById(R.id.destinationEditText);
		reasonForTravelEditText = (EditText) findViewById(R.id.reasonEditText);
		
		String dest = destinationEditText.getText().toString();
		String reason = reasonForTravelEditText.getText().toString();
		
		Destination destination = new Destination(dest, reason);
		claim.addDestination(destination);		
		return true;
		
	}
	
	public void doneCreateDestination(View v) throws ParseException{
		
		if (createDestination(v)){
			finish();
		}
	}
}
