package ca.ualberta.cs.shinyexpensetracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * TagController object that stores a list
 * of created tags. It is unique for the
 * program
 * Covers Issue 25
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-08
 */

public class TagController {
	private static TagList tagList;
	
	/**
	 * Returns the current list of tags. If there is none,
	 * returns a new blank tag list.
	 * <p>
	 * @return the current tag list
	 */
	static public TagList getTagList()
	{
		if (tagList == null)
		{
			tagList = new TagList();
		}
		return tagList;
	}
	
	// I don't think we need these if it is a singleton.
	/*
	public TagController() {
		tagList = new TagList();
	}
	
	public TagController(TagList list) {
		this.tagList = list;
	}
	*/
	

	/**
	 * Add a tag to the current tag list
	 * <p>
	 * @param s the string value of the added tag
	 */
	public static void addTag(String s) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
		Matcher m = p.matcher(s);
		if (m.matches())
			tagList.addTag(s);
		else
			return;
	}
	
	/**
	 * Remove a tag from the current tag list
	 * <p>
	 * @param s the string value of the tag to be removed
	 */
	public static void removeTag(String s) {
		tagList.removeTag(s);
	}
	
	/**
	 * Get the number of tags in the current list
	 * <p>
	 * @return the number of tags in the current list
	 */
	public static int getTagCount(){
		return tagList.getCount();
	}
}
