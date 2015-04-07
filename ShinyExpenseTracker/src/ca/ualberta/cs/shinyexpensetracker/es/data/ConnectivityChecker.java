// Code taken from: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
// Apr 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Object for checking if the application has network access
 */
public class ConnectivityChecker {

	/**
	 * Checks whether or not the device is connected to the internet.
	 * 
	 * @param context Android context to use to check for connectivity
	 * @return boolean Whether or not the network is available.
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
