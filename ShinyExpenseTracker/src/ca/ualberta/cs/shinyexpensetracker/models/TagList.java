package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

public class TagList extends Model<TagList> {
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/* FIXME
	 * UML says addTag(String) and removeTag(String).
	 * Should this go into the TagController object?
	 */
	public void addTag(Tag t) {
		tags.add(t);
	}
	public void addTag(String s) {
		tags.add(new Tag(s));
	}
	
	public void removeTag(Tag t) {
		tags.remove(t);
	}

	public void removeTag(String s) {
		tags.remove(new Tag(s));
	}

	public int getCount() {
		return tags.size();
	}

	public Tag getTagById(int i) {
		return tags.get(i);
	}
}
