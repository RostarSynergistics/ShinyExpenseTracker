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


package ca.ualberta.cs.shinyexpensetracker.framework;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
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
	


	
	/**
	 * Add a tag to the current tag list
	 * <p>
	 * @param The tag object to add
	 * @return boolean stating if the tag was added
	 */
	public boolean addTag(Tag tag){
		return list.addTag(tag);
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
	 *@param s the string value of the tag to be removed
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
	
	/**
	 * Returns true if the string given matches any tags 
	 * already in the list 
	 * <p>
	 * @return boolean if the string is in TagList 
	 */
	public boolean inTagList(Tag tag){
		return list.contains(tag);
		
	}
	
	/**
	 * Sets a new tag list for the singleton object
	 * @param tagList a new tag list for the singleton
	 */
	public void setTagList(TagList tagList){
		list = tagList;
	}
	
	/**
	 * Will return the tag at the given index in the list
	 * @param index the index of the tag to get in the list
	 * @return the Tag at the given index
	 */
	public Tag getTag(int index){
		return list.getTagById(index);
	}
	
	public boolean deleteTag(int index){
		return list.deleteTag(index);
	}
	
	public boolean editTag(int index, Tag newTag){
		return list.editTag(index, newTag);
	}

}
