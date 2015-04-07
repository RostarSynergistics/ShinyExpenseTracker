package ca.ualberta.cs.shinyexpensetracker.activities.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;

/**
 * Generalized error dialog. Extracts the dialog building
 * for errors to one class. Usually used after catching
 * a ValidationException.
 * 
 * Example:
 *  public class MyActivity {
 *  ...
 *  void tryParseData() {
 * 		try {
 * 			parseInputData()
 * 		} catch (ValidationEception e) {
 * 			new ValidationErrorAlertDialog(this, e).show();
 * 		}
 * 	}
 *
 */
public class ValidationErrorAlertDialog {
	private Dialog dialog;
	private boolean isShowing = false;

	/**
	 * Creates an alert dialog indicating an error has occurred.
	 * @param context The android context that the alert can bind to
	 * @param validationException The exception that led to the alert.
	 */
	public ValidationErrorAlertDialog(Context context, ValidationException validationException) {
		this(context, validationException.getMessage());
	}

	/**
	 * Creates an alert dialog indicating an error has occurred.
	 * @param context The android context that the alert can bind to
	 * @param messages String or strings. Messages will be concatenated.
	 */
	public ValidationErrorAlertDialog(Context context, String... messages) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		for (String message : messages) {
			builder.setMessage(message);
		}

		builder.setCancelable(true);

		builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog = builder.create();
	}

	/**
	 * Shows the alert dialog.
	 */
	public void show() {
		dialog.show();
		isShowing = true;
	}

	public boolean isShowing() {
		return isShowing;
	}
}
