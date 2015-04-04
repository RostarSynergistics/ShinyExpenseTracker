package ca.ualberta.cs.shinyexpensetracker.test.models;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * Test the tag list model
 * 
 *
 */
public class TagListTests extends TestCase {
	TagList tagList;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tagList = new TagList();
	}
	
	/**
	 * Tests tag lists ability to add a tag
	 */
	public void testAdd() {
		Tag tag = new Tag("test");
		
		assertEquals(tagList.size(), 0);
		tagList.addTag(tag);
		assertEquals(tagList.size(), 1);
		assertTrue(tagList.contains(tag));
	}
	
	/**
	 * Tests tag list's ability to edit a tag
	 */
	public void testEdit(){
		Tag tag = new Tag("test");
		Tag newTag = new Tag("new");
		
		tagList.addTag(tag);
		
		assertEquals(tagList.size(), 1);
		assertTrue(tagList.contains(tag));
		
		tagList.editTag(0, newTag);
		
		assertEquals(tagList.size(), 1);
		assertTrue(tagList.contains(newTag));
	}
	
	/**
	 * Test tag list's ability to remove a tag
	 */
	public void testDelete(){
		Tag tag = new Tag("test");
		tagList.addTag(tag);
		
		assertEquals(tagList.size(), 1);
		
		tagList.removeTag(tag);
		
		assertEquals(tagList.size(), 0);
		assertFalse(tagList.contains(tag));
	}
	
	
}
