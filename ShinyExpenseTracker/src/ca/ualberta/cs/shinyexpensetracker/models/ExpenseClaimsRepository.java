package ca.ualberta.cs.shinyexpensetracker.models;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Source: https://www.youtube.com/watch?v=gmNfc6u1qk0 (2015-01-31)
 * https://www.youtube.com/watch?v=uat-8Z6U_Co (2015-02-01)
 */
public class ExpenseClaimsRepository {
	private static final String preferencesFileName = "ShinyExpenseTracker-expenseClaims";
	private static final String preferenceKey = "expenseClaims";
	private static ExpenseClaimsRepository singleton = null;

	Context context;
	Gson gson;

	private ExpenseClaimsRepository(Context context) {
		this.context = context;
		this.gson = new Gson();
	}

	public static void initialize(Context context) {
		if (singleton == null) {
			if (context == null) {
				throw new RuntimeException(
						"Missing Context for ExpenseClaimsRepository.");
			}

			singleton = new ExpenseClaimsRepository(context);
		}
	}

	public ExpenseClaimList loadExpenseClaim() {
		String travelClaimsListData = getSettings().getString(preferenceKey, "");
		if (travelClaimsListData.equals("")) {
			return new ExpenseClaimList();
		} else {
			return gson.fromJson(travelClaimsListData, ExpenseClaimList.class);
		}
	}

	public void saveExpenseClaim(ExpenseClaimList travelClaims) {
		Editor editor = getSettings().edit();
		String travelClaimsString = gson.toJson(travelClaims);
		editor.putString(preferenceKey, travelClaimsString);
		editor.commit();
	}

	private SharedPreferences getSettings() {
		return context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
	}
}
