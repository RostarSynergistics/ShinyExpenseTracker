/**
 *  This Activity displays a list of expense claims that
 *  the user can manage.
 *  
 *  Copyright (C) 2015  Tristan Meleshko
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
 * @author ItsTristan
 */

package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.IView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

// TODO #55
public class ExpenseClaimsView extends Activity implements IView<ExpenseClaimList> {
	private ArrayAdapter<ExpenseClaim> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claims_view);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// Get access to the claim list
		ExpenseClaimList claims = ExpenseClaimController.getExpenseClaimList();
		
		claims.addView(this);
		
		// Set the list view to receive updates from the model
		ListView claim_list = (ListView) findViewById(R.id.expense_claim_list);
		// TODO #61 - adapter should be a custom adapter that can get all claims itself
		adapter = new ArrayAdapter<ExpenseClaim>(this, R.layout.list_item,
				claims.getAllClaims());
		claim_list.setAdapter(adapter);
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
			// TODO Depends on #17
			ExpenseClaimController.addExpenseClaim(new ExpenseClaim("Test. Millis = " + System.currentTimeMillis()));
			Toast.makeText(this, "Added claim", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_sort:
		case R.id.action_filter:
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
	
}
