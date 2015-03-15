package ca.ualberta.cs.shinyexpensetracker.persistance;

/**
 * Defines an interface for classes that can handle the saving and loading
 * of a String value
 */
public interface IPersistanceStrategy {
	public void save(String value);
	public String load();
}
