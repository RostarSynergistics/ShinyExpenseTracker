package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

import com.google.gson.Gson;

/**
 * Handles the persistence of {@link ExpenseClaimList} to a file for offline
 * usage, using Gson to serialize it.
 * 
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class GsonExpenseClaimListPersister implements IExpenseClaimListPersister {
	private final IPersistenceStrategy persistenceStrategy;
	private final Gson gson;
	ExpenseClaimList list;

	/**
	 * Constructor.
	 * 
	 * @param persistenceStrategy
	 *            The desired persistence strategy.
	 */
	public GsonExpenseClaimListPersister(IPersistenceStrategy persistenceStrategy) {
		this.persistenceStrategy = persistenceStrategy;
		this.gson = new Gson();
	}

	@Override
	public ExpenseClaimList loadExpenseClaims() throws IOException {
		String travelClaimsListData = persistenceStrategy.load();

		if (travelClaimsListData.equals("")) {
			return new ExpenseClaimList();
		} else {
			return gson.fromJson(travelClaimsListData, ExpenseClaimList.class);
		}
	}

	@Override
	public void saveExpenseClaims(ExpenseClaimList list) throws IOException {
		String travelClaimsString = gson.toJson(list);
		persistenceStrategy.save(travelClaimsString);
	}
}
