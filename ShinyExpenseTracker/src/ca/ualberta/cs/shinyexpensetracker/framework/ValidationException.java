package ca.ualberta.cs.shinyexpensetracker.framework;

/**
 * Thrown when a validation error is encountered.
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}
}
