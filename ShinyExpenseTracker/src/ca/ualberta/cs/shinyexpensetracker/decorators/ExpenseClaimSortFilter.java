package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * This decorator sorts the claim list that is being used.
 */
public class ExpenseClaimSortFilter extends ExpenseClaimFilter {
	
	private ArrayList<ExpenseClaim> cachedClaims;
	
	// Comparator for returning descending order
	private static final Comparator<ExpenseClaim> DESC_ORDER =
			new Comparator<ExpenseClaim>() {
				public int compare(ExpenseClaim lhs, ExpenseClaim rhs) {
					return rhs.compareTo(lhs);
				};
			};

	@Override
	public ExpenseClaim getClaim(int index) {
		// Use an array list to get an index
		return getClaims().get(index);
	}

	@Override
	public ArrayList<ExpenseClaim> getClaims() {
		// Cache the claims objects so getting objects is cheaper.
		if (cachedClaims == null) {
			// Sort claims in descending order
			cachedClaims = new ArrayList<ExpenseClaim>(source.getClaims());
			Collections.sort(cachedClaims, DESC_ORDER);
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
