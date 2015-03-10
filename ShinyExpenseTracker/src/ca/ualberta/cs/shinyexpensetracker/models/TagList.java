package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.IView;

public class TagList implements IModel<IView<TagList>> {
	private ArrayList<Tag> tags;
	
	private ArrayList<IView<TagList>> views ; 
	
	public TagList(){
		tags = new ArrayList<Tag>();
		views = new ArrayList<IView<TagList>>();
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/* FIXME
	 * UML says addTag(String) and removeTag(String).
	 * Should this go into the TagController object?
	 */
	public void addTag(Tag t) {
		tags.add(t);
		notifyViews();
	}
	public void addTag(String s) {
		tags.add(new Tag(s));
		notifyViews();
	}
	
	public void removeTag(Tag t) {
		tags.remove(t);
	}

	public void removeTag(String s) {
		tags.remove(new Tag(s));
	}
	
	public int size(){
		return tags.size();
	}

	@Override
	public void addView(IView<TagList> v) {
		views.add(v);
	}

	@Override
	public void removeView(IView<TagList> v) {
		// FIXME May crash if v is not in views
		views.remove(v);
	}

	@Override
	public void notifyViews() {
		for (IView<TagList> v : views) {
			v.update(this);
		}
	}
	
}
