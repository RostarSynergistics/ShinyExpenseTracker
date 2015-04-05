package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

/**
 * Represents the collection of ExpenseClaims that exist in the application.
 * 
 * As each part of the application should be working with the same list,
 * ExpenseClaimList uses the Singleton pattern.
 */
public class ExpenseClaimList extends Model<ExpenseClaimList> implements ExpenseClaimListViewer {
	protected ArrayList<ExpenseClaim> claims;
	
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
	}

	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	public ArrayList<ExpenseClaim> getClaims() {
		return claims;
	}
	
	public int getCount() {
		return this.claims.size();
	}
	
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
		notifyViews();
	}
	
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		int index = claims.indexOf(oldClaim);
		claims.set(index, newClaim);
	}
}
