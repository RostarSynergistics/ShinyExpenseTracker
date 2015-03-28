package ca.ualberta.cs.shinyexpensetracker.test;

import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseClaimActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests various parts of the functionality of ExpenseClaimActivity that relates to 
 * creating new ExpenseClaims.
 **/
public class EditExpenseClaimTests extends ActivityInstrumentationTestCase2<ExpenseClaimActivity> {
	ExpenseClaimController	controller;
	Instrumentation			instrumentation;
	ExpenseClaimActivity	activity;

	public EditExpenseClaimTests(Class<ExpenseClaimActivity> activityClass) {
		super(activityClass);
	}

	public EditExpenseClaimTests() {
		super(ExpenseClaimActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
}
