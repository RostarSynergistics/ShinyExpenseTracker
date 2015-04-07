package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.es.data.ConnectivityChecker;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchLoad;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSave;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSync;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
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
		ConnectivityChecker checker = new ConnectivityChecker();
		Context context = Application.getAppContext();
		ElasticSearchLoad load = new ElasticSearchLoad();

		// Creating the two lists to sync if necessary
		ExpenseClaimList localList;
		ExpenseClaimList cloudList;

		// Loading local data
		if (travelClaimsListData.equals("")) {
			localList = new ExpenseClaimList();
		} else {
			localList = gson.fromJson(travelClaimsListData, ExpenseClaimList.class);
		}

		// If a network is a avialable then get the server copy of the claim
		// list
		if (checker.isNetworkAvailable(context)) {
			try {
				// Loading the data
				cloudList = load.execute().get();

				// Syncing the two data sources in case of change
				ElasticSearchSync elasticSearchSync = new ElasticSearchSync(localList, cloudList);
				localList = elasticSearchSync.sync();

				// Saving the combined list
				saveExpenseClaims(localList);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return localList;

	}

	@Override
	public void saveExpenseClaims(ExpenseClaimList list) throws IOException {
		String travelClaimsString = gson.toJson(list);
		persistenceStrategy.save(travelClaimsString);
		ConnectivityChecker checker = new ConnectivityChecker();
		Context context = Application.getAppContext();

		// Saving online if the network is available
		if (checker.isNetworkAvailable(context)) {
			new ElasticSearchSave().execute(list);
		}

	}

}
