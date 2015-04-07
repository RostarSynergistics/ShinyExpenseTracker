package ca.ualberta.cs.shinyexpensetracker.framework;

import java.io.IOException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.models.User;
import ca.ualberta.cs.shinyexpensetracker.models.User.Type;

/**
 * Serves as a Service Locator (http://en.wikipedia.org/wiki/Service_locator_pattern).
 * 
 * Source: http://stackoverflow.com/a/5114361/14064 (2015-03-15)
 */
public class Application extends android.app.Application {
	private static Context context;
	private static ExpenseClaimController expenseClaimController;
	private static TagController tagController;
	private static User user;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Application.context = getApplicationContext();
	}
	
	public static Context getAppContext() {
		return context;
	}
	
	/**
	 * Sets the application's ExpenseClaimController to a specific instance.
	 * 
	 * Use only for testing purposes.
	 * 
	 * @param controller
	 */
	public static void setExpenseClaimController(ExpenseClaimController controller) {
		expenseClaimController = controller;
	}
	
	/**
	 * Gets the application's ExpenseClaimController. If it has not yet been set,
	 * instantiate one using the default method.
	 */
	public static ExpenseClaimController getExpenseClaimController() {
		if(expenseClaimController == null) {
			try {
				setExpenseClaimController(new ExpenseClaimController(context));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return expenseClaimController;
	}

	/**
	 * Sets the application's TagController to a specific instance.
	 * 
	 * Use only for testing purposes.
	 * 
	 * @param controller
	 */
	public static void setTagController(TagController controller) {
		tagController = controller;
	}
	
	/**
	 * Gets the application's TagController. If it has not yet been set,
	 * instantiate one using the default method.
	 */
	public static TagController getTagController() {
		if(tagController == null) {
			try {
				setTagController(new TagController(context));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		return tagController;
	}
	
	public static void setUser(User u) {
		user = u;
	}
	
	public static User getUser() {
		if (user == null) {
			try {
				//setUser(new User(context));
				throw new IOException();
			} catch (IOException e) {
				user = new User();
			}
		}
		return user;
	}
	
	/**
	 * Sets the application's state (claimant or approver) when the user logs in
	 * @param state
	 */
	public static void setUserType(Type type) {
		getUser().setUserType(type);
	}
	
	/**
	 * Gets the application's user type (claimant or approver)
	 * @return
	 */
	public static Type getUserType() {
		return getUser().getUserType();
	}
}
