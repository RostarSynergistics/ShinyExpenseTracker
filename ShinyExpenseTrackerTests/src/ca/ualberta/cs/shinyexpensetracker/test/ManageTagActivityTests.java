package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ManageTagActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockTagListPersister;

public class ManageTagActivityTests extends ActivityInstrumentationTestCase2<ManageTagActivity> {
	ManageTagActivity activity;
	Instrumentation instrumentation;
	TagController tagController;
	ListView manageTagsListView;
	
	public ManageTagActivityTests() {
		super(ManageTagActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();

		tagController = new TagController(new MockTagListPersister());
		Application.setTagController(tagController);
		
		activity = (ManageTagActivity)getActivity();
		instrumentation = getInstrumentation();
		manageTagsListView = (ListView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.listViewManageTags);
		
		ExpenseClaim claim = new ExpenseClaim("TEST");
	}
	
	public void testAddDialogShows(){
		//Press the add button in the menu bar. Should create a dialog 
		instrumentation.invokeMenuActionSync(activity,ca.ualberta.cs.shinyexpensetracker.R.id.actionManageTagsAdd , 0);
		AlertDialog dialog = ManageTagActivity.getDialog();
		assertTrue(dialog.isShowing());
	}

	

	public void testAddingToDialog(){
		Tag tag = new Tag("TEST");
		addTagThroughDialog(tag.getValue());
		assertTrue("Tag not added correctly",tagController.inTagList(tag));
		
		tag = new Tag("FAI LED");
		addTagThroughDialog(tag.getValue());
		assertFalse("Non alphanumeric tag added" ,tagController.inTagList(tag));
		tag = new Tag("!@#$%");
		addTagThroughDialog(tag.getValue());
		assertFalse("Non alphanumeric tag added",tagController.inTagList(tag));
	}
	
	public void testEditDialogShows(){
		addTagToController(new Tag("TEST"));
		assertEquals(tagController.getTagCount() , 1);
		
		//Clicking the postion of the newly added tag to see if the dialog shows
		clickListView(0);
		AlertDialog dialog = ManageTagActivity.getEditDialog();
		assertTrue(dialog.isShowing());
	}
	
	public void testEditTag(){
		assertTrue(tagController.getTagCount() == 0);
		Tag tag = new Tag("TagToEdit");
		addTagToController(tag);
		assertTrue(tagController.getTagCount() == 1);
		

		editTagThroughDialog(0, "EditedTag");
		assertTrue("Number of tags did not stay the same" ,tagController.getTagCount() == 1);
		
		assertFalse("Tag is still the same",tagController.getTag(0).equals(new Tag("TagToEdit")));
		assertEquals("Tag does no have the correct value" ,tagController.getTag(0).getValue(),"EditedTag");
		
		editTagThroughDialog(0, "Edited Tag");
		assertTrue("Number of tags did not stay the same" ,tagController.getTagCount() == 1);
		assertFalse("Non alphanumeric tag added", tagController.getTag(0).getValue().equals("Edited Tag"));
		assertEquals("Tag does no have the correct value" ,tagController.getTag(0).getValue(),"EditedTag");
		
		editTagThroughDialog(0, "#@!^");
		assertTrue("Number of tags did not stay the same" ,tagController.getTagCount() == 1);
		assertFalse("Non alphanumeric tag added", tagController.getTag(0).getValue().equals("#@!^"));
		assertEquals("Tag does no have the correct value" ,tagController.getTag(0).getValue(),"EditedTag");
	}
	
	public void testDeleteDialogShows(){
		addTagToController(new Tag("TEST"));
		
		//Simulate a long click to show delete dialog
		activity.deleteTagFromDialog(0);
		AlertDialog dialog = ManageTagActivity.getDeleteDialog();
		assertTrue(dialog.isShowing());
	}
	
	public void testDeleteTag(){
		assertEquals(tagController.getTagCount(),0);
		addTagToController(new Tag("TEST"));
		assertEquals(tagController.getTagCount(),1);
		
		//Test to see if tag is deleted
		deleteTagThroughDialog(0);
		assertEquals("Tag was not deleted",tagController.getTagCount(),0);
		
		//Test to see if the right tag is deleted 
		Tag testTag = new Tag("TEST2");
		addTagToController(new Tag("TEST1"));
		addTagToController(testTag);
		assertEquals(tagController.getTagCount(),2);
		deleteTagThroughDialog(0);
		assertEquals("Tag not deleted", tagController.getTagCount(),1);
		assertEquals("Wrong tag deleted",tagController.getTag(0), testTag);
		
	}

	//Adds a tag through the add menu button and dialog
	private void addTagThroughDialog(String tagName) {
		instrumentation.invokeMenuActionSync(activity,ca.ualberta.cs.shinyexpensetracker.R.id.actionManageTagsAdd , 0);
		AlertDialog dialog = ManageTagActivity.getDialog();
		
		EditText dialogEditText = (EditText) dialog.findViewById(R.id.EditTextDialogTag);
		final Button dialogPostiveButton = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		
		dialogEditText.setText(tagName);
		
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				dialogPostiveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
	}
	
	//Edits a tag through the listview click and dialog
	private void editTagThroughDialog(int pos, final String editedTagString) {
		clickListView(pos);
		
		AlertDialog dialog = ManageTagActivity.getEditDialog();
		final EditText dialogEditText = (EditText) dialog.findViewById(R.id.EditTextDialogTag);
		final Button dialogPostiveButton = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				dialogEditText.setText(editedTagString);
				dialogPostiveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
	}
	
	//Deletes a tag through the listview click and dialog
	private void deleteTagThroughDialog(final int pos){
		instrumentation.runOnMainSync(new Runnable() {
		@Override
			public void run() {
			activity.deleteTagFromDialog(pos);
			AlertDialog dialog = ManageTagActivity.getDeleteDialog();
			
			final Button dialogPostiveButton = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
			dialogPostiveButton.performClick();
			}	
		});
		instrumentation.waitForIdleSync();
	}
	
	//Clicks the list view at given position
	private void clickListView(int pos) {
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				manageTagsListView.performItemClick(manageTagsListView.getAdapter().getView(0, null, null), 
				0, manageTagsListView.getAdapter().getItemId(0));
			}
		});
		instrumentation.waitForIdleSync();
	}
	
	//Adds tag to the the tag controller 
	private void addTagToController(final Tag tag){
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				try {
					tagController.addTag(tag);
				} catch (IOException e) {
					fail();
				}	
			}
		});
		instrumentation.waitForIdleSync();
	}
}


