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
public class ExpenseClaimList extends Model<ExpenseClaimList> {
	private ArrayList<ExpenseClaim> claims;
	
	/* FIXME
	 * UML says 0..* ExpenseClaimList's are composed of
	 * 			1 ExpenseClaimController,
	 * and that 1 ExpenseClaimList is composed of ? Expense Claims
	 */
	
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
	}

	// FIXME UML says this takes no args
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
	
	public int size(){
		return claims.size();
	}
	
	public ArrayList<ExpenseClaim> getClaims(){
		return claims;
	}
	
	public int getCount() {
		return this.claims.size();
	}

	public void sort() {
		Comparator<? super ExpenseClaim> reverse_compare = new Comparator<ExpenseClaim>() {
			public int compare(ExpenseClaim lhs, ExpenseClaim rhs) {
				return rhs.compareTo(lhs);
			};
		};
		Collections.sort(claims, reverse_compare);
	}
	
	/**
	 * Replaces the old claim with the new claim.
	 * @param oldClaim
	 * @param newClaim
	 */
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		int index = claims.indexOf(oldClaim);
		claims.set(index, newClaim);
	}
}
