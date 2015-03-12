package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * Acts as an interface between an ExpenseClaim view and
 * the ExpenseClaimList model.
 * 
 * Use ExpenseClaimController.getInstance() to get the
 * singleton object.
 */
public class ExpenseClaimController {
	private static ExpenseClaimController instance;
	private ExpenseClaimList claimList;
	
	private ExpenseClaimController() {
		super();
		claimList = new ExpenseClaimList();
	}
	
	/**
	 * Returns the global instance of this controller class.
	 * 
	 * @return the unique instance of the controller class.
	 */
	public static ExpenseClaimController getInstance() {
		if (instance == null) {
			instance = new ExpenseClaimController();
		}
		return instance;
	}
	
	/**
	 * Used to override the automatically loaded claimList object.
	 * @param claimList
	 */
	public void setClaimList(ExpenseClaimList claimList) {
		this.claimList = claimList;
	}
	
	/**
	 * Uses the given exporter to save the requested claim.
	 * The exporter can be anything that implements the @link ClaimDataExporter
	 * interface.
	 * @param claim
	 * @param exporter
	 */
	public void saveExpenseClaim(ExpenseClaim claim, ClaimDataExporter exporter) {
		exporter.export(claim);	
	}
	
	/**
	 * Adds a claim to the ClaimList model
	 * @param claim
	 */
	public void addExpenseClaim(ExpenseClaim claim) {
		claimList.addClaim(claim);
	}
	
	/**
	 * Fetches a claim from the claim list model.
	 * @param index
	 * @return the claim at the given index
	 */
	public ExpenseClaim getExpenseClaim(int index){
		return claimList.getClaim(index);
	}
	
	/**
	 * Remove the claim from the claim list. 
	 * @param claim
	 */
	public void removeExpenseClaim(ExpenseClaim claim) {
		claimList.removeClaim(claim);
	}
	
	/**
	 * Replaces the old claim with the new claim
	 * @param oldClaim
	 * @param newClaim
	 */
	
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		claimList.updateExpenseClaim(oldClaim, newClaim);
	}

	/**
	 * @return the number of claims in the claim list
	 */
	public int getCount() {
		return claimList.getCount();
	}
	
	/**
	 * Sort the data on the model.
	 * Warning: This changes the indexes in the model.
	 */
	// XXX: May need to be changed. See #17 for details.
	public void sort() {
		claimList.sort();
	}
	
	/**
	 * Returns the model
	 * @return
	 */
	public ExpenseClaimList getExpenseClaimList() {
		return claimList;
	}
}
