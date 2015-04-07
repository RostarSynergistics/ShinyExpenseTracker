package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.utilities.IntentExtraIDs;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;
import ca.ualberta.cs.shinyexpensetracker.utilities.Base64BitmapConverter;

/**
 * Tests various parts of the functionality of ExpenseItemActivity that relates
 * to editing existing ExpenseItems.
 **/
@SuppressLint("SimpleDateFormat")
public class EditExpenseItemTests extends ActivityInstrumentationTestCase2<ExpenseItemActivity> {
	static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

	ExpenseItemActivity activity;
	ExpenseClaimController controller;
	Bitmap imageSmall;
	Bitmap imageBig;

	Resources res;
	private EditText nameField;
	private EditText dateField;
	private Spinner categorySpinner;
	private EditText amountField;
	private Spinner currencySpinner;
	private EditText descriptionField;
	private Button doneButton;
	private TextView geoloc;
	Coordinate c;

	private MockExpenseClaimListPersister persister;

	private ExpenseItem item;

	private ExpenseClaim claim;

	public EditExpenseItemTests(Class<ExpenseItemActivity> activityClass) {
		super(activityClass);
	}

	public EditExpenseItemTests() {
		super(ExpenseItemActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		persister = new MockExpenseClaimListPersister(claimList);
		controller = new ExpenseClaimController(persister);
		Application.setExpenseClaimController(controller);

		claim = new ExpenseClaim("test claim");
		res = getInstrumentation().getTargetContext().getResources();
		imageSmall = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		imageBig = BitmapFactory.decodeResource(res, R.drawable.keyhole_nebula_hubble_1999);

		c = new Coordinate(1.0, -1.0);

		item = new ExpenseItem("test item",
				sdf.parse("2001-01-01"),
				Category.fromString("air fare"),
				new BigDecimal("0.125"),
				Currency.CAD,
				"Test Item",
				imageBig,
				c);

		claim.addExpenseItem(item);
		claimList.addClaim(claim);

		Intent intent = new Intent();
		intent.putExtra(IntentExtraIDs.CLAIM_ID, claim.getID());
		intent.putExtra(IntentExtraIDs.EXPENSE_ITEM_ID, item.getID());

		setActivityIntent(intent);
		activity = getActivity();

		nameField = (EditText) activity.findViewById(R.id.expenseItemNameEditText);
		dateField = (EditText) activity.findViewById(R.id.expenseItemDateEditText);
		categorySpinner = (Spinner) activity.findViewById(R.id.expenseItemCategorySpinner);
		amountField = (EditText) activity.findViewById(R.id.expenseItemAmountEditText);
		currencySpinner = (Spinner) activity.findViewById(R.id.expenseItemCurrencySpinner);
		descriptionField = (EditText) activity.findViewById(R.id.expenseItemDescriptionEditText);
		doneButton = (Button) activity.findViewById(R.id.expenseItemDoneButton);
		geoloc = (TextView) activity.findViewById(R.id.expenseItemCoordinatesValueTextView);
	}

	public void testThatFieldsWerePopulatedProperlyOnStart() throws ParseException {
		assertEquals("name is not right", item.getName(), nameField.getText().toString());
		assertEquals("date is not right", item.getDate(), getDate(dateField));
		assertEquals("category is not right", item.getCategory().toString(), categorySpinner.getSelectedItem()
				.toString());
		assertEquals("amount is not right", item.getAmountSpent().toString(), amountField.getText().toString());
		assertEquals("currency is not right", item.getCurrency().toString(), currencySpinner.getSelectedItem()
				.toString());
		assertEquals("description is not right", item.getDescription(), descriptionField.getText().toString());
		assertEquals("wrong geolocation", item.getGeolocation().toString(), geoloc.getText().toString());
	}

	public void testThatTappingDoneWhileEditingAnExistingExpenseClaimUpdatesThatExpenseClaim() throws ParseException {
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

		final ExpenseItem updatedItem = controller.getExpenseClaimByID(claim.getID()).getExpenseItemByID(item.getID());
		assertEquals(newName, updatedItem.getName());
		assertEquals(newDate, updatedItem.getDate());
		assertEquals(newCategory, updatedItem.getCategory().toString());
		assertEquals(newAmount, updatedItem.getAmountSpent());
		assertEquals(newCurrency, updatedItem.getCurrency().toString());
		assertEquals(newDescription, updatedItem.getDescription());

		assertTrue("Persister's .save() was never called", persister.wasSaveCalled());
	}

	/**
	 * This tests that the image is successfully drawn on the ImageButton
	 */

	public void testDrawImageButton() {
		ImageButton button = (ImageButton) activity.findViewById(R.id.expenseItemReceiptImageButton);
		drawNewImage();
		assertNotNull("no image on button", button.getDrawable());
		Drawable dr = button.getDrawable();
		Bitmap bm = activity.convertToBitmap(dr, imageSmall.getWidth(), imageSmall.getHeight());
		assertTrue("receipt is not right", bm.sameAs(imageSmall));
	}

	/**
	 * This tests that the image is successfully scaled down
	 */
	public void testScale() {
		BitmapDrawable dr = new BitmapDrawable(res, imageBig);
		Bitmap imageBigScaled = activity.convertToBitmap(dr, imageBig.getWidth(), imageBig.getHeight());
		String base64Img = Base64BitmapConverter.convertToBase64(imageBigScaled);
		assertTrue("image too big", base64Img.length() <= 65536);
	}

	/**
	 * Draw a new image on ImageButton
	 */
	private void drawNewImage() {
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				ImageButton button = (ImageButton) activity.findViewById(R.id.expenseItemReceiptImageButton);
				button.setImageBitmap(imageSmall);
			}
		});
		getInstrumentation().waitForIdleSync();
	}

	private Date getDate(EditText dateField) throws ParseException {
		return sdf.parse(dateField.getText().toString());
	}
}
