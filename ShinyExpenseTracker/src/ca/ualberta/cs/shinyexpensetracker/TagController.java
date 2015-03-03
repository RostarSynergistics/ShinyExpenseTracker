package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class TagController {
	private TagList list;
	
	public TagController() {
		list = new TagList();
	}
	public TagController(TagList list) {
		this.list = list;
	}
	
	public void addTag(String s) {
		list.addTag(s);
	}
	public void removeTag(String s) {
		list.removeTag(s);
	}
}
