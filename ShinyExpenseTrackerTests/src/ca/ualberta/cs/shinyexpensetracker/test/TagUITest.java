package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.activities.ManageTagActivity;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class TagUITest extends ActivityInstrumentationTestCase2<ManageTagActivity> {
	Activity activity;
	Instrumentation instrumentation;
	TagController tagController;
	ListView manageTagsListView;
	
	public TagUITest() {
		super(ManageTagActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		tagController = TagController.getInstance();
		tagController.setTagList(new TagList());
		activity = getActivity();
		instrumentation = getInstrumentation();
		manageTagsListView = (ListView) activity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.listViewManageTags);
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
		
		assertFalse("Tag is still the same",tagController.getTag(0).equals(tag));
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

	//Adds a tag through the add menu button and dialog
	private void addTagThroughDialog(String tagName) {
		instrumentation.invokeMenuActionSync(activity,ca.ualberta.cs.shinyexpensetracker.R.id.actionManageTagsAdd , 0);
		AlertDialog dialog = ManageTagActivity.getDialog();
		
		EditText dialogEditText = (EditText) dialog.findViewById(R.id.EditTextDialogTag);
		final Button dialogPostiveButton = (Button) dialog.getButton(dialog.BUTTON_POSITIVE);
		
		dialogEditText.setText(tagName);
		
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				dialogPostiveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
	}
	
	private void editTagThroughDialog(int pos, final String editedTagString) {
		clickListView(pos);
		
		AlertDialog dialog = ManageTagActivity.getEditDialog();
		
		final EditText dialogEditText = (EditText) dialog.findViewById(R.id.EditTextDialogTag);
		
		final Button dialogPostiveButton = (Button) dialog.getButton(dialog.BUTTON_POSITIVE);
		
		
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				dialogEditText.setText(editedTagString);
				dialogPostiveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
	}

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
	
	private void addTagToController(final Tag tag){
		instrumentation.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				tagController.addTag(tag);	
			}
		});
		instrumentation.waitForIdleSync();
	}
}


