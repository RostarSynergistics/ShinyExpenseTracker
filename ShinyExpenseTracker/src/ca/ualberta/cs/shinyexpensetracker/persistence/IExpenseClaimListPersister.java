package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import android.content.Context;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * Defines an interface for classes that can handle the saving and loading 
 * of an ExpenseClaimList.
 */
public interface IExpenseClaimListPersister {

	/**
	 * Loads the ExpenseClaimList from file (or creates a new one if none exists)
	 * and returns it.
	 * 
	 * @return A loaded or new ExpenseClaimList.
	 * @throws IOException 
	 */
	public abstract ExpenseClaimList loadExpenseClaims() throws IOException;

	/**
	 * Saves an ExpenseClaimList to file.
	 * 
	 * @param list The ExpenseClaimList to save;
	 * @throws IOException 
	 */
	public abstract void saveExpenseClaims(ExpenseClaimList list)
			throws IOException;

}