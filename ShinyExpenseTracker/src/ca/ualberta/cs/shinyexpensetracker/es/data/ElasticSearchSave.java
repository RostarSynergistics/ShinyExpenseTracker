package ca.ualberta.cs.shinyexpensetracker.es.data;

import java.io.IOException;

import android.os.AsyncTask;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * This class will handle saving asynchronously with the use of the
 * ESClaimManager
 *
 */
public class ElasticSearchSave extends AsyncTask<ExpenseClaimList, Void, Boolean> {

	ESClaimManager manager = new ESClaimManager();

	@Override
	protected Boolean doInBackground(ExpenseClaimList... claimLists) {
		try {
			manager.insertClaimList(claimLists[0]);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}