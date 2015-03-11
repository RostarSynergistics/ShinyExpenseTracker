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
 * Covers Issue 25
 * Things to implement: proper navigation back to Manage Tags Activity
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class AddTagActivityTest extends ActivityInstrumentationTestCase2<AddTagActivity> {

	AddTagActivity activity;
	TagController tagController = TagController.getInstance();
	
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
		
		assertEquals("failed to add a tag", tagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", tagController.getTagList().getTagById(0), new Tag("q1wert"));
		
		tagController.removeTag("q1wert");
		assertEquals("failed to remove a tag", tagController.getTagCount(), 0);
	
		
	}
	
	public void testAddInvalidTagFromUI(){
		simulateUIInteraction("!@#$%");
		
		assertEquals("should have discarded non-alphanumeric tag", tagController.getTagCount(), 0);
	}
}
