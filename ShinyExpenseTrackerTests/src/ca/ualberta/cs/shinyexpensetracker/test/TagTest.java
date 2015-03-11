package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.TagController;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import junit.framework.TestCase;

/**
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
 * 
 * Covers Issue 25
 * Things to implement: proper navigation back to Manage Tags Activity
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class TagTest extends TestCase {
	public void setUp(){
		TagController.getTagList();
	}
	
	public void testAddAndRemoveTag(){
		
		TagController.addTag("q1wert");
		assertEquals("failed to add a tag", TagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", TagController.getTagList().getTagById(0), new Tag("q1wert"));
		
		TagController.removeTag("q1wert");
		assertEquals("failed to remove a tag", TagController.getTagCount(), 0);
		
		TagController.addTag("1qwert");
		assertEquals("failed to add a tag", TagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", TagController.getTagList().getTagById(0), new Tag("1qwert"));
		
		TagController.removeTag("1qwert");
		assertEquals("failed to remove a tag", TagController.getTagCount(), 0);

		TagController.addTag("qwert");
		assertEquals("failed to add a tag", TagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", TagController.getTagList().getTagById(0), new Tag("qwert"));
		
		TagController.removeTag("qwert");
		assertEquals("failed to remove a tag", TagController.getTagCount(), 0);
		
		TagController.addTag("12345");
		assertEquals("failed to add a tag", TagController.getTagCount(), 1);
		assertEquals("added a tag incorrectly", TagController.getTagList().getTagById(0), new Tag("12345"));
		
		TagController.removeTag("12345");
		assertEquals("failed to remove a tag", TagController.getTagCount(), 0);
	}
	
	public void testAddInvalidTags(){
		TagController.addTag("!@#$%");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag("qwe rty");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag("qwe!@#$%rty");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag("!@qwerty#$%");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag(" qwerty ");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag(null);
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag("");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag(" ");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
		TagController.addTag("\n");
		assertEquals("should have discarded non-alphanumeric tag", TagController.getTagCount(), 0);
	}
}
