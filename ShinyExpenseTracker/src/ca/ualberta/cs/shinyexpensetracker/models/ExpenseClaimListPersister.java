package ca.ualberta.cs.shinyexpensetracker.models;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles the persistence of {@link ExpenseClaimList} to a file for offline usage.
 * 
 * Uses a modified Singleton pattern; must be initialized with a {@link Context} first
 * before an instance can be supplied via {@link getInstance}.
 * 
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class ExpenseClaimListPersister {
	private static final String preferencesFileName = "ShinyExpenseTracker-expenseClaims";
	private static final String preferenceKey = "expenseClaims";
	private static ExpenseClaimListPersister singleton = null;

	Context context;
	Gson gson;

	/**
	 * Constructor. Private; use {@link getInstance} instead.
	 * 
	 * @param context The application's context.
	 */
	private ExpenseClaimListPersister(Context context) {
		this.context = context;
		this.gson = new Gson();
	}

	/**
	 * Initializes singleton with the application's current context;
	 * 
	 * @param context The application's context.
	 */
	public static void initialize(Context context) {
		if (singleton == null) {
			if (context == null) {
				throw new RuntimeException(
						"Missing Context for ExpenseClaimsRepository.");
			}

			singleton = new ExpenseClaimListPersister(context);
		}
	}
	
	/**
	 * Returns the instance of ExpenseClaimsListPersister if it
	 * has been properly initialized, and throws an exception otherwise.
	 * 
	 * @return The instance of ExpenseClaimsListPersister.
	 */
	public ExpenseClaimListPersister getInstance() {
		if (singleton == null) {
			throw new RuntimeException(
					"initialize(context) must be called first to supply a Context.");
		}
		
		return singleton;
	}

	/**
	 * Loads the ExpenseClaimList from file (or creates a new one if none exists)
	 * and returns it.
	 * 
	 * @return A loaded or new ExpenseClaimList.
	 */
	public ExpenseClaimList loadExpenseClaims() {
		String travelClaimsListData = getSettings().getString(preferenceKey, "");
		if (travelClaimsListData.equals("")) {
			return new ExpenseClaimList();
		} else {
			return gson.fromJson(travelClaimsListData, ExpenseClaimList.class);
		}
	}

	/**
	 * Saves an ExpenseClaimList to file.
	 * 
	 * @param travelClaims The ExpenseClaimList to save;
	 */
	public void saveExpenseClaims(ExpenseClaimList travelClaims) {
		Editor editor = getSettings().edit();
		String travelClaimsString = gson.toJson(travelClaims);
		editor.putString(preferenceKey, travelClaimsString);
		editor.commit();
	}

	private SharedPreferences getSettings() {
		return context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
	}
}
