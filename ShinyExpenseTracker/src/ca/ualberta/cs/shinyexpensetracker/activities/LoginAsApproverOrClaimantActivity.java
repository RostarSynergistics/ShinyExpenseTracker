package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.User;
import ca.ualberta.cs.shinyexpensetracker.utilities.InAppHelpDialog;

public class LoginAsApproverOrClaimantActivity extends Activity {

	private AlertDialog.Builder adb;
	private AlertDialog alertDialogGetUserName;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_as_approver_or_claimant);

		User user;

		try {
			user = Application.getUser();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (user == null) {
			// No, get it
			adb = new AlertDialog.Builder(this);

			LayoutInflater layoutInflater = this.getLayoutInflater();
			View dialogView = layoutInflater.inflate(R.layout.dialog_approver_name_input, null);
			adb.setView(dialogView);

			final EditText nameTextBox = (EditText) dialogView.findViewById(R.id.EditTextDialogUserName);

			adb.setMessage("Name: ");

			// Setting the positive button to save the text in the dialog as a
			// comment
			// if valid
			adb.setPositiveButton("Name", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String name = nameTextBox.getText().toString();

					try {
						Application.setUser(new User(name));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});

			// Setting the negative button to close the dialog
			adb.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			alertDialogGetUserName = adb.create();
			alertDialogGetUserName.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in_as_approver_or_claimant, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help) {
			InAppHelpDialog.showHelp(this, R.string.help_login);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Saves the type of the user to the User and the application upon logging
	 * in to the system
	 * 
	 * @param v
	 */
	/*
	 * source
	 * http://developer.android.com/guide/topics/ui/controls/radiobutton.html on
	 * April 1 2015
	 */
	public void saveUserType(View v) {
		boolean checked = ((RadioButton) findViewById(R.id.approverRadioButton)).isChecked();

		if (checked) {
			Application.switchToApproverMode();
		} else {
			Application.switchToClaimantMode();
		}
	}

	/**
	 * when user selects continue button, saves the selection of user type into
	 * the application as well as into the user takes user to claim view
	 * activity
	 * 
	 * @param v
	 */
	public void login(View v) {
		Intent intent;

		saveUserType(v);

		intent = new Intent(LoginAsApproverOrClaimantActivity.this, ExpenseClaimListActivity.class);

		startActivity(intent);
	}

	/**
	 * for testing purposes only
	 */
	public AlertDialog getDialog() {
		return alertDialogGetUserName;
	}
}
