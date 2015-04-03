package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.User;
import ca.ualberta.cs.shinyexpensetracker.models.User.Type;

public class LoginAsApproverOrClaimantActivity extends Activity {

	User user = new User();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_as_approver_or_claimant);
		
		user.setUserId(1);
		
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Saves the type of the user upon logging in to the system
	 * 
	 * @param v
	 */
	/* source http://developer.android.com/guide/topics/ui/controls/radiobutton.html on April 1 2015 */
	public void saveUserType(View v) {
	
		boolean checked = ((RadioButton) v).isChecked();
		
		switch (v.getId()) {
		case (R.id.approverRadioButton):
			if (checked) {
				user.setUserType(Type.Approver);
				Application.setUserType(Type.Approver);
				break;
			}
		case (R.id.claimantRadioButton):
			if (checked) {
				user.setUserType(Type.Claimant);
				Application.setUserType(Type.Claimant);
				break;
			}
		}
	}

	/**
	 * when user selects continue button, saves the selection of user type into the application
	 * as well as into the user takes user to claim view activity
	 * 
	 * @param v
	 */
	public void login(View v) {
		saveUserType(v);
		Intent intent;
		
		if (user.getUserType().equals(Type.Claimant)) {
			intent = new Intent(LoginAsApproverOrClaimantActivity.this,
					ExpenseClaimListActivity.class);
		} else {
			// will go to approver claim view
			intent = new Intent(LoginAsApproverOrClaimantActivity.this, ExpenseClaimListActivity.class);
		}
		
		startActivity(intent);

	}
}