package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.R.id;
import ca.ualberta.cs.shinyexpensetracker.R.layout;
import ca.ualberta.cs.shinyexpensetracker.R.menu;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class AddTagToClaimActivity extends Activity implements IView<TagList> {

	private ListView manageTags;
	private ArrayAdapter<Tag> tagListAdapter;
	private TagController tagController;
	private ExpenseClaimController expenseClaimController;
	private int claimIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);

		manageTags = (ListView) findViewById(R.id.listViewManageTags);
		tagController = Application.getTagController();

		// Setting a listener for tag controller
		//tagController.getTagList().addView(this);
		
		expenseClaimController = Application.getExpenseClaimController();
		
		//Getting the current claim 
		claimIndex = getIntent().getIntExtra("claimIndex", -1);
				
		if (claimIndex == -1){
			throw new RuntimeException("Error getting current claim index");
		}
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_tag, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//Getting the correct tag list to display
		TagList claimTagList = expenseClaimController.getExpenseClaim(claimIndex).getTagList();
		TagList displayedTagList = tagController.getTagsNotIn(claimTagList);
		
		// Setting the list view
		tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_multiple_choice, displayedTagList.getTags());
		manageTags.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		TextView emptyText = (TextView)findViewById(R.id.TagListEmpty);
		manageTags.setEmptyView(emptyText);
		manageTags.setAdapter(tagListAdapter);
		
	}
		
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionManageTagsAdd:
			return addTagsToClaimDialog();

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private boolean addTagsToClaimDialog() {
		// Creating the dialog. Reusing the delete tag dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_delete_tag, null);
		builder.setView(dialogView);
		
		//Setting the header to the correct text
		TextView header = (TextView) dialogView.findViewById(R.id.TextViewDialogInputType);
		header.setText("Add selected tags to claims");
		
		//Setting the postive button. On click calls addTagsToClaim
		builder.setPositiveButton("Add tags", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				addTagsToClaim();
				finish();
			}
		});
	
		builder.setNegativeButton("Cancel" , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		builder.create().show();
		
		return true;
	}

	
	private void addTagsToClaim(){
		if (manageTags.getCheckedItemCount() == 0 ){
			Toast.makeText(AddTagToClaimActivity.this, "Error no items selected", Toast.LENGTH_LONG).show();
			return; 
		}
		
		//Returns an boolean array mapped to true or false for each postion that is clicked or not 
		SparseBooleanArray clicked = manageTags.getCheckedItemPositions();
		
	
		
		ExpenseClaim claim = expenseClaimController.getExpenseClaim(claimIndex);
		
		//Goes through the list and adds the tags that were checked
		for(int i = manageTags.getCount(); i >= 0; i--){
			//Checks the sparse boolean array for the tags clicked
			if(clicked.get(i)){
				Tag tag = (Tag) manageTags.getItemAtPosition(i);
				claim.addTag(tag);

			}
		}
		
	}
	

	@Override
	public void update(TagList m) {
		// TODO Auto-generated method stub
		tagListAdapter.notifyDataSetChanged();
	}
}
