package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests various parts of the functionality of ExpenseItemActivity that relates
 * to creating new ExpenseItems.
 **/
@SuppressLint("SimpleDateFormat")
public class AddExpenseItemTests extends ActivityInstrumentationTestCase2<ExpenseItemActivity> {
	static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

	private static final int TARGET_YEAR = 2008;
	private static final int TARGET_MONTH = 11;
	private static final int TARGET_DAY = 7;

	int year, month, day;

	Instrumentation instrumentation;
	ExpenseItemActivity activity;
	DatePickerDialog datePicker;
	EditText nameField, dateField, amountField, descriptionField;
	Spinner currencySpinner, categorySpinner;
	ImageButton photoField;
	Button doneButton;
	Coordinate c;

	private MockExpenseClaimListPersister persister;
	private ExpenseClaim claim;

	Drawable image = new Drawable() {
		@Override
		public void setColorFilter(ColorFilter arg0) {}

		@Override
		public void setAlpha(int arg0) {}

		@Override
		public int getOpacity() {
			return 0;
		}

		public void draw(Canvas arg0) {}
	};

	private ExpenseClaimController controller;

	public AddExpenseItemTests() {
		super(ExpenseItemActivity.class);
	}

	public AddExpenseItemTests(Class<ExpenseItemActivity> activityClass) {
		super(activityClass);
	}

	private OnDateSetListener dateListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
			year = Year;
			month = monthOfYear;
			day = dayOfMonth;
		}
	};

	/**
	 * Setup for each test. Creates a new claim and passes the intent for the 0
	 * index. Sets up input fields
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();

		ExpenseClaimList list = new ExpenseClaimList();

		persister = new MockExpenseClaimListPersister(list);

		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		claim = new ExpenseClaim("Test Claim", new Date(1000), new Date(2000));
		list.addClaim(claim);
		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		setActivityIntent(intent);

		activity = getActivity();

		datePicker = new DatePickerDialog(instrumentation.getContext(),
				dateListener,
				TARGET_YEAR,
				TARGET_MONTH,
				TARGET_DAY);

		nameField = (EditText) activity.findViewById(R.id.expenseItemNameEditText);
		dateField = (EditText) activity.findViewById(R.id.expenseItemDateEditText);
		categorySpinner = (Spinner) activity.findViewById(R.id.expenseItemCategorySpinner);
		amountField = (EditText) activity.findViewById(R.id.expenseItemAmountEditText);
		currencySpinner = (Spinner) activity.findViewById(R.id.expenseItemCurrencySpinner);
		descriptionField = (EditText) activity.findViewById(R.id.expenseItemDescriptionEditText);
		photoField = (ImageButton) activity.findViewById(R.id.expenseItemReceiptImageButton);
		doneButton = (Button) activity.findViewById(R.id.expenseItemDoneButton);
	}

	/**
	 * Tests that when the ExpenseItemDateTextView is clicked a DatePickerDialog
	 * is shown
	 */
	public void testSetDateTimeField() {
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				dateField.performClick();
			}
		});

		instrumentation.waitForIdleSync();

		assertTrue("datepicker dialog is showing", ((ExpenseItemActivity) activity).getDialog().isShowing());
	}

	/** test if an expense Item is successfully created */
	public void testCreateExpenseItem() {
		instrumentation.runOnMainSync(new Runnable() {
			@SuppressLint("SimpleDateFormat")
			public void run() {
				assertNotNull(doneButton);

				SimpleDateFormat sdf = new SimpleDateFormat();
				Date date = new Date();
				sdf.format(date);
				BigDecimal amount = new BigDecimal(10.00);
				Bitmap bitmap = null;
				c = new Coordinate(1.0, -1.0);

				ExpenseItem expense = null;

				try {
					expense = new ExpenseItem("name",
							date,
							Category.ACCOMODATION,
							amount,
							Currency.CAD,
							"description",
							bitmap,
                            c);
				} catch (ValidationException e) {
					e.printStackTrace();
					fail();
				}

				assertEquals("name != name", "name", expense.getName());
				assertEquals("date != date", date, expense.getDate());
				assertEquals("category != accomodation", Category.ACCOMODATION, expense.getCategory());
				assertEquals("amount != 10.00", amount, expense.getAmountSpent());
				assertEquals("currency != CAD", Currency.CAD, expense.getCurrency());
				assertEquals("description != description", "description", expense.getDescription());
				assertEquals("bitmap != bitmap", bitmap, expense.getReceiptPhoto());
				assertEquals("geoloc != geoloc", c, expense.getGeolocation());
			}
		});

		instrumentation.waitForIdleSync();
	}

	/**
	 * tests if the data entered has been correctly saved to an expenseItem when
	 * the Done button is clicked
	 * 
	 * @throws ParseException
	 */
	public void testDone() throws ParseException {
		final int newSpinnerPosition = 3;
		final String newName = "McDonald's";
		final String newDateString = "2015-01-01";
		final String newCategory = (String) categorySpinner.getItemAtPosition(newSpinnerPosition);
		final BigDecimal newAmount = new BigDecimal("5000");
		final String newCurrency = (String) currencySpinner.getItemAtPosition(newSpinnerPosition);
		final String newDescription = "FooBarBaz";

		final Date newDate = sdf.parse(newDateString);

		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				nameField.setText(newName);
				dateField.setText(newDateString);
				categorySpinner.setSelection(newSpinnerPosition);
				amountField.setText(newAmount.toString());
				currencySpinner.setSelection(newSpinnerPosition);
				descriptionField.setText(newDescription);

				doneButton.performClick();
			}
		});

		getInstrumentation().waitForIdleSync();

		final ExpenseItem updatedItem = controller.getExpenseClaimByID(claim.getID()).getExpenseItemAtPosition(0);

		assertNotNull(updatedItem);
		assertEquals(newName, updatedItem.getName());
		assertEquals(newDate, updatedItem.getDate());
		assertEquals(newCategory, updatedItem.getCategory().toString());
		assertEquals(newAmount, updatedItem.getAmountSpent());
		assertEquals(newCurrency, updatedItem.getCurrency().toString());
		assertEquals(newDescription, updatedItem.getDescription());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}

	/**
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
		instrumentation.waitForIdleSync();

		assertNotNull("no name dialog", activity.alertDialog);
	}

	/**
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
		instrumentation.waitForIdleSync();

		assertNotNull("no date dialog", activity.alertDialog);
	}

	/**
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
		instrumentation.waitForIdleSync();

		assertNotNull("no amount spent dialog", activity.alertDialog);
	}
}
