package ca.ualberta.cs.shinyexpensetracker.models;

/**
 * This class is the superclass for all expense claim list
 * filters and decorators. 
 * 
 * Subclasses must implement the getters for claims and count
 * of claims.
 */
public abstract class ExpenseClaimFilter extends ExpenseClaimList {
	private ExpenseClaimList source;

	@Override
	public void addClaim(ExpenseClaim claim) {
		source.addClaim(claim);
	}

	@Override
	public void removeClaim(ExpenseClaim claim) {
		source.removeClaim(claim);
	}

	@Override
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		source.updateExpenseClaim(oldClaim, newClaim);
	}
	
}
