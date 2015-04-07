package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Status;

/**
 * Filters out just submitted claims so that they can be displayed for approvers
 *
 */
public class ExpenseClaimSubmittedFilter extends ExpenseClaimFilter{

	public ExpenseClaimSubmittedFilter() {
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
				if (claim.getStatus().equals(Status.SUBMITTED)) {
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
