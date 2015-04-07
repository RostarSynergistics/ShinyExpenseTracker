package ca.ualberta.cs.shinyexpensetracker.framework;

import java.io.IOException;

import android.content.Context;
import ca.ualberta.cs.shinyexpensetracker.models.User;
import ca.ualberta.cs.shinyexpensetracker.persistence.FilePersistenceStrategy;
import ca.ualberta.cs.shinyexpensetracker.persistence.GsonUserPersister;
import ca.ualberta.cs.shinyexpensetracker.persistence.IUserPersister;

/**
 * Serves as a Service Locator
 * (http://en.wikipedia.org/wiki/Service_locator_pattern).
 * 
 * Source: http://stackoverflow.com/a/5114361/14064 (2015-03-15)
 */
public class Application extends android.app.Application {
	public enum ApplicationMode {
		Approver, Claimant
	}

	private static Context context;
	private static ExpenseClaimController expenseClaimController;
	private static TagController tagController;
	private static User user;
	private static IUserPersister userPersister;
	private static ApplicationMode currentMode = ApplicationMode.Claimant;

	@Override
	public void onCreate() {
		super.onCreate();
		Application.context = getApplicationContext();
		Application.userPersister = new GsonUserPersister(new FilePersistenceStrategy(context, "user"));
	}

	public static Context getAppContext() {
		return context;
	}

	public static void setUserPersister(IUserPersister userPersister) {
		Application.userPersister = userPersister;
	}

	private static IUserPersister getUserPersister() {
		if (Application.userPersister == null) {
			setUserPersister(new GsonUserPersister(new FilePersistenceStrategy(context, "user")));
		}

		return userPersister;
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
	 * Gets the application's ExpenseClaimController. If it has not yet been
	 * set, instantiate one using the default method.
	 */
	public static ExpenseClaimController getExpenseClaimController() {
		if (expenseClaimController == null) {
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
		if (tagController == null) {
			try {
				setTagController(new TagController(context));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return tagController;
	}

	public static void setUser(User u) throws IOException {
		getUserPersister().saveUser(u);
		user = u;
	}

	public static User getUser() throws IOException {
		if (user == null) {
			user = getUserPersister().loadUser();
		}

		return user;
	}

	public static boolean inClaimantMode() {
		return Application.currentMode == ApplicationMode.Claimant;
	}

	public static boolean inApproverMode() {
		return Application.currentMode == ApplicationMode.Approver;
	}

	public static void switchToClaimantMode() {
		Application.currentMode = ApplicationMode.Claimant;
	}

	public static void switchToApproverMode() {
		Application.currentMode = ApplicationMode.Approver;
	}
}
