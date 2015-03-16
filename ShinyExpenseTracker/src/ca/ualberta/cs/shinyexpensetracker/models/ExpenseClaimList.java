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
	
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
	}

	/**
	 * Returns a claim from the claimList based on index passed in
	 * @param index
	 * @return
	 */
	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	/**
	 * Adds a claim to the claimList
	 * @param claim
	 */
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	/**
	 * deletes a claim from the claimList
	 * @param claim
	 */
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
		notifyViews();
	}
	
	/**
	 * returns number of claims in claimList
	 * @return
	 */
	public int size(){
		return claims.size();
	}
	
	/**
	 * returns array list of claims
	 * @return
	 */
	public ArrayList<ExpenseClaim> getClaims(){
		return claims;
	}
	
	/**
	 * returns number of claims in claimList
	 * @return
	 */
	public int getCount() {
		return this.claims.size();
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
