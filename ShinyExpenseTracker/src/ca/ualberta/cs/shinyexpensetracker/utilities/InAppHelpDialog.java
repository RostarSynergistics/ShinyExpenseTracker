package ca.ualberta.cs.shinyexpensetracker.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import ca.ualberta.cs.shinyexpensetracker.R;

/**
 * This class builds an in-app help dialog.
 * 
 * Example usage:
 * 	class MyActivity extends Activity {
 * 		...
 * 		void showHelp() {
 * 			InAppHelpDialog.showHelp(this, R.id.my_activity_help);
 * 		}
 */
public final class InAppHelpDialog {
	
	/**
	 * Creates and displays a help dialog given the resource ID
	 * and context.
	 * 
	 * @param context Android context to display on
	 * @param displayResourceID The string resource in R (/res/) to display
	 * @return the displayed dialog
	 */
	public static Dialog showHelp(Context context, int displayResourceID) {
		Dialog help = new AlertDialog.Builder(context)
			.setCancelable(false)
			.setMessage(displayResourceID)
			.setNeutralButton(android.R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.create();
		help.show();
		return help;
	}

	/**
	 * Shows default help given a context
	 * 
	 * @param context Android context to display on
	 * @return the displayed dialog
	 */
	public static Dialog showHelp(Context context) {
		return showHelp(context, R.string.default_help);
	}
}
