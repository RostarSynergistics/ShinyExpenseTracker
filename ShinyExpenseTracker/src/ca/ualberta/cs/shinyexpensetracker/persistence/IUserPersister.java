package ca.ualberta.cs.shinyexpensetracker.persistence;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.User;

/**
 * Defines an interface for classes that can handle the saving and loading 
 * of a User.
 */
public interface IUserPersister {

	/**
	 * Loads the User from file (or creates a new one if none exists)
	 * and returns it.
	 * 
	 * @return A loaded or new TagList.
	 * @throws IOException 
	 */
	public abstract User loadUser() throws IOException;

	/**
	 * Saves an User to file.
	 * 
	 * @param user The user to save
	 * @throws IOException 
	 */
	public abstract void saveUser(User user) throws IOException;
}