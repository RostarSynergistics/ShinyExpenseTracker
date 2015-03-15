package ca.ualberta.cs.shinyexpensetracker.persistance;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles the persistence of {@link ExpenseClaimList} to a file for offline usage.
 * 
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class ExpenseClaimListPersister {
	private final IPersistenceStrategy persistanceStrategy;
	private final Gson gson;

	/**
	 * Constructor. 
	 * 
	 * @param context The application's context.
	 */
	public ExpenseClaimListPersister(IPersistenceStrategy persistanceStrategy) {
		this.persistanceStrategy = persistanceStrategy;
		this.gson = new Gson();
	}

	/**
	 * Loads the ExpenseClaimList from file (or creates a new one if none exists)
	 * and returns it.
	 * 
	 * @return A loaded or new ExpenseClaimList.
	 */
	public ExpenseClaimList loadExpenseClaims() {
		String travelClaimsListData = persistanceStrategy.load();
		if (travelClaimsListData.equals("")) {
			return new ExpenseClaimList();
		} else {
			return gson.fromJson(travelClaimsListData, ExpenseClaimList.class);
		}
	}

	/**
	 * Saves an ExpenseClaimList to file.
	 * 
	 * @param list The ExpenseClaimList to save;
	 */
	public void saveExpenseClaims(ExpenseClaimList list) {
		String travelClaimsString = gson.toJson(list);
		persistanceStrategy.save(travelClaimsString);
	}
}
