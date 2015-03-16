/* 
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ca.ualberta.cs.shinyexpensetracker.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * Activity that handles viewing all the tags and also allows movement to add, edit and delete tags 
 * Covers Issue #24, 74
 * @author Rajan Jassal
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-10
 */
public class ManageTagActivity extends Activity implements IView<TagList> {
	
	private ListView manageTags;
	private ArrayAdapter<Tag> tagListAdapter;
	private TagController tagController;
	protected static AlertDialog alertDialogAddTag;
	protected static AlertDialog alertDialogEditTag;
	protected static AlertDialog alertDialogDeleteTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tag);
		manageTags = (ListView)findViewById(R.id.listViewManageTags);
		tagController = TagController.getInstance();
		
		//Setting a listener for tag controller
		tagController.getTagList().addView(this);
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
		
		//Setting the list view
		tagListAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, tagController.getTagList().getTags());
		manageTags.setAdapter(tagListAdapter);
		
		manageTags.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				editTagFromDialog(position);
			}
		
		});
		
		manageTags.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				deleteTagFromDialog(position);
				return true;
			}
		
		});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionManageTagsAdd:
			return addTagFromDialogue();

		default:
			return super.onOptionsItemSelected(item);
		}
	
		
	}

	/**
	 * Extracted method from onOptionsItemSelected that shows a
	 * dialog to add a tag if the menu button is pressed
	 * @return true to indicate the function of the menu button has been achieved 
	 */
	private boolean addTagFromDialogue() {
		//Creating the dialog from a custom layout and getting all the widgets needed 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_tag_input, null);
		builder.setView(dialogView);
		final EditText tagNameTextBox = (EditText)dialogView.findViewById(R.id.EditTextDialogTag);
		
		//Set the correct text
		TextView dialogTextView = (TextView) dialogView.findViewById(R.id.TextViewDialogInputType);
		dialogTextView.setText("Name for the new Tag:");
		
		//Setting the positive button to save the text in the dialog as a tag if valid
		builder.setPositiveButton("Add Tag", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String tagName = tagNameTextBox.getText().toString();
				Tag tag = new Tag(tagName);
				if(tagController.inTagList(tag)){
					Toast.makeText(getApplicationContext(), "Tag already exists", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (!tagController.addTag(tag)){
					Toast.makeText(getApplicationContext(), "Tag name has to be an alphanumeric value.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Setting the negative button to close the dialog
		builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		alertDialogAddTag = builder.create();
		alertDialogAddTag.show();
		
		return true;
	}
	
	/**
	 * Extracted method from the listview onClickListener
	 * dialog to edit a tag if the tag is pressed from the listview
	 * @param pos of the list view clicked
	 */
	private void editTagFromDialog(final int pos){
		//Creating the dialog from a custom layout and getting all the widgets needed 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_tag_input, null);
		builder.setView(dialogView);
		
		
		//Set the correct text
		TextView dialogTextView = (TextView) dialogView.findViewById(R.id.TextViewDialogInputType);
		dialogTextView.setText("Edit tag name:");
		
		//Displaying the old tag name to edit
		final EditText tagNameTextBox = (EditText)dialogView.findViewById(R.id.EditTextDialogTag);
		tagNameTextBox.setText(tagController.getTag(pos).getValue());
		
		//Setting the buttons
		builder.setPositiveButton("Edit Tag", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String tagName = tagNameTextBox.getText().toString();
				Tag tag = new Tag(tagName);
				if(tagController.inTagList(tag)){
					Toast.makeText(getApplicationContext(), "Tag already exists", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (!tagController.editTag(pos, tag)){
					Toast.makeText(getApplicationContext(), "Tag name has to be an alphanumeric value.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		alertDialogEditTag = builder.create();
		alertDialogEditTag.show();
		
	}
	
	/**
	 * Extracted method from the listview onLongItemClickListener
	 * dialog to delete a tag if the tag is long pressed from the listview
	 * @param pos of the list view clicked
	 */
	public void deleteTagFromDialog(final int position){
		//Creating a delete dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = this.getLayoutInflater();
		View dialogView = layoutInflater.inflate(R.layout.dialog_delete_tag, null);
		builder.setView(dialogView);
		

		//Setting buttons
		builder.setPositiveButton("Delete Tag", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!tagController.deleteTag(position)){
					Toast.makeText(getApplicationContext(), "Tag could not be deleted", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		alertDialogDeleteTag = builder.create();
		alertDialogDeleteTag.show();
		
	}
	
	@Override
	public void update(TagList m) {
		tagListAdapter.notifyDataSetChanged();
	}
	
	public static AlertDialog getDialog(){
		return alertDialogAddTag;
	}
	
	public static AlertDialog getEditDialog(){
		return alertDialogEditTag;
	}

	public static AlertDialog getDeleteDialog(){
		return alertDialogDeleteTag;
	}
}
