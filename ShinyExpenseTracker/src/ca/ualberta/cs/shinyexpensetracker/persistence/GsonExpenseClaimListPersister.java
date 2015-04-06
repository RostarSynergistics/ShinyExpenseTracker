package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.es.data.ConnectivityChecker;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSave;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

import com.google.gson.Gson;

/**
 * Handles the persistence of {@link ExpenseClaimList} to a file for offline usage,
 * using Gson to serialize it.
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
	 * @param persistenceStrategy The desired persistence strategy.
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
		
		if(checker.isNetworkAvailable(context)){
			try {
				return load.execute().get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
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
		ConnectivityChecker checker = new ConnectivityChecker();
		Context context = Application.getAppContext();
		
		if(checker.isNetworkAvailable(context)){
			new ElasticSearchSave().execute(list);
		}
		
		
	}
	
	
	private class ElasticSearchLoad extends AsyncTask<Void, Void, ExpenseClaimList>{
		
		ESClaimManager manager = new ESClaimManager();
		
		@Override
		protected ExpenseClaimList doInBackground(Void... params) {
			list = manager.getClaimList();
			return manager.getClaimList();
		}
		@Override
		protected void onPostExecute(ExpenseClaimList result) {
			list = result;
		}
		public ExpenseClaimList getList() {
			return list;
		}
				
	}
	
}
