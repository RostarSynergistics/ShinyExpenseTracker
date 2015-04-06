package ca.ualberta.cs.shinyexpensetracker.activities;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.DrawableBitmapConverter;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.ValidationErrorAlertDialog;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;

/**
 * Covers Issues 5, 15, and 29
 * 
 * @version 1.2
 * @since 2015-03-15
 * 
 *        Displays activity_create_expense_item activity, to give the user an
 *        interface to add the name, date, category, amount spent, currency,
 *        description and a photo of a receipt for expense items. Also allows
 *        editing of a referred Expense Item, if there is any
 */
public class ExpenseItemActivity extends Activity implements OnClickListener {
	// DatePickerDialog from:
	// http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
	// On March 2 2015
	private EditText date;
	private DatePickerDialog datePickerDialog;
	private SimpleDateFormat dateFormatter;
	private ImageButton button;
	private Uri imageFileUri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public ValidationErrorAlertDialog alertDialog;
	private boolean isEditing = false;
	private ExpenseItem item;
	private ExpenseClaim claim;
	private HashMap<String, Integer> categoriesMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> currenciesMap = new HashMap<String, Integer>();
	private ExpenseClaimController controller;
	private Drawable defaultDrawableOnImageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_expense_item);

		dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.CANADA);

		button = (ImageButton) findViewById(R.id.expenseItemReceiptImageButton);
		defaultDrawableOnImageButton = button.getDrawable();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle != null) {
			// we have to receive a Claim ID so that we know to what claim to
			// save an item
			UUID claimID = (UUID) bundle.getSerializable(IntentExtraIDs.CLAIM_ID);
			UUID expenseItemID = (UUID) bundle.getSerializable(IntentExtraIDs.EXPENSE_ITEM_ID);
			controller = Application.getExpenseClaimController();
			claim = controller.getExpenseClaimByID(claimID);
			// if we received an Item ID
			// then we are editing an item
			// fetch the item and preset all fields with its values
			if (expenseItemID != null) {
				item = claim.getExpenseItemByID(expenseItemID);
				isEditing = true;
				populateViews();
			}
		}

		findViewsById();

		setDateTimeField();
	}

	@Override
	protected void onStart() {
		super.onStart();

		controller = Application.getExpenseClaimController();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_item, menu);
		return true;
	}

	/*
	 * Fixes null pointer on return from camera
	 * http://stackoverflow.com/questions
	 * /8248327/my-android-camera-uri-is-returning
	 * -a-null-value-but-the-samsung-fix-is-in-place March 26, 2015
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (imageFileUri != null) {
			outState.putString("cameraImageUri", imageFileUri.toString());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey("cameraImageUri")) {
			imageFileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
			// Just in case the parsing doesn't work
			if (imageFileUri != null) {
				// Create a new drawable instance from the Uri instead of
				// using the existing fs bitmap object
				button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
			}
		}
	}

	/**
	 * Pre-set fields with a loaded ExpenseItem info
	 */
	private void populateViews() {
		populateHashMaps();

		setEditTextValue(R.id.expenseItemNameEditText, item.getName().toString());
		setEditTextValue(R.id.expenseItemDateEditText, dateFormatter.format(item.getDate()));
		setEditTextValue(R.id.expesenItemDescriptionEditText, item.getDescription().toString());
		setEditTextValue(R.id.expenseItemAmountEditText, item.getAmountSpent().toString());
		Spinner s = (Spinner) findViewById(R.id.expenseItemCategorySpinner);
		s.setSelection(categoriesMap.get(item.getCategory().toString()));
		s = (Spinner) findViewById(R.id.expenseItemCurrencySpinner);
		s.setSelection(currenciesMap.get(item.getCurrency().name()));

		if (item.hasAnAttachedReceipt()) {
			button.setImageBitmap(item.getReceiptPhoto());
		}
	}

	/**
	 * Sets an given edit text view to the correct value
	 * 
	 * @param textViewId
	 *            Id for the textview
	 * @param value
	 *            value of new string to be put in
	 */
	private void setEditTextValue(int textViewId, String value) {
		EditText tv = (EditText) findViewById(textViewId);
		tv.setText(value);
	}

	/**
	 * Fills the categories hashmap to the correct values
	 */
	private void populateHashMaps() {
		categoriesMap.put("air fare", 0);
		categoriesMap.put("ground transport", 1);
		categoriesMap.put("vehicle rental", 2);
		categoriesMap.put("private automobile", 3);
		categoriesMap.put("fuel", 4);
		categoriesMap.put("parking", 5);
		categoriesMap.put("registration", 6);
		categoriesMap.put("accomodation", 7);
		categoriesMap.put("meal", 8);
		categoriesMap.put("supplies", 9);

		currenciesMap.put("CAD", 0);
		currenciesMap.put("USD", 1);
		currenciesMap.put("GBP", 2);
		currenciesMap.put("EUR", 3);
		currenciesMap.put("CHF", 4);
		currenciesMap.put("JPY", 5);
		currenciesMap.put("CNY", 6);

	}

	private void findViewsById() {
		date = (EditText) findViewById(R.id.expenseItemDateEditText);
		date.setInputType(InputType.TYPE_NULL);
		date.requestFocus();
	}

	/**
	 * Uses a DatePickerDialog to allow user to choose a date
	 */
	private void setDateTimeField() {
		date.setOnClickListener(this);
		date.setOnClickListener(this);

		Calendar newCalendar = Calendar.getInstance();

		datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				date.setText(dateFormatter.format(newDate.getTime()));
			}

		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

	}

	/**
	 * displays a datePickerDialog when expenseItemDateTextView is clicked.
	 */
	@Override
	public void onClick(View view) {
		if (view == date) {
			datePickerDialog.show();
		}
	}

	/**
	 * Gets inputed data from text fields and spinners, saving them in an
	 * expenseItem object.
	 * 
	 * @param v
	 * @throws ParseException
	 * @throws IOException
	 */
	public boolean createExpenseItem() throws ParseException, IOException {
		EditText nameText = (EditText) findViewById(R.id.expenseItemNameEditText);
		EditText dateText = (EditText) findViewById(R.id.expenseItemDateEditText);
		Spinner categorySpinner = (Spinner) findViewById(R.id.expenseItemCategorySpinner);
		EditText amountText = (EditText) findViewById(R.id.expenseItemAmountEditText);
		Spinner currencySpinner = (Spinner) findViewById(R.id.expenseItemCurrencySpinner);
		EditText descriptionText = (EditText) findViewById(R.id.expesenItemDescriptionEditText);

		String name = nameText.getText().toString();

		// get the date of the expense item
		Date date = null;
		if (dateText.getText().length() != 0) {
			date = dateFormatter.parse(dateText.getText().toString());
		}

		// get the current selection of the category spinner
		Category category = Category.fromString(categorySpinner.getSelectedItem().toString());

		// get the amount Spent from the editText, checking if not entered
		BigDecimal amount = new BigDecimal("0.00");
		if (amountText.getText().length() != 0) {
			amount = new BigDecimal(amountText.getText().toString());
		}

		// get the current selection of the currencySpinner
		Currency currency = Currency.valueOf(currencySpinner.getSelectedItem().toString());

		// get the description entered
		String description;
		if (descriptionText.getText().length() == 0) {
			description = "";
		} else {
			description = descriptionText.getText().toString();
		}

		Bitmap bm;
		// convert ImageButton image to bitmap if it is different from the
		// default Drawable
		if (button.getDrawable().equals(defaultDrawableOnImageButton)) {
			bm = null;
		} else {
			Drawable dr = button.getDrawable();
			bm = DrawableBitmapConverter.convertToBitmap(dr, dr.getMinimumWidth(), dr.getMinimumHeight());
		}

		try {
			if (isEditing) {
				controller.updateExpenseItemOnClaim(claim.getID(), item.getID(), name, date, category, amount,
						currency, description, bm);
			} else {
				controller
						.addExpenseItemToClaim(claim.getID(), name, date, category, amount, currency, description, bm);
			}
		} catch (ValidationException e) {
			alertDialog = new ValidationErrorAlertDialog(this, e);
			alertDialog.show();
		}

		return true;
	}

	/**
	 * Runs on click of the expenseItemImageButton. Opens Camera app to allow
	 * user to take a picture of a receipt. Saves picture in a temporary file.
	 * 
	 * @param V
	 */
	// Source: https://github.com/saemorris/BogoPicLab/tree/master/CameraTest
	public void takePicture(View V) {
		// Create a folder to store pictures
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Create an URI for the picture file
		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Runs on return from the camera app. Displays the image taken in the
	 * expenseItemImageButton
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Result: OK!", Toast.LENGTH_SHORT).show();
				button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Result: Cancelled", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Result: ???", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * On click of the expenseItemDoneButton, the activity will close to return
	 * to the previous activity (ExpenseItems Summary page)
	 * 
	 * @param v
	 * @throws ParseException
	 * @throws IOException
	 */
	public void doneExpenseItem(View v) throws ParseException, IOException {
		if (createExpenseItem()) {
			finish();
		}
	}

	/**
	 * Method to allow for testing of the DatePickerDialog
	 * 
	 * @return
	 */
	public DatePickerDialog getDialog() {
		return datePickerDialog;
	}
}
