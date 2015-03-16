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
		if(checkValidTag(tag)){
			tags.add(tag);
			notifyViews();
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Edits a tag in the tag list at a given position
	 * @param postion of tag to edit
	 * @param newTag to change the old tag into 
	 * @return boolean stating if change could be made
	 */
	public boolean editTag(int postion, Tag newTag){
		//Check for valid position
		if(postion >= tags.size()){
			return false;
		}
		
		//Check for valid tag
		if(checkValidTag(newTag)){
			tags.set(postion, newTag);
			notifyViews();
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Checks if the tag given is valid for this tag list e.g alphanumeric
	 * @param tag 
	 * @return boolean stating if the tag is vallid 
	 */
	private boolean checkValidTag(Tag tag) {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tags.size(); i++) {
			sb.append(tags.get(i));
		}
		return sb.toString();
	}
}
