package ca.ualberta.cs.shinyexpensetracker.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesPersistanceStrategy implements
		IPersistanceStrategy {
	private static final String preferencesFileName = "ShinyExpenseTracker-expenseClaims";
	private static final String preferenceKey = "expenseClaims";
	private final Context context;

	public SharedPreferencesPersistanceStrategy(Context context) {
		this.context = context;
	}

	@Override
	public void save(String value) {
		Editor editor = getSettings().edit();
		editor.putString(preferenceKey, value);
		editor.commit();
	}

	@Override
	public String load() {
		return getSettings().getString(preferenceKey, "");
	}

	private SharedPreferences getSettings() {
		return context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
	}
}
