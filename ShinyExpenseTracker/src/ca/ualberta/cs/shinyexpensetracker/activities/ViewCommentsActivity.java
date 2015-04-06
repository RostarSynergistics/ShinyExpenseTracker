package ca.ualberta.cs.shinyexpensetracker.activities;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ViewCommentsActivity extends Activity {

	private ExpenseClaimController controller;
	private Intent intent;
	private UUID claimID;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_comments);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_comments, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		controller = Application.getExpenseClaimController();

		intent = getIntent();
		claimID = (UUID) intent.getSerializableExtra(IntentExtraIDs.CLAIM_ID);
		final ExpenseClaim claim = controller.getExpenseClaimByID(claimID);

		final ListView comments_list = (ListView) findViewById(R.id.commentsListView);

		// Set the list view to receive updates from the model
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, claim.getComments());
		comments_list.setAdapter(adapter);

		if (claim.getComments().size() == 0) {
			comments_list.setVisibility(View.INVISIBLE);
			findViewById(R.id.noCommentsTextView).setVisibility(View.VISIBLE);
		} else {
			comments_list.setVisibility(View.VISIBLE);
			findViewById(R.id.noCommentsTextView).setVisibility(View.INVISIBLE);
		}
	};
}
