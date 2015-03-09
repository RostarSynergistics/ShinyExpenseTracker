package ca.ualberta.cs.shinyexpensetracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * TagController object that stores a list
 * of created tags. It is unique for the
 * program
 * 
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
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
		if (s == null || s.equals(""))
			return;
		Pattern p = Pattern.compile("^\\w*$");
		Matcher m = p.matcher(s);
		if (m.matches())
			getTagList().addTag(s);
		else
			return;
	}
	
	/**
	 * Remove a tag from the current tag list
	 * <p>
	 * @param s the string value of the tag to be removed
	 */
	public static void removeTag(String s) {
		getTagList().removeTag(s);
	}
	
	/**
	 * Get the number of tags in the current list
	 * <p>
	 * @return the number of tags in the current list
	 */
	public static int getTagCount(){
		return getTagList().getCount();
	}
}
