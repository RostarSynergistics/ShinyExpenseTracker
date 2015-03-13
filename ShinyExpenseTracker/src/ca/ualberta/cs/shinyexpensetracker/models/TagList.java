package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagList extends Model<TagList> {
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public TagList(){
		tags = new ArrayList<Tag>();
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	
	public boolean addTag(Tag tag) {
		String tagString = tag.getValue();
		if(contains(tag)){
			return false;
		}
		if (tagString == null || tagString.equals("")){
			return false;
		}
		Pattern p = Pattern.compile("^\\w*$");
		Matcher m = p.matcher(tagString);
		if (m.matches()) {
			tags.add(tag);
			notifyViews();
		return true;
		}
		else{
			return false;
		}
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

	public int getCount() {
		return tags.size();
	}

	public Tag getTagById(int i) {
		return tags.get(i);
	}
	
	public boolean contains(Tag tag){
		return tags.contains(tag);
	}
}
