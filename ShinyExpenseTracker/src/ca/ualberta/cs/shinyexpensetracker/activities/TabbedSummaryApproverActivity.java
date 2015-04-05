package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;

/**
 * Creates Approver specific menu items (Approve Claim, Return Claim and Comment) 
 * in the tabbedSummaryActivity
 */
public class TabbedSummaryApproverActivity extends TabbedSummaryActivity {

	protected static AlertDialog alertDialogAddComment;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary_approver, menu);
		m = menu;
		return true;
	}
	
	/**
	 * Called on MenuItem "Approve Claim" click
	 * @param menu
	 */
	public void approveClaimMenuItem(MenuItem menu) {
		
	}
	
	/**
	 * Called on MenuItem "Comment" click
	 * Opens a dialog box for the approver to enter a comment about the claim 
	 * @param menu
	 */
	public void commentMenuItem(MenuItem menu) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_comment_input, null);
		builder.setView(dialogView);
		final EditText commentTextBox = (EditText) dialogView
				.findViewById(R.id.EditTextDialogComment);

		// Set the correct text
		TextView dialogTextView = (TextView) dialogView
				.findViewById(R.id.TextViewDialogInputType);
		dialogTextView.setText("Comment:");

		// Setting the positive button to save the text in the dialog as a tag
		// if valid
		builder.setPositiveButton("Add Comment",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String comment = commentTextBox.getText().toString();

						
						Toast.makeText(TabbedSummaryApproverActivity.this, comment, Toast.LENGTH_SHORT).show();
					}
				});

		// Setting the negative button to close the dialog
		builder.setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		
		alertDialogAddComment = builder.create();
		alertDialogAddComment.show();
	}
	
	/**
	 * Called on MenuItem "Return Claim" click
	 * @param menu
	 */
	public void returnClaimMenuItem(MenuItem menu) {
		
	}
	
}
