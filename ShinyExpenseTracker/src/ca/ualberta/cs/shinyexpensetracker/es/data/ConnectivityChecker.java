// Code taken from: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
// Apr 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChecker {
	
	/**
	 * Checks whether or not the device is connected to the internet. Returns appropriate booleans
	 * @param context
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
