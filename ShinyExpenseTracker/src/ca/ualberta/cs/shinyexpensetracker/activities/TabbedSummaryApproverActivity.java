package ca.ualberta.cs.shinyexpensetracker.activities;

import android.view.Menu;
import android.view.MenuItem;
import ca.ualberta.cs.shinyexpensetracker.R;

public class TabbedSummaryApproverActivity extends TabbedSummaryActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary_approver, menu);
		m = menu;
		return true;
	}
	
	/**
	 * Called on MenuItem "Approve Claim" click
	 * @param menu
	 */
	public void approveClaimMenuItem(MenuItem menu) {
		
	}
	
	/**
	 * Called on MenuItem "Comment" click
	 * Opens a dialog box for the approver to enter a comment about the claim 
	 * @param menu
	 */
	public void commentMenuItem(MenuItem menu) {
		
	}
	
	/**
	 * Called on MenuItem "Return Claim" click
	 * @param menu
	 */
	public void returnClaimMenuItem(MenuItem menu) {
		
	}
	
}
