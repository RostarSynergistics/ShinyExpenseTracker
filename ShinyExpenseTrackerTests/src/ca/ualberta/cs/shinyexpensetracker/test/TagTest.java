/* 
 * Test suite for adding and removing a tag
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
 */

package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;
import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import junit.framework.TestCase;


/**
 *
 * 
 * Covers Issue 25
 * Things to implement: proper navigation back to Manage Tags Activity
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class TagTest extends InstrumentationTestCase {

	TagController tagController; 
	public void setUp(){
		tagController = TagController.getInstance();
		// inject an empty list so that other things don't interfere.
		tagController.setTagList(new TagList());
	}
	
	public void testAddAndRemoveTag(){
		String[] tags = {"q1wert", "1qwert", "qwert", "12345"};
		Instrumentation instrumentation = getInstrumentation();
		
		assertEquals("Tag controller not empty: ", 0, tagController.getTagCount());
		
		for (final String t : tags) {
			Tag tag = new Tag(t);
			
			instrumentation.runOnMainSync(new Runnable() {
				@Override
				public void run() {
					tagController.addTag(t);
				}
			});
			assertEquals("failed to add a tag: " + t, 1, tagController.getTagCount());
			assertEquals("added a tag incorrectly", tag, tagController.getTagList().getTagById(0));

			instrumentation.runOnMainSync(new Runnable() {
				@Override
				public void run() {
					tagController.removeTag(t);
				}
			});
			
			assertEquals("failed to remove a tag: " + t, tagController.getTagCount(), 0);
		}
	}
	
	public void testAddInvalidTags(){
		String[] invalidTags = { ("!@#$%"), ("qwe rty"), ("qwe!@#$%rty"),
				("!@qwerty#$%"), (" qwerty "), (null), (""), (" "), ("\n") };
		Instrumentation instrumentation = getInstrumentation();
		for (final String s : invalidTags) {
			instrumentation.runOnMainSync(new Runnable() {
				
				@Override
				public void run() {
					tagController.addTag(s);					
				}
			});
			
			//The tag list will still be empty because nothing should be added   
			assertEquals("should have discarded non-alphanumeric tag: " + s, tagController.getTagCount(), 0);
		}
	}
}
