package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class TagController {
	private static TagList list = new TagList();
	
	public static void addTag(String s) {
		list.addTag(s);
	}
	public static void removeTag(String s) {
		list.removeTag(s);
	}
	
	public static TagList getTagList(){
		return list;
	}
}
