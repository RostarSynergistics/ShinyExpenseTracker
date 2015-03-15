/** 
 * Covers Issue 5
 * Things to implement: saving of an expenseItem to an expenseClaim
 * @author Sarah Morris
 * @version 1.0
 * @since 2015-03-09
 *
 * Tests ExpenseItemActivity 
 */

package ca.ualberta.cs.shinyexpensetracker.test;

import java.text.ParseException;

import ca.ualberta.cs.shinyexpensetracker.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ExpenseItemActivityTest extends
		ActivityInstrumentationTestCase2<ExpenseItemActivity> {

	private static final int TARGET_YEAR = 2008;
	private static final int TARGET_MONTH = 11;
	private static final int TARGET_DAY = 7;

	int year, month, day;

	Instrumentation instrumentation;
	ExpenseItemActivity activity;
	DatePickerDialog datePicker;
	EditText nameInput, dateInput, amountInput, descriptionInput;
	Spinner currencyInput, categoryInput;
	ImageButton photoInput;
	Button doneButton;
	Drawable image = new Drawable() {
		@Override
		public void setColorFilter(ColorFilter arg0) {
		}

		@Override
		public void setAlpha(int arg0) {
		}

		@Override
		public int getOpacity() {
			return 0;
		}

		public void draw(Canvas arg0) {
		}
	};

	public ExpenseItemActivityTest() {
		super(ExpenseItemActivity.class);
	}

	public ExpenseItemActivityTest(Class<ExpenseItemActivity> activityClass) {
		super(activityClass);
	}

	private OnDateSetListener dateListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int Year, int monthOfYear,
				int dayOfMonth) {
			year = Year;
			month = monthOfYear;
			day = dayOfMonth;
		}
	};

	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();

		datePicker = new DatePickerDialog(instrumentation.getContext(),
				dateListener, TARGET_YEAR, TARGET_MONTH, TARGET_DAY);

		nameInput = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemNameEditText));
		dateInput = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDateEditText));
		categoryInput = ((Spinner) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCategorySpinner));
		amountInput = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemAmountEditText));
		currencyInput = ((Spinner) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemCurrencySpinner));
		descriptionInput = ((EditText) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expesenItemDescriptionEditText));
		photoInput = ((ImageButton) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemReceiptImageButton));
		doneButton = (Button) activity
				.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.expenseItemDoneButton);
	}

	/*
	 * Tests that when the ExpenseItemDateTextView is clicked a DatePickerDialog
	 * is shown
	 */
	public void testSetDateTimeField() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				dateInput.performClick();
			}
		});
		assertTrue("datepicker dialog is showing",
				((ExpenseItemActivity) activity).getDialog().isShowing());
	}

	/*
	 * tests if the data entered has been correctly saved to an expenseItem when
	 * the Done button is clicked
	 */
	public void testDone() throws ParseException {
		
		String name = "name";
		String category;

		// TODO: Test not done being implemented, needs to check to see if
		// loaded expenseItem is what was entered
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				nameInput.setText("name");
				categoryInput.setSelection(1);
			}
		});
		instrumentation.waitForIdleSync();

		ExpenseItem item = getActivity().createExpenseItem();

		assertEquals("name", item.getName());
	}

	/*
	 * tests that a dialog telling the user that they require a name before
	 * completing the expense item appears
	 */
	public void testNameDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no name dialog", activity.alertDialog);
		assertTrue("Name dialog is not showing",
				activity.alertDialog.isShowing());
	}

	/*
	 * tests that a dialog telling the user that they require a date before
	 * completing the expense item appears
	 */
	public void testDateDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no date dialog", activity.alertDialog);
		assertTrue("Date dialog is not showing",
				activity.alertDialog.isShowing());
	}

	/*
	 * tests that a dialog telling the user that they require an amount spent
	 * before completing the expense item appears
	 */
	public void testAmountDialogs() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				doneButton.performClick();
			}
		});

		assertNotNull("no amount spent dialog", activity.alertDialog);
		assertTrue("Dialog amount spent is not showing",
				activity.alertDialog.isShowing());
	}

}
