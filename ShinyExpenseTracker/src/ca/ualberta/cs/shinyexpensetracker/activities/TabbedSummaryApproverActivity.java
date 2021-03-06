package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.ValidationErrorAlertDialog;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.Status;
import ca.ualberta.cs.shinyexpensetracker.utilities.InAppHelpDialog;

/**
 * Creates Approver specific menu items (Approve Claim, Return Claim and
 * Comment) in the tabbedSummaryActivity
 */
public class TabbedSummaryApproverActivity extends TabbedSummaryActivity {

	protected static AlertDialog alertDialogAddComment;
	protected static AlertDialog alertDialogApproveClaim;
	protected static ValidationErrorAlertDialog alertDialogApproveCommentNeeded;
	protected static AlertDialog alertDialogReturnClaim;
	protected static ValidationErrorAlertDialog alertDialogReturnCommentNeeded;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary_approver, menu);
		m = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help) {
			InAppHelpDialog.showHelp(this, R.string.help_tabbed_summary);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Called on MenuItem "Approve Claim" click Checks if claim has a comment
	 * attached to it. If so, claim is approved, otherwise, user is warned that
	 * they need to comment on the claim before they can approve it.
	 * 
	 * @param menu
	 * @throws IOException
	 * @throws ValidationException
	 */
	public void approveClaimMenuItem(MenuItem menu) throws IOException {
		adb = new AlertDialog.Builder(this);

		try {
			controller.updateExpenseClaimStatus(claimID, Status.APPROVED);
		} catch (ValidationException e) {
			alertDialogApproveCommentNeeded = new ValidationErrorAlertDialog(this, e);
			alertDialogApproveCommentNeeded.show();
		}

		adb.setMessage("The claim has been approved");

		adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		alertDialogApproveClaim = adb.create();
		alertDialogApproveClaim.show();

		// disable ability to return a claim
		m.getItem(2).setEnabled(false);
	}

	/**
	 * Called on MenuItem "Comment" click Opens a dialog box for the approver to
	 * enter a comment about the claim
	 * 
	 * @param menu
	 */
	@SuppressLint("InflateParams")
	public void commentMenuItem(MenuItem menu) {

		adb = new AlertDialog.Builder(this);

		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_comment_input, null);
		adb.setView(dialogView);
		final EditText commentTextBox = (EditText) dialogView.findViewById(R.id.EditTextDialogComment);

		adb.setMessage("Comment: ");

		// Setting the positive button to save the text in the dialog as a
		// comment
		// if valid

		adb.setPositiveButton("Add Comment", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String comment = commentTextBox.getText().toString();

				try {
					controller.addCommentToClaim(claimID, comment);
				} catch (IOException e) {
					throw new RuntimeException(e);

				}
			}
		});

		// Setting the negative button to close the dialog
		adb.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		alertDialogAddComment = adb.create();
		alertDialogAddComment.show();
	}

	/**
	 * Called on MenuItem "Return Claim" click
	 * 
	 * @param menu
	 * @throws IOException
	 * @throws ValidationException
	 */
	public void returnClaimMenuItem(MenuItem menu) throws IOException {
		adb = new AlertDialog.Builder(this);

		try {
			controller.updateExpenseClaimStatus(claimID, Status.RETURNED);
		} catch (ValidationException e) {
			alertDialogReturnCommentNeeded = new ValidationErrorAlertDialog(this, e);
			alertDialogReturnCommentNeeded.show();
		}

		adb.setMessage("The claim has been returned.");
		adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialogReturnClaim = adb.create();
		alertDialogReturnClaim.show();

		// disable ability to approve claim
		m.getItem(0).setEnabled(false);
	}

	/**
	 * Takes the user to a view comments list view to see all of the comments a
	 * claim has
	 */
	public void viewCommentsMenuItem(MenuItem menu) {
		intent = new Intent(TabbedSummaryApproverActivity.this, ViewCommentsActivity.class);
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claimID);
		startActivity(intent);
	}

	/*
	 * return dialog for testing purposes only
	 */
	public AlertDialog getCommentDialog() {
		return alertDialogAddComment;
	}

	public AlertDialog getApprovedDialog() {
		return alertDialogApproveClaim;
	}

	public ValidationErrorAlertDialog getCommentApproveNeededDialog() {
		return alertDialogApproveCommentNeeded;
	}

	public Dialog getClaimReturnedDialog() {
		return alertDialogReturnClaim;
	}

	public ValidationErrorAlertDialog getCommentNeededReturnDialog() {
		return alertDialogReturnCommentNeeded;
	}

}
