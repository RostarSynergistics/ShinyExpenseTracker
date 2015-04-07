package ca.ualberta.cs.shinyexpensetracker.es.data;

import android.os.AsyncTask;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * This class will handle loading asynchronously with the use of the
 * ESClaimManager
 *
 */
public class ElasticSearchLoad extends AsyncTask<Void, Void, ExpenseClaimList> {

	ESClaimManager manager = new ESClaimManager();

	@Override
	protected ExpenseClaimList doInBackground(Void... params) {
		return manager.getClaimList();
	}

}