package ca.ualberta.cs.shinyexpensetracker.framework;

import java.io.IOException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.persistence.ExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.FilePersistenceStrategy;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;

/**
 * Acts as an interface between an ExpenseClaim view and
 * the ExpenseClaimList model.
 * 
 * Use ExpenseClaimController.getInstance() to get the
 * singleton object.
 */
public class ExpenseClaimController {
	private final IExpenseClaimListPersister persister;
	private ExpenseClaimList claimList;
	
	/**
	 * Default constructor.
	 * 
	 * Only use if you have a very good reason. Otherwise, just use
	 * Application.getExpenseClaimController().
	 * 
	 * @param context The application's current context.
	 * @throws IOException
	 */
	public ExpenseClaimController(Context context) throws IOException {
		this(new ExpenseClaimListPersister(
				new FilePersistenceStrategy(context, "expenseClaims")));
	}
	
	/**
	 * Constructor. Use for testing only.
	 * 
	 * @param context The application's current context.
	 * @throws IOException
	 */
	public ExpenseClaimController(IExpenseClaimListPersister persister) throws IOException {
		this.persister = persister;
		claimList = persister.loadExpenseClaims();
	}
	
	/**
	 * Adds a claim to the ClaimList model
	 * @param claim
	 */
	public void addExpenseClaim(ExpenseClaim claim) throws IOException {
		claimList.addClaim(claim);
		persister.saveExpenseClaims(claimList);
	}
	
	/**
	 * Fetches a claim from the claim list model.
	 * @param index
	 * @return the claim at the given index
	 */
	public ExpenseClaim getExpenseClaim(int index) {
		return claimList.getClaim(index);
	}
	
	/**
	 * Remove the claim from the claim list. 
	 * @param claim
	 */
	public void removeExpenseClaim(ExpenseClaim claim) throws IOException {
		claimList.removeClaim(claim);
		persister.saveExpenseClaims(claimList);
	}
	
	/**
	 * Replaces the old claim with the new claim
	 * @param oldClaim
	 * @param newClaim
	 */
	
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) 
	throws IOException {
		claimList.updateExpenseClaim(oldClaim, newClaim);
		persister.saveExpenseClaims(claimList);
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
	// XXX: May need to be changed. See #64 for details.
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
	
	public void addExpenseItem(ExpenseItem expense, int index)	{
		claimList.getClaim(index).addExpense(expense);
	}

	public int getIndexOf(ExpenseClaim claim) {
		return claimList.getClaims().indexOf(claim);
	}
}
