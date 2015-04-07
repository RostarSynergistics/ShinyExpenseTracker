package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.es.data.ConnectivityChecker;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchLoad;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSave;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSync;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * Decorates the GsonExpenseClaimListPersister to maintain the ElasticSearch
 * data while also maintaining the local copy of the data.
 */
public class ElasticSearchExpenseClaimListPersister implements IExpenseClaimListPersister {
	private final Context context;
	private final GsonExpenseClaimListPersister childPersister;
	private final ConnectivityChecker connectivityChecker;

	public ElasticSearchExpenseClaimListPersister(Context context, GsonExpenseClaimListPersister childPersister,
			ConnectivityChecker connectivityChecker) {
		this.context = context;
		this.childPersister = childPersister;
		this.connectivityChecker = connectivityChecker;
	}

	@Override
	public ExpenseClaimList loadExpenseClaims() throws IOException {
		ElasticSearchLoad load = new ElasticSearchLoad();

		// Creating the two lists to sync if necessary
		ExpenseClaimList localList = childPersister.loadExpenseClaims();

		// If a network is a available then get the server copy of the claim
		// list
		if (connectivityChecker.isNetworkAvailable(context)) {
			try {
				// Loading the data
				ExpenseClaimList cloudList = load.execute().get();

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
		childPersister.saveExpenseClaims(list);

		// Saving online if the network is available
		if (connectivityChecker.isNetworkAvailable(context)) {
			new ElasticSearchSave().execute(list);
		}
	}
}
