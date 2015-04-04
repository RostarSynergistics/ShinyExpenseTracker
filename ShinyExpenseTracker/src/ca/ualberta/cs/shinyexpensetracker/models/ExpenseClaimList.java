package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents the collection of ExpenseClaims that exist in the application.
 * 
 * As each part of the application should be working with the same list,
 * ExpenseClaimList uses the Singleton pattern.
 */
public class ExpenseClaimList extends AbstractExpenseClaimList {
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
	}

	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
		notifyViews();
	}
	
	public ArrayList<ExpenseClaim> getClaims() {
		return claims;
	}
	
	public int getCount() {
		return this.claims.size();
	}
	
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		int index = claims.indexOf(oldClaim);
		claims.set(index, newClaim);
	}

	/**
	 * sorts the claimList
	 */
	public void sort() {
		Comparator<? super ExpenseClaim> reverse_compare = new Comparator<ExpenseClaim>() {
			public int compare(ExpenseClaim lhs, ExpenseClaim rhs) {
				return rhs.compareTo(lhs);
			};
		};
		Collections.sort(claims, reverse_compare);
	}
}
