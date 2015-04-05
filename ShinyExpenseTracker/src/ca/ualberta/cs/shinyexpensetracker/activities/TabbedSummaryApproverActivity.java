package ca.ualberta.cs.shinyexpensetracker.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;

/**
 * Creates Approver specific menu items (Approve Claim, Return Claim and Comment) 
 * in the tabbedSummaryActivity
 */
public class TabbedSummaryApproverActivity extends TabbedSummaryActivity {

	protected static AlertDialog alertDialogAddComment;
	protected static AlertDialog alertDialogApproveClaim;
	protected static AlertDialog alertDialogCommentNeeded;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary_approver, menu);
		m = menu;
		return true;
	}
	
	/**
	 * Called on MenuItem "Approve Claim" click
	 * Checks if claim has a comment attached to it.  If so, claim is approved,
	 * otherwise, user is warned that they need to comment on the claim before they 
	 * can approve it.
	 * @param menu
	 */
	public void approveClaimMenuItem(MenuItem menu) {
		if (controller.getExpenseClaim(claimIndex).getComments() == null) {
			adb.setMessage("You must comment on a claim before you can approve it");
			adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialogCommentNeeded = adb.create();
			alertDialogCommentNeeded.show();
		} else {
			controller.getExpenseClaim(claimIndex).setStatus(Status.APPROVED);
			adb.setMessage("The claim has been approved");
			adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			alertDialogApproveClaim = adb.create();
			alertDialogApproveClaim.show();
		}
	}
	
	/**
	 * Called on MenuItem "Comment" click
	 * Opens a dialog box for the approver to enter a comment about the claim 
	 * @param menu
	 */
	@SuppressLint("InflateParams")
	public void commentMenuItem(MenuItem menu) {
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_comment_input, null);
		adb.setView(dialogView);
		final EditText commentTextBox = (EditText) dialogView.findViewById(R.id.EditTextDialogComment);

		// Set the correct text
		TextView dialogTextView = (TextView) dialogView
				.findViewById(R.id.TextViewDialogInputType);
		dialogTextView.setText("Comment:");

		// Setting the positive button to save the text in the dialog as a comment
		// if valid
		adb.setPositiveButton("Add Comment",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String comment = commentTextBox.getText().toString();
						controller.getExpenseClaim(claimIndex).addComment(comment);
					}
				});

		// Setting the negative button to close the dialog
		adb.setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		
		alertDialogAddComment = adb.create();
		alertDialogAddComment.show();
	}
	
	/**
	 * Called on MenuItem "Return Claim" click
	 * @param menu
	 */
	public void returnClaimMenuItem(MenuItem menu) {
		
	}
	
	public AlertDialog getCommentDialog() {
		return alertDialogAddComment;
	}
	
	public AlertDialog getApprovedDialog() {
		return alertDialogApproveClaim;
	}
	
	public AlertDialog getCommentNeededDialog() {
		return alertDialogCommentNeeded;
	}
	
}
