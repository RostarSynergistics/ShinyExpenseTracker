package ca.ualberta.cs.shinyexpensetracker.models;

/**
 * Tag object that holds a string that represents a tag
 * 
 * Has value: String
 * 
 * @version 1.0
 * @since 2015-03-10
 */

public class Tag extends Model<Tag> {
	private String value;

	/**
	 * Takes a string which will represent the tag object
	 * 
	 * @param s
	 *            A string to represent the tag
	 */
	public Tag(String s) {
		this.value = s;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Tag))
			return false;
		else {
			String otherValue = ((Tag) other).getValue();
			return this.getValue().equals(otherValue);
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
