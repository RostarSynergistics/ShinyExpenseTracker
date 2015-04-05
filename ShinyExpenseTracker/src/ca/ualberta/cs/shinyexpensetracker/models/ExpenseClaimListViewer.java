package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

public interface ExpenseClaimListViewer {

	/**
	 * Returns a claim from the claimList based on index passed in
	 * @param index
	 * @return
	 */
	abstract public ExpenseClaim getClaim(int index);

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

}