/**
 *  This Activity displays a list of expense claims that
 *  the user can manage.
 *  
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Issue #22
 * @author Tristan Meleshko
 */

package ca.ualberta.cs.shinyexpensetracker.activities;

import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.AddExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

// TODO #55 ?
public class ExpenseClaimsView extends Activity implements IView<ExpenseClaimList> {
	private ExpenseClaimController controller;
	private ClaimListAdapter adapter;
	static int debug_addedID = 0;	// XXX Remove this when #17 is merged

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claims_view);
		controller = ExpenseClaimController.getInstance();
		controller.getExpenseClaimList().addView(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		// Set the list view to receive updates from the model
		final ListView claim_list = (ListView) findViewById(R.id.expense_claim_list);
		// TODO #61 - adapter should be a custom adapter that can
		// watch the ExpenseClaimsList object, rather than an
		// ArrayList object
		adapter = new ClaimListAdapter(this);
		claim_list.setAdapter(adapter);
		
		
		claim_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(ExpenseClaimsView.this, TabbedSummaryActivity.class);
				intent.putExtra("claimIndex", position);
				startActivity(intent);
				
			}
			
		});
		
		
		// -- Long Press of ListView Item
		claim_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Don't change this line. Change "askDeleteClaimAt" instead.
				askDeleteClaimAt(position);
				return true;
			}
		});
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_claims_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_new_claim:
			
			Intent intent = new Intent(this, AddExpenseClaimActivity.class);
			startActivity(intent);
			
			return true;
			// TODO Depends on #17

			// Add randomly dated claim
			// XXX Begin -- REMOVE ME
			/* Replace this code with something that opens an activity. (#76)
			Date sd = new Date(System.currentTimeMillis() + new Random().nextInt(2000000000));
			Date ed = null;
			if (new Random().nextBoolean()) {
				ed = new Date(System.currentTimeMillis() + 2000000000  + new Random().nextInt(2000000000));
			}
			
			controller.addExpenseClaim(
					new ExpenseClaim("Test ["+debug_addedID++ +"]", sd, ed));
			
			return true;
			// XXX REMOVE ME -- End*/
			
		case R.id.action_sort:
		case R.id.action_filter:
		case R.id.action_manage_tags:
			// TODO #22, #28
			Toast.makeText(this, "Not Yet Implemented.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(ExpenseClaimList m) {
		adapter.notifyDataSetChanged();
	}
	
	public void addClaim(ExpenseClaim claim) {
		controller.addExpenseClaim(claim);
	}
	
	public void deleteClaim(ExpenseClaim claim) {
		controller.removeExpenseClaim(claim);
	}
	
	public AlertDialog askDeleteClaimAt(int position) {
		// Alert Dialog (Mar 7, 2015):
		//  http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
		//  http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog
		// Get a final copy of the requested claim
		final ExpenseClaim claimToDelete = controller.getExpenseClaim(position);
		
		AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("Delete Claim?")
			.setMessage("Delete '" + claimToDelete.toString() + "'?\n(This cannot be undone)")
			// If OK, delete the claim. (Positive action);
			.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteClaim(claimToDelete);
					dialog.dismiss();
				}
			})
			// If cancel, do nothing
			.setNeutralButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Do nothing
					dialog.dismiss();
				}
			})
			.create();
		
		dialog.show();
		return dialog;
	}
	
}