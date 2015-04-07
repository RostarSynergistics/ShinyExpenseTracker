package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.User;

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
			User user;
			try {
				user = Application.getUser();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			cachedClaims = new ArrayList<ExpenseClaim>();
			// get the claims with submitted status
			for (ExpenseClaim claim : source.getClaims()) {
				Log.d("User Filter", claim.getName() + ": " + claim.getUserID() + " == " + user.getUserID());
				if (!claim.getUserID().equals(user.getUserID())) {
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
