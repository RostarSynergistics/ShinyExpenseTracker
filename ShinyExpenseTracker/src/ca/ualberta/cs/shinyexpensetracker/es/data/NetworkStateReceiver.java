//Taken from: http://stackoverflow.com/questions/12157130/internet-listener-android-example
//April 5, 2015
package ca.ualberta.cs.shinyexpensetracker.es.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class NetworkStateReceiver extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	public void onReceive(Context context, Intent intent) {
		Log.d("app", "Network connectivity change");
		if (intent.getExtras() != null) {
			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
				Log.i("app", "Network " + ni.getTypeName() + " connected");
				ExpenseClaimList list = Application.getExpenseClaimController().getExpenseClaimList();
				ConnectivityChecker checker = new ConnectivityChecker();

				if (checker.isNetworkAvailable(context)) {
					new ElasticSearchSave().execute(list);
				}
			}
		}
		if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
			Log.d("app", "There's no network connectivity");

		}
	}
}