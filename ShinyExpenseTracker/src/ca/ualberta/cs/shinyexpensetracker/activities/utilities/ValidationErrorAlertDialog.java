package ca.ualberta.cs.shinyexpensetracker.activities.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;

public class ValidationErrorAlertDialog {
	private Dialog dialog;

	public ValidationErrorAlertDialog(Context context, ValidationException validationException) {
		this(context, validationException.getMessage());
	}

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

	public void show() {
		dialog.show();
	}
}
