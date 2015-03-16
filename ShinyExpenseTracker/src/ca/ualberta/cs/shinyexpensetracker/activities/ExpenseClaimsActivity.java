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

import java.io.IOException;

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
import ca.ualberta.cs.shinyexpensetracker.Application;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.adapters.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ExpenseClaimsActivity 
	extends Activity 
	implements IView<ExpenseClaimList> {
	private ExpenseClaimController controller;
	private ClaimListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claims_view);
	}

	@Override
	protected void onStart() {
		super.onStart();
		controller = Application.getExpenseClaimController();
		controller.getExpenseClaimList().addView(this);

		// Set the list view to receive updates from the model
		final ListView claim_list = (ListView) findViewById(R.id.expense_claim_list);
		adapter = new ClaimListAdapter(this);
		claim_list.setAdapter(adapter);

		claim_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ExpenseClaimsActivity.this,
						TabbedSummaryActivity.class);
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
		case R.id.action_sort:
			return true;
		case R.id.action_filter:
			// TODO #28
			return true;
		case R.id.action_manage_tags:
			Intent manageTagsIntent = new Intent(ExpenseClaimsActivity.this,
					ManageTagActivity.class);
			startActivity(manageTagsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(ExpenseClaimList m) {
		adapter.notifyDataSetChanged();
	}

	public void addClaim(ExpenseClaim claim) throws IOException {
		controller.addExpenseClaim(claim);
	}

	public void deleteClaim(ExpenseClaim claim) throws IOException {
		controller.removeExpenseClaim(claim);
	}

	public AlertDialog askDeleteClaimAt(int position) {
		// Alert Dialog (Mar 7, 2015):
		// http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
		// http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog
		// Get a final copy of the requested claim
		final ExpenseClaim claimToDelete = controller.getExpenseClaim(position);

		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Delete Claim?")
				.setMessage(
						"Delete '" + claimToDelete.toString()
								+ "'?\n(This cannot be undone)")
				// If OK, delete the claim. (Positive action);
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							deleteClaim(claimToDelete);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
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
				}).create();

		dialog.show();
		return dialog;
	}

}
