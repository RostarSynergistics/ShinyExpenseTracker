package ca.ualberta.cs.shinyexpensetracker.framework;

import java.io.IOException;

import android.content.Context;

/**
 * Serves as a Service Locator (http://en.wikipedia.org/wiki/Service_locator_pattern).
 * 
 * Source: http://stackoverflow.com/a/5114361/14064 (2015-03-15)
 */
public class Application extends android.app.Application {
	private static Context context;
	private static ExpenseClaimController expenseClaimController;
	
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
}
