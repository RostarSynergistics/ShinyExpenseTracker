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
import junit.framework.TestCase;

public class AddTagUITest extends ActivityInstrumentationTestCase2<ManageTagActivity> {
	Activity activity;
	Instrumentation instrumentation;
	TagController tagController;
	ListView manageTagsListView;
	
	public AddTagUITest() {
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
	
	public void testDialogShows(){
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

	//Adds a tag through the add menu button and dialog
	private void addTagThroughDialog(String tagName) {
		instrumentation.invokeMenuActionSync(activity,ca.ualberta.cs.shinyexpensetracker.R.id.actionManageTagsAdd , 0);
		AlertDialog dialog = ManageTagActivity.getDialog();
		
		EditText dialogEditText = (EditText) dialog.findViewById(R.id.EditTextdialogAddTag);
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
	
}
