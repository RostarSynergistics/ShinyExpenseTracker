package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

/**
 * Defines an interface for classes that can handle the saving and loading 
 * of a TagList.
 */
public interface ITagListPersister {

	/**
	 * Loads the TagList from file (or creates a new one if none exists)
	 * and returns it.
	 * 
	 * @return A loaded or new TagList.
	 * @throws IOException 
	 */
	public abstract TagList loadTags() throws IOException;

	/**
	 * Saves an TagList to file.
	 * 
	 * @param list The TagList to save;
	 * @throws IOException 
	 */
	public abstract void saveTags(TagList list) throws IOException;
}