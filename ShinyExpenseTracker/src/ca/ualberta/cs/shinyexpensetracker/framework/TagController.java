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
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.persistence.FilePersistenceStrategy;
import ca.ualberta.cs.shinyexpensetracker.persistence.GsonTagListPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.ITagListPersister;

/**
 * TagController object that stores a list
 * of created tags. It is unique for the
 * program
 * 
 * Covers Issue 25
 * @version 1.0
 * @since 2015-03-10
 */
public class TagController {
	private ITagListPersister persister;
	private TagList list;

	/**
	 * Default constructor.
	 * 
	 * Only use if you have a very good reason. Otherwise, just use
	 * Application.getTagController().
	 * 
	 * @param context The application's current context.
	 * @throws IOException
	 */
	public TagController(Context context) throws IOException {
		this(new GsonTagListPersister(
				new FilePersistenceStrategy(context, "tags")));
	}
	
	/**
	 * Constructor. Use for testing only.
	 * 
	 * @param context The application's current context.
	 * @throws IOException
	 */
	public TagController(ITagListPersister persister) throws IOException {
		this.persister = persister;
		list = persister.loadTags();
	}
	
	/**
	 * Add a tag to the current tag list
	 * <p>
	 * @param The tag object to add
	 * @return boolean stating if the tag was added
	 */
	public boolean addTag(Tag tag) throws IOException {
		boolean result = list.addTag(tag);
		persister.saveTags(list);
		return result;
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
	 * @throws IOException 
	 */
	public void removeTag(String s) throws IOException {
		list.removeTag(s);
		persister.saveTags(list);
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
	 * Will return the tag at the given index in the list
	 * @param index the index of the tag to get in the list
	 * @return the Tag at the given index
	 */
	public Tag getTag(int index){
		return list.getTagById(index);
	}
	


	

	/**
	 * Deletes a tag from the master tagList.
	 * Also deletes the tag from any claim lists tag list
	 * @param index
	 * @return
	 */
	public boolean deleteTag(Tag tag) throws IOException {
		boolean result = list.deleteTag(tag);
		Application.getExpenseClaimController().removeTag(tag);
		persister.saveTags(list);
		return result;
	}
	
	/**
	 * Edits a tag in the master Taglist
	 * @param index
	 * @param newTag
	 * @return
	 */
	public boolean editTag(int index, Tag newTag) throws IOException {
		boolean result = list.editTag(index, newTag);
		persister.saveTags(list);
		return result;
	}
	
	/** 
	 * Returns a list of all the tags that are in the tagController but
	 * not in the given tag list 
	 * @param tagList
	 * @return a tag list of the different tags 
	 */
	public TagList getTagsNotIn(TagList tagList){
		TagList differentTags = new TagList();
		for(Tag tag : list){
			if(!tagList.contains(tag)){
				differentTags.addTag(tag);
			}
		}
		return differentTags;
	}

}
