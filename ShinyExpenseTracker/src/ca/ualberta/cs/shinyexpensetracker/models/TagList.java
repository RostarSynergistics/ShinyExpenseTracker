package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Keeps a list of tags.
 * 
 * Used as a master TagList for all tags in the system and by
 * claims individually to keeps a claim dependant list of tags associated 
 * with given claim
 * 
 * Has tags: ArrayList<Tag>
 *
 */
public class TagList extends Model<TagList> implements Iterable<Tag> {
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public TagList(){
		tags = new ArrayList<Tag>();
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * Adds a tag to the tag list
	 * @param tag The tag to add
	 * @return if it was added
	 */
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
	 * @param position of tag to edit
	 * @param newTag to change the old tag into 
	 * @return boolean stating if change could be made
	 */
	public boolean editTag(int position, Tag newTag){
		//Check for valid position
		if(position >= tags.size()){
			return false;
		}
		
		//Check for valid tag
		if(checkValidTag(newTag)){
			tags.set(position, newTag);
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
	
	/**
	 * Deletes a tag in the tag list at a given position
	 * @param postion of tag to delete
	 * @return boolean stating if change could be made
	 */
	public boolean deleteTag(int position){
		//Check for valid position
		if(position >= tags.size()){
			return false;
		}else {
			tags.remove(position);
			notifyViews();
			return true;
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
			sb.append("  ");
		}
		return sb.toString();
	}

	//Method required for iterable. Used to use TagList in a for each loop 
	@Override
	public Iterator<Tag> iterator() {
		return tags.iterator();
	}
}
