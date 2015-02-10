package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.TestCase;

public class TagTest extends TestCase {

	TagList tagList = new TagList();
	
	public void addTagTest() {
		Tag tag = new Tag("tag");
		tagList.add(tag);
		assertTrue("Expected tagList to be of size 1, tagList not of size 1", tagList.size == 1);
		assertTrue("Expected to see tag added, tag not added", tagList.contains(tag));
	}
	
	public void getTagTest(){
		Tag tag = tagList.getTag("tag");
		assertTrue("Expected Tag = tag, Tag = null", tag.getTag().equals("tag"));
	}
	
	public void editTagTest() {
		Tag tag = tagList.getTag(tag);
		tag.edit("changed tag");
		assertTrue("Expected tag = changed tag, tag = tag", tagList.getTag(tag).equals("changed tag");
		
	}
	
	public void removeTagTest() {
		tagList.remove("tag");
		assertTrue("Expected tagList size == 0, tagList size != 1", tagList.size() == 0);
		assertTrue("Expected tagList doesn't containt 'tag'", tagList.contains("tag"));
		
	}
	
	
}
