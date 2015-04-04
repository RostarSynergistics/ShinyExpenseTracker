package ca.ualberta.cs.shinyexpensetracker.activities;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

// Source: https://github.com/astuetz/PagerSlidingTabStrip
// on March 11 2015
//http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/
//on March 12 2015
/**
 * Deals with displaying the ClaimSummaryFragment, ExpenseItemListFragment, and
 * DestinationListFragment.  Allows user to either click on the action bar tabs
 * or swipe left and right to view the other tabs.  
 * 
 * Called when user clicks on an Expense claim in the expenseClaimListActivity
 * 
 * Begins by loading the ClaimSummaryFragment.	
 *
 */
public class TabbedSummaryClaimantActivity extends TabbedSummaryActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary_claimant, menu);
		m = menu;
		return true;
	}
	
	/**
	 * Called on MenuItem "Add Expense Item" click
	 * Goes to ExpenseItemActivity to allow user to add an expense item to their claim
	 * @param menu
	 */
	public void addExpenseItemMenuItem(MenuItem menu) {
		intent = new Intent(TabbedSummaryClaimantActivity.this, ExpenseItemActivity.class);
		intent.putExtra("claimIndex", claimIndex);
		startActivity(intent);
	}
	
	/**
	 * Called on MenuItem "Add Tag" click
	 * @param menu
	 */
	public void addTagMenuItem(MenuItem menu) {
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex" , -1);
		intent = new Intent(TabbedSummaryClaimantActivity.this, AddTagToClaimActivity.class);
		intent.putExtra("claimIndex", claimIndex);
		startActivity(intent);
	}
	
	/**
	 * Called on MenuItem "Remove Tag" click
	 * @param menu
	 */
	public void removeTagMenuItem(MenuItem menu) {
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex" , -1);
		intent = new Intent(TabbedSummaryClaimantActivity.this, RemoveTagFromClaimActivity.class);
		intent.putExtra("claimIndex", claimIndex);
		startActivity(intent);
	}
	
	/** 
	 * Called on MenuItem "Add Destination" click
	 * Takes user to add destination screen
	 * @param menu
	 */
	public void addDestinationMenuItem(MenuItem menu) {
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex", -1);
		intent = new Intent(TabbedSummaryClaimantActivity.this, AddDestinationActivity.class);
		intent.putExtra("claimIndex", claimIndex);
		startActivity(intent);
	}
	
	/**
	 * Called on MenuItem "Edit Claim" click
	 * Takes user to edit claim screen
	 * @param menu
	 */
	public void editClaimMenuItem(MenuItem menu) {
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex", -1);
		
		intent = new Intent(this, ExpenseClaimActivity.class);

		intent.putExtra("claimIndex", claimIndex);
		startActivity(intent);
	}
	
	/**
	 * Called on MenuItem "Submit Claim" click
	 * Changes status of claim to "Submitted" if claim is complete
	 * Disables 'Submit Claim' menu item once claim is submitted
	 * @param menu
	 */
	public void submitClaimMenuItem(MenuItem menu) {
		
		ExpenseClaimController ecc = Application.getExpenseClaimController();
		
		Intent intent = getIntent();
		int claimIndex = intent.getIntExtra("claimIndex", -1);
		ArrayList<ExpenseItem> expenses = ecc.getExpenseItems(ecc.getExpenseClaim(claimIndex));
		boolean incomplete = false;
		for (ExpenseItem expense : expenses) {
			if (expense.getIsMarkedIncomplete()) {
				adb.setMessage("Cannot submit an incomplete claim");
				adb.setCancelable(true);

				adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) { }
				});
				alertDialog = adb.create();
				alertDialog.show();
				incomplete = true;
				break;
			}
		}
		if (!incomplete) {
			ecc.getExpenseClaim(claimIndex).setStatus(Status.SUBMITTED);
			adb.setMessage("Claim Submitted for Approval");
			adb.setCancelable(true);
			adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialog = adb.create();
			alertDialog.show();
			
			// set menu items to false, so claim cannot be edited or submitted again
			m.getItem(0).setEnabled(false);
			m.getItem(3).setEnabled(false);
			m.getItem(4).setEnabled(false);
			m.getItem(5).setEnabled(false);
		}
	}
	
}
