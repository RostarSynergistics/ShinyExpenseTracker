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
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.GeolocationRequestCode;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.adapters.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimSortFilter;
import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimSubmittedFilter;
import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimUserFilter;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.User;
import ca.ualberta.cs.shinyexpensetracker.utilities.InAppHelpDialog;


public class ExpenseClaimListActivity extends Activity implements IView<ExpenseClaimList> {


	private ExpenseClaimController controller;
	private ClaimListAdapter adapter;
	private User user;
	// TODO: these have to be moved to another object,
	// like Claimant or something, when that class is created
	private Coordinate homeGeolocation = new Coordinate();

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

		try {
			user = Application.getUser();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Set the list view to receive updates from the model
		final ListView claim_list = (ListView) findViewById(R.id.expense_claim_list);
		adapter = new ClaimListAdapter(this);
		// Ensure the adapter is sorted.
		adapter.applyFilter(new ExpenseClaimSortFilter());

		// if the user is an approver filter the claims so only the submitted
		// claims are shown
		if (Application.inApproverMode()) {
			adapter.applyFilter(new ExpenseClaimSubmittedFilter());
			adapter.applyFilter(new ExpenseClaimUserFilter());
		}

		claim_list.setAdapter(adapter);

		if (user.getHomeGeolocation() != null) {
			TextView homeGeolocationValue = (TextView) findViewById(R.id.homeGeolocationValueTextView);
			homeGeolocationValue.setText(user.getHomeGeolocation().toString());
			homeGeolocation = user.getHomeGeolocation();
		}

		claim_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ExpenseClaim claim = adapter.getItem(position);

				Intent intent;

				if (Application.inClaimantMode()) {
					intent = new Intent(ExpenseClaimListActivity.this, TabbedSummaryClaimantActivity.class);
				} else {
					intent = new Intent(ExpenseClaimListActivity.this, TabbedSummaryApproverActivity.class);
				}

				intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
				startActivity(intent);
			}
		});

		// -- Long Press of ListView Item
		claim_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				if (controller.getExpenseClaimAtPosition(position).getStatus() == Status.SUBMITTED) {
					disableSubmittedClaimDeletion();
				}
				// Don't change this line. Change "askDeleteClaimAt" instead.
				else {
					askDeleteClaimAt(position);
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_claims_view, menu);

		if (Application.inApproverMode()) {
			menu.getItem(0).setEnabled(false);
		}

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

			Intent intent = new Intent(this, ExpenseClaimActivity.class);
			startActivity(intent);

			return true;
		case R.id.action_manage_tags:
			Intent manageTagsIntent = new Intent(ExpenseClaimListActivity.this, ManageTagActivity.class);
			startActivity(manageTagsIntent);
			return true;
		case R.id.set_home_geolocation:
			Intent geolocationViewIntent = new Intent(ExpenseClaimListActivity.this, GeolocationViewActivity.class);
			startActivityForResult(geolocationViewIntent, GeolocationRequestCode.SET_GEOLOCATION);
			return true;
		case R.id.display_geolocations_on_map:
			Intent geolocationMapViewIntent = new Intent(ExpenseClaimListActivity.this,
					MapViewActivity.class);
			geolocationMapViewIntent.putExtra(IntentExtraIDs.REQUEST_CODE, GeolocationRequestCode.DISPLAY_GEOLOCATIONS);
			startActivity(geolocationMapViewIntent);
			return true;
		case R.id.action_help:
			InAppHelpDialog.showHelp(this, R.string.help_claim_list);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Get result back from setting geolocation Put it in a Toast until there is
	 * a Claimant class where the values can actually be stored
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check result is ok
		if (resultCode == RESULT_OK) {
			double latitude = data.getDoubleExtra(IntentExtraIDs.LATITUDE, Coordinate.DEFAULT_COORDINATE.getLatitude());
			double longitude = data.getDoubleExtra(IntentExtraIDs.LONGITUDE, Coordinate.DEFAULT_COORDINATE.getLongitude());
			homeGeolocation.setLatitude(latitude);
			homeGeolocation.setLongitude(longitude);
			TextView homeGeolocationValue = (TextView) findViewById(R.id.homeGeolocationValueTextView);
			homeGeolocationValue.setText(homeGeolocation.toString());
			user.setHomeGeolocation(homeGeolocation);
		}
	}

	@Override
	public void update(ExpenseClaimList m) {
		adapter.notifyDataSetChanged();
	}

	/**
	 * Deletes a claim from the claim list controller
	 * 
	 * @param claim
	 * @throws IOException
	 */
	public void deleteClaim(ExpenseClaim claim) throws IOException {
		controller.deleteExpenseClaim(claim.getID());
	}

	/**
	 * Disables deletion of submitted claim
	 * 
	 * @return
	 */

	public AlertDialog disableSubmittedClaimDeletion() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Not availble")
				.setMessage("You cannot delete a Submitted claim")
				// Do nothing
				.setNeutralButton("Okay", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
						dialog.dismiss();
					}
				}).create();

		dialog.show();
		return dialog;
	}

	/**
	 * This creates and shows an dialog used when a claim is longed clicked.
	 * This dialog is used for deleting a claim
	 * 
	 * @param position
	 * @return The alertDialog for delete claim
	 */
	public AlertDialog askDeleteClaimAt(int position) {
		// Alert Dialog (Mar 7, 2015):
		// http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
		// http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog
		// Get a final copy of the requested claim
		final ExpenseClaim claimToDelete = controller.getExpenseClaimAtPosition(position);

		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Delete Claim?")
				.setMessage("Delete '" + claimToDelete.toString() + "'?\n(This cannot be undone)")
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

	public Coordinate getHomeGeolocation() {
		return homeGeolocation;
	}
}
