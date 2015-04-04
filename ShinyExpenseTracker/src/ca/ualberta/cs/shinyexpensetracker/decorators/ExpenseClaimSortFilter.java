package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * This decorator sorts the claim list that is being used.
 */
public class ExpenseClaimSortFilter extends ExpenseClaimFilter {
	// This treeset is updated when values are added or removed
	// from the source object.
	
	private SortedSet<ExpenseClaim> sortedClaims;
	private ArrayList<ExpenseClaim> cachedClaims;
	
	// Comparator for returning descending order
	private static final Comparator<ExpenseClaim> DESC_ORDER =
		new Comparator<ExpenseClaim>() {
			public int compare(ExpenseClaim lhs, ExpenseClaim rhs) {
				return rhs.compareTo(lhs);
			};
		};
		
	public ExpenseClaimSortFilter(ExpenseClaimList source) {
		super(source);
		// Sort claims in descending order
		sortedClaims = new TreeSet<ExpenseClaim>(DESC_ORDER);
	}

	@Override
	public void addClaim(ExpenseClaim claim) {
		super.addClaim(claim);
		// Also add the claim to the local treeset
		sortedClaims.add(claim);
	}
	
	@Override
	public void removeClaim(ExpenseClaim claim) {
		super.removeClaim(claim);
		// Also remove the claim to the local treeset
		sortedClaims.remove(claim);
	}
	
	@Override
	public ExpenseClaim getClaim(int index) {
		// Use an array list to get an index
		return getClaims().get(index);
	}

	@Override
	public ArrayList<ExpenseClaim> getClaims() {
		// Cache the claims objects so getting objects is cheaper.
		if (cachedClaims == null) {
			cachedClaims = new ArrayList<ExpenseClaim>(sortedClaims);
		}
		return cachedClaims;
	}

	@Override
	public int getCount() {
		return sortedClaims.size();
	}

	@Override
	protected void onDatasetChanged() {
		super.onDatasetChanged();
		// Nullify the cache so it must be rebuilt.
		cachedClaims = null;
	}
}
