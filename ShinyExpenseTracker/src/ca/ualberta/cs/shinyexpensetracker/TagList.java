package ca.ualberta.cs.shinyexpensetracker;

import java.util.ArrayList;

public class TagList implements IModel {
	private ArrayList<Tag> tags;
	
	private ArrayList<IView> views; 
	
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

	@Override
	public void addView(IView<IModel> v) {
		views.add(v);
	}

	@Override
	public void removeView(IView<IModel> v) {
		// FIXME May crash if v is not in views
		views.remove(v);
	}

	@Override
	public void notifyViews() {
		for (IView v : views) {
			v.update(this);
		}
	}
	
}
