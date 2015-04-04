package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * Used for adding and editing destinations.
 * 
 * Covers issue #18 - Editing is done there
 * 
 * XXX - Editing is not yet implemented. (Issue #100)
 * 
 */
public class AddDestinationActivity extends Activity {
	private EditText destinationEditText;
	private EditText reasonForTravelEditText;

	public Dialog dialog;

	private ExpenseClaimController controller;

	private ExpenseClaim claim;
	private UUID claimID;

	private Destination destination;
	private int destinationIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_destination);

		Intent intent = getIntent();
		claimID = (UUID) intent.getSerializableExtra(IntentExtraIDs.CLAIM_ID);
		controller = Application.getExpenseClaimController();
		claim = controller.getExpenseClaimByID(claimID);

		destinationIndex = intent.getIntExtra(IntentExtraIDs.DESTINATION_INDEX, -1);
		// Is this a new destination?
		if (destinationIndex == -1) {
			// Yes.
			// Destination object will be created later.
			destination = null;
		} else {
			// No.
			// Set the destination object to the existing object
			destination = claim.getDestination(destinationIndex);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (destination != null) {
			// If we loaded a destination, load the values
			TextView dest = (TextView) findViewById(R.id.destinationEditText);
			TextView reason = (TextView) findViewById(R.id.reasonEditText);

			dest.setText(destination.getName());
			reason.setText(destination.getReasonForTravel());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_destination, menu);
		return true;
	}

	/**
	 * Creates the new destination if this activity is opened without a
	 * destination index, or save it to the existing destination.
	 * 
	 * @return true if destination input was valid, false otherwise.
	 * @throws IOException
	 */
	public boolean saveDestination() throws IOException {
		destinationEditText = (EditText) findViewById(R.id.destinationEditText);
		reasonForTravelEditText = (EditText) findViewById(R.id.reasonEditText);

		if (destinationEditText.getText().length() == 0) {
			// Display an error prompt.
			dialog = new AlertDialog.Builder(this).setMessage("Destination requires a name")
					.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
			dialog.show();
			return false;
		}

		String dest = destinationEditText.getText().toString();
		String reason = reasonForTravelEditText.getText().toString();

		if (destination == null) {
			// If new, create a new one
			destination = new Destination(dest, reason);
			claim.addDestination(destination);
		} else {
			// If old, update the data.
			destination.setName(dest);
			destination.setReasonForTravel(reason);
		}

		controller.update();

		return true;
	}

	/**
	 * Called when the done button is pressed. Attempts to create the
	 * destination. If it fails, stops and warns the user. Otherwise, the
	 * activity is closed.
	 * 
	 * @param v
	 * @throws IOException
	 */
	public void doneCreateDestination(View v) throws IOException {
		if (saveDestination()) {
			finish();
		}
	}
}
