package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.activities.AddTagActivity;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Test suite for adding a tag from the corresponding activity view
 * 
 * Covers Issue 25
 * Things to implement: proper navigation back to Manage Tags Activity
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class AddTagActivityTest extends ActivityInstrumentationTestCase2<AddTagActivity> {

	AddTagActivity activity;
	public AddTagActivityTest() {
		super(AddTagActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}
	
	public void simulateUIInteraction(final String inputText){
		final EditText et = (EditText) activity.findViewById(R.id.tagName);
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				et.setText(inputText);
				Button b = (Button) activity.findViewById(R.id.addTagButton);
				b.performClick();
			}
		});
	}
	
	public void testAddValidTagFromUI(){
		
		simulateUIInteraction("q1wert");
		
		assertEquals("failed to add a tag", TagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", TagController.getTagList().getTagById(0), new Tag("q1wert"));
		
		TagController.removeTag("q1wert");
		assertEquals("failed to remove a tag", TagController.getTagCount(), 0);
	
		
	}
	
	public void testAddInvalidTagFromUI(){
		simulateUIInteraction("!@#$%");
		
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
	}
}
