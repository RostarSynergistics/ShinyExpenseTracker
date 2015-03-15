package ca.ualberta.cs.shinyexpensetracker.persistance;

import java.io.IOException;

/**
 * Defines an interface for classes that can handle the saving and loading
 * of a String value.
 */
public interface IPersistenceStrategy {
	/**
	 * Saves value.
	 * 
	 * @param value The value to save.
	 */
	public void save(String value) throws IOException;
	/**
	 * Loads the value;
	 * 
	 * @return The loaded value;
	 */
	public String load() throws IOException;
}
