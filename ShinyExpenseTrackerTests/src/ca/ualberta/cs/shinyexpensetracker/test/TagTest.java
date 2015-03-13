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

public class TagTest extends TestCase {


	TagController tagController; 
	public void setUp(){
		tagController = TagController.getInstance();
		tagController.setTagList(new TagList());
	}
	
	public void testAddAndRemoveTag(){
		
		tagController.addTag("q1wert");
		assertEquals("failed to add a tag", tagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", tagController.getTagList().getTagById(0), new Tag("q1wert"));
		
		tagController.removeTag("q1wert");
		assertEquals("failed to remove a tag", tagController.getTagCount(), 0);
		
		tagController.addTag("1qwert");
		assertEquals("failed to add a tag", tagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", tagController.getTagList().getTagById(0), new Tag("1qwert"));
		
		tagController.removeTag("1qwert");
		assertEquals("failed to remove a tag", tagController.getTagCount(), 0);

		tagController.addTag("qwert");
		assertEquals("failed to add a tag", tagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", tagController.getTagList().getTagById(0), new Tag("qwert"));
		
		tagController.removeTag("qwert");
		assertEquals("failed to remove a tag", tagController.getTagCount(), 0);
		
		tagController.addTag("12345");
		assertEquals("failed to add a tag", tagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", tagController.getTagList().getTagById(0), new Tag("12345"));
		
		tagController.removeTag("12345");
		assertEquals("failed to remove a tag", tagController.getTagCount(), 0);
	}
	
	public void testAddInvalidTags(){
		String[] invalidTags = { ("!@#$%"), ("qwe rty"), ("qwe!@#$%rty"),
				("!@qwerty#$%"), (" qwerty "), (null), (""), (" "), ("\n") };
		for (String s : invalidTags) {
			tagController.addTag(s);
			
			//The tag list will still be empty because nothing should be added   
			assertEquals("should have discarded non-alphanumeric tag", tagController.getTagCount(), 0);
		}
	}
}
