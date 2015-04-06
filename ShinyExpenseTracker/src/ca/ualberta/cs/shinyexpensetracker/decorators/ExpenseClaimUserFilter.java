package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;

import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ExpenseClaimUserFilter extends ExpenseClaimFilter {

	public ExpenseClaimUserFilter() {
		// TODO Auto-generated constructor stub
	}

	private ArrayList<ExpenseClaim> cachedClaims;

	@Override
	public ExpenseClaim getClaimAtPosition(int index) {
		// Use an array list to get an index
		return getClaims().get(index);
	}

	@Override
	public ArrayList<ExpenseClaim> getClaims() {
		
		// Cache the claims objects so getting objects is cheaper.
		if (cachedClaims == null) {

			cachedClaims = new ArrayList<ExpenseClaim>();	
			//get the claims with submitted status
			for (ExpenseClaim claim : source.getClaims()) {
				Log.d("User Filter", claim.getName() + ": " + claim.getUserId() + " == " + Application.getUser().getUserId());
				if (!claim.getUserId().equals(Application.getUser().getUserId())) {
					cachedClaims.add(claim);
				}
			}
		}
		return cachedClaims;
	}

	@Override
	public int getCount() {
		return getClaims().size();
	}
	
	@Override
	protected void onDatasetChanged() {
		super.onDatasetChanged();
		cachedClaims = null;
	}

}
