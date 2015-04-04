package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

public abstract class ExpenseClaimList extends Model<ExpenseClaimList> {
	protected ArrayList<ExpenseClaim> claims;

	public ExpenseClaimList() {
		super();
	}

	/**
	 * Returns a claim from the claimList based on index passed in
	 * @param index
	 * @return
	 */
	abstract public ExpenseClaim getClaim(int index);

	/**
	 * Adds a claim to the claimList
	 * @param claim
	 */
	abstract public void addClaim(ExpenseClaim claim);

	/**
	 * deletes a claim from the claimList
	 * @param claim
	 */
	abstract public void removeClaim(ExpenseClaim claim);

	/**
	 * returns array list of claims
	 * @return
	 */
	abstract public ArrayList<ExpenseClaim> getClaims();

	/**
	 * returns number of claims in claimList
	 * @return
	 */
	abstract public int getCount();

	/**
	 * Replaces the old claim with the new claim.
	 * @param oldClaim
	 * @param newClaim
	 */
	abstract public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim);

}