package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

public interface ExpenseClaimListViewer {

	/**
	 * Returns a claim from the claimList based on index passed in
	 * 
	 * @param position The position of the claim to get
	 * @return
	 */
	abstract public ExpenseClaim getClaimAtPosition(int position);

	/**
	 * returns array list of claims
	 * 
	 * @return
	 */
	abstract public ArrayList<ExpenseClaim> getClaims();

	/**
	 * returns number of claims in claimList
	 * 
	 * @return
	 */
	abstract public int getCount();

}