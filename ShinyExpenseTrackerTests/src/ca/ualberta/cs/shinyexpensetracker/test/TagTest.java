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
//
//	TagList tagList = new TagList();
//	
//	// corresponds to issue #25
//	public void testAddTag() {
//		Tag tag = new Tag("tag");
//		tagList.add(tag);
//		assertTrue("Expected tagList to be of size 1, tagList not of size 1", tagList.size == 1);
//		assertTrue("Expected to see tag added, tag not added", tagList.contains(tag));
//	}
//	
//	public void testGetTag(){
//		Tag tag = tagList.getTag("tag");
//		assertTrue("Expected Tag = tag, Tag = null", tag.getTag().equals("tag"));
//	}
//	
//	// corresponds to issue #27
//	public void testEditTag() {
//		Tag tag = tagList.getTag(tag);
//		tag.edit("changed tag");
//		assertTrue("Expected tag = changed tag, tag = tag", tagList.getTag(tag).equals("changed tag");
//	}
//	
//	// corresponds to issue #26
//	public void testRemoveTag() {
//		tagList.remove("tag");
//		assertTrue("Expected tagList size == 0, tagList size != 1", tagList.size() == 0);
//		assertTrue("Expected tagList doesn't containt 'tag'", tagList.contains("tag"));
//	}
//	
//	// corresponds to issue #23
//	public void testAssignTagToClaim(){
//		Claim claim = new Claim();
//		Tag tag = new tag("tag");
//		claim.addTag(tag);
//		assertTrue("tagList of claim is empty", claim.tagList.size() == 0);
//		assertTrue("tag not associated with claim", claim.getTag().equals(tag));
//	}
//	
//	// corresponds to issue #28
//	public void testFilterByTag(){
//		Claim claim = new Claim();
//		Tag tag = new tag("tag");
//		claim.addTag(tag);
//		ClaimList.filter(tag);
//		Adapter adapter = ((ListView) activity.findViewById(R.id.claimsList)).getAdapter();
//		int count = adapter.getCount;
//		assertTrue("filter incorrect amount of claims", count == 1);
//	}
//	
//	//corresponds to issue #24
//	public void testManageTags() {
//		Adapter adapter = ((ListView) activity.findViewById(R.id.tagsList)).getAdapter();
//		int count = adapter.getCount;
//		assertTrue("TagList not being displayed", count == 1);
//	}
//	
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
