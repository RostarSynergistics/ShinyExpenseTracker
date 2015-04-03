package ca.ualberta.cs.shinyexpensetracker.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import ca.ualberta.cs.shinyexpensetracker.activities.LoginAsApproverOrClaimantActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;

public class LoginAsApproverOrClaimantActivityTest extends
		ActivityInstrumentationTestCase2<LoginAsApproverOrClaimantActivity> {

	LoginAsApproverOrClaimantActivity activity;
	
	public LoginAsApproverOrClaimantActivityTest() {
		super(LoginAsApproverOrClaimantActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
	}

	public void testSaveUserTypeApprover() {

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// set the approver radio button to checked
				((RadioButton) activity.findViewById(
						ca.ualberta.cs.shinyexpensetracker.R.id.approverRadioButton)).setChecked(true);
				activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.continueButton).performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		assertEquals("User State was not set to Approver", 'a' ,Application.getUserState());
		
	}
	
	public void testSaveUserTypeClaimant() {

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// set the approver radio button to checked
				((RadioButton) activity.findViewById(
						ca.ualberta.cs.shinyexpensetracker.R.id.claimantRadioButton)).setChecked(true);
				activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.continueButton).performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		assertEquals("User State was not set to Claimant", 'c' ,Application.getUserState());
		
	}
	
}
