package ca.ualberta.cs.shinyexpensetracker.models;

/**
 * Represents an expense claim status.
 */
public enum Status {
	IN_PROGRESS("In Progress"), SUBMITTED("Submitted"), RETURNED("Returned"), APPROVED("Approved");

	private final String text;

	private Status(final String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	/**
	 * Converts from nice output to enum object
	 * @param text The human output to convert into a status
	 * @return the converted status
	 */
	public static Status fromString(String text) {
		if (text != null) {
			for (Status s : Status.values()) {
				if (text.equalsIgnoreCase(s.text)) {
					return s;
				}
			}
		}
		return null;
	}
}