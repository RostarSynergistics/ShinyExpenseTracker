package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

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

	/**
	 * Returns a claim from the claimList based on index passed in
	 * 
	 * @param index
	 * @return
	 */
	public ExpenseClaim getClaimAtPosition(int index) {
		return claims.get(index);
	}

	/**
	 * Returns a claim from the list with the specified ID, if any.
	 * 
	 * @param id
	 *            The ID of the desired claim.
	 * @return The claim if it exists, or null otherwise.
	 */
	public ExpenseClaim getClaimByID(UUID id) {
		ExpenseClaim foundClaim = null;

		for (ExpenseClaim claim : claims) {
			if (claim.getID().equals(id)) {
				foundClaim = claim;
				break;
			}
		}

		return foundClaim;
	}

	/**
	 * Adds a claim to the claimList
	 * 
	 * @param claim
	 */
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}

	/**
	 * Deletes the claim with the specified ID.
	 * 
	 * @param id
	 *            The ID of the claim to delete.
	 */
	public void deleteClaim(UUID id) {
		ExpenseClaim claim = getClaimByID(id);

		if (claim != null) {
			claims.remove(claim);
			notifyViews();
		}
	}

	/**
	 * returns number of claims in claimList
	 * 
	 * @return
	 */
	public int size() {
		return claims.size();
	}

	/**
	 * returns array list of claims
	 * 
	 * @return
	 */
	public ArrayList<ExpenseClaim> getClaims() {
		return claims;
	}

	/**
	 * returns number of claims in claimList
	 * 
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
	 * Check if a claim list contains a claim of a given uuid 
	 * @param uuid of the claim
	 */
	public boolean containsUUID(UUID uuid){
		for(ExpenseClaim claim: claims){
			if(claim.getID().equals(uuid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a claim of a given UUID. 
	 * @param uuid of the given claim
	 * @return the claim of the given UUID. null if that claim does not exist
	 */
	public ExpenseClaim getClaim(UUID uuid){
		for(ExpenseClaim claim: claims){
			if(claim.getID().equals(uuid)){
				return claim;
			}
		}
		return null;
	}
}
