/*
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
 */


package ca.ualberta.cs.shinyexpensetracker;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * TagController object that stores a list
 * of created tags. It is unique for the
 * program
 * 
 * Covers Issue 25
 * @author Oleg Oleynikov 
 * @auther Rajan Jassal
 * @version 1.0
 * @since 2015-03-10
 */
public class TagController {
	private TagList list;
	private static TagController tagController;


	private TagController() {
		list = new TagList();
	}

	/**
	 * Creates an instance of TagController if one does not already exist. 
	 * If one does exist it is within the class it is returned
	 * @return A tag controller
	 */
	public static TagController getInstance() {
		if (tagController == null) {
			tagController = new TagController();
			return tagController;
		} else {
			return tagController;
		}
	}
	
	public void setTagList(TagList list) {
		this.list = list;
	}

	/**
	 * Add a tag to the current tag list
	 * <p>
	 * 
	 * @param s the string value of the added tag
	 */
	public boolean addTag(String s) {
		if (s == null || s.equals("")){
			return false;
		}
		Pattern p = Pattern.compile("^\\w*$");
		Matcher m = p.matcher(s);
		if (m.matches()) {
			list.addTag(s);
		return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Get the number of tags in the current list
	 * <p>
	 * @return the number of tags in the current list
	 */
	public int getTagCount() {
		return list.getCount();
	}

	/**
	 * Remove a tag from the current tag list
	 * <p>
	 * @param s the string value of the tag to be removed
	 */
	public void removeTag(String s) {
		list.removeTag(s);
	}
	
	/**
	 * Returns the current list of tags. If there is none,
	 * returns a new blank tag list.
	 * <p>
	 * @return the current tag list
	 */
	public TagList getTagList(){
		return list;
	}

}
