package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RadioButton;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimListActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.LoginAsApproverOrClaimantActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.User.Type;

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

	/**
	 * Test that when the continue button is click and the Approver radio button
	 * is checked in the login page that the users state in the application was
	 * set to Approver
	 */
	public void testSaveUserTypeApprover() {

		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseClaimListMonitor = getInstrumentation()
				.addMonitor(ExpenseClaimListActivity.class.getName(), null, false);

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// set the approver radio button to checked
				((RadioButton) activity
						.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.approverRadioButton))
						.setChecked(true);
				activity.findViewById(
						ca.ualberta.cs.shinyexpensetracker.R.id.continueButton)
						.performClick();

			}
		});

		// Get the view expense claims activity
		final ExpenseClaimListActivity expenses = (ExpenseClaimListActivity) getInstrumentation()
				.waitForMonitorWithTimeout(expenseClaimListMonitor, 1000);
		assertEquals(true,
				getInstrumentation()
						.checkMonitorHit(expenseClaimListMonitor, 1));

		assertEquals("User State was not set to Approver", Type.Approver,
				Application.getUserType());

		expenses.finish();

		getInstrumentation().waitForIdleSync();
		
		assertEquals("User State was not set to Approver", 'a' ,Application.getUserType());

	}

	/**
	 * Test that when the continue button is click and the Claimant radio button
	 * is checked in the login page that the users state in the application was
	 * set to Claimant
	 */
	public void testSaveUserTypeClaimant() {

		// Monitor for ExpenseItemActivity
		ActivityMonitor expenseClaimListMonitor = getInstrumentation()
				.addMonitor(ExpenseClaimListActivity.class.getName(), null, false);

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// set the approver radio button to checked
				((RadioButton) activity
						.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.claimantRadioButton))
						.setChecked(true);
				activity.findViewById(
						ca.ualberta.cs.shinyexpensetracker.R.id.continueButton)
						.performClick();
			}
		});

		// Get the view expense claims activity
		final ExpenseClaimListActivity expenses = (ExpenseClaimListActivity) getInstrumentation()
				.waitForMonitorWithTimeout(expenseClaimListMonitor, 1000);
		assertEquals(true,
				getInstrumentation()
						.checkMonitorHit(expenseClaimListMonitor, 1));

		expenses.finish();

		assertEquals("User State was not set to Claimant", Type.Claimant,
				Application.getUserType());

		getInstrumentation().waitForIdleSync();
		
		assertEquals("User State was not set to Claimant", 'c' ,Application.getUserType());
		
	}

}
