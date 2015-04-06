package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.utilities.InAppHelpDialog;

public class RemoveTagFromClaimActivity extends Activity implements IView<TagList> {

	private ListView manageTags;
	private ArrayAdapter<Tag> tagListAdapter;
	private ExpenseClaimController expenseClaimController;
	private int claimIndex;
	private static AlertDialog removeTags;
	private Button done;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);
		
		//Setting up needed resources
		manageTags = (ListView) findViewById(R.id.listViewManageTags);
		expenseClaimController = Application.getExpenseClaimController();
		done = (Button) findViewById(R.id.doneButtonManageTags);
		
		//Getting the current claim 
		 claimIndex = getIntent().getIntExtra("claimIndex", -1);
		//Intent error 
		if (claimIndex == -1){
			throw new RuntimeException("Error getting current claim index");
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//Getting the correct tag list to display
		TagList claimTagList = expenseClaimController.getExpenseClaim(claimIndex).getTagList();
		
		// Setting the list view
		tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_multiple_choice, claimTagList.getTags());
		manageTags.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		//Setting default message for an empty list
		TextView emptyText = (TextView)findViewById(R.id.TagListEmpty);
		emptyText.setText("No tags to remove");
		manageTags.setEmptyView(emptyText);
		manageTags.setAdapter(tagListAdapter);
		done.setText("Remove Tags");
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeTagsFromClaimDialog();
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_tag, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help) {
			InAppHelpDialog.showHelp(this, R.string.help_remove_tag);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Creates a dialog to confirm the adding of selected 
	 * tags to the current claim 
	 * @return true if added correctly 
	 */
	private boolean removeTagsFromClaimDialog() {
		// Creating the dialog. Reusing the delete tag dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_delete_tag, null);
		builder.setView(dialogView);
		
		//Setting the header to the correct text
		TextView header = (TextView) dialogView.findViewById(R.id.TextViewDialogInputType);
		header.setText("Delete selected tags of the claim?");
		
		//Setting the positive button. On click calls addTagsToClaim
		builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeTagsFromClaim();
				
				finish();
			}
		});
	
		//Set the negative button to do nothing 
		builder.setNegativeButton("Cancel" , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		removeTags = builder.create();
		removeTags.show();
		
		return true;
	}
	
	//Adds the check marked tags to the current claim
		private void removeTagsFromClaim() {
			if (manageTags.getCheckedItemCount() == 0 ){
				Toast.makeText(RemoveTagFromClaimActivity.this, "No items selected", Toast.LENGTH_LONG).show();
				return; 
			}
			
			//Returns an boolean array mapped to true or false for each postion that is clicked or not 
			SparseBooleanArray clicked = manageTags.getCheckedItemPositions();
			
			ExpenseClaim claim = expenseClaimController.getExpenseClaim(claimIndex);
			
			//Goes through the list and removes the tags that were checked
			for(int i = manageTags.getCount(); i >= 0; i--){
				//Checks the sparse boolean array for the tags clicked
				if(clicked.get(i)){
					Tag tag = (Tag) manageTags.getItemAtPosition(i);
					claim.removedTag(tag);
				}
			}
			
			try {
				expenseClaimController.update();
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		}
		
	public static AlertDialog getDialog(){
		return removeTags;
	}
		
	@Override
	public void update(TagList m) {
	
	}
	
	
}
