package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemActivity;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests activity that lets the user edit information of the selected
 * ExpenseItem
 * 
 * Covers Issue 15
 * 
 * @author Oleg Oleynikov
 * @version 1.0
 * @since 2015-03-15
 */

public class TestEditExpenseItem extends
		ActivityInstrumentationTestCase2<ExpenseItemActivity> {

	public TestEditExpenseItem() {
		super(ExpenseItemActivity.class);
	}

	ExpenseItemActivity activity;
	ExpenseClaimController controller;
	Bitmap imageSmall;
	Bitmap imageBig;

	Resources res;
	
	protected void setUp() throws Exception {
		super.setUp();

		ExpenseClaimList claimList = new ExpenseClaimList();
		ExpenseClaimController controller = new ExpenseClaimController(
				new MockExpenseClaimListPersister(claimList));
		Application.setExpenseClaimController(controller);

		ExpenseClaim claim = new ExpenseClaim("test claim");
		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);
		res = getInstrumentation().getTargetContext().getResources();
		imageSmall = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		imageBig = BitmapFactory.decodeResource(res, R.drawable.keyhole_nebula_hubble_1999);
		
		ExpenseItem item = new ExpenseItem("test item", newDate.getTime(), Category.fromString("air fare"), new BigDecimal("0.125"), Currency.CAD, "Test Item", imageBig);
		claim.addExpense(item);
		claimList.addClaim(claim);
		Intent intent = new Intent();

		intent.putExtra("claimIndex", 0);
		intent.putExtra("itemIndex", 0);

	    setActivityIntent(intent);
	    activity = getActivity();
	}

	public void testNameValue() {
		EditText name = (EditText) activity
				.findViewById(R.id.expenseItemNameEditText);
		assertEquals("name is not right", name.getText().toString(),
				"test item");
	}

	public void testDateValue() {
		EditText date = (EditText) activity
				.findViewById(R.id.expenseItemDateEditText);
		assertEquals("date is not right", date.getText().toString(),
				"01-01-2000");
	}

	public void testCategoryValue() {
		Spinner category = (Spinner) activity
				.findViewById(R.id.expenseItemCategorySpinner);
		assertEquals("category is not right", category.getSelectedItem()
				.toString(), "air fare");
	}

	public void testAmountValue() {
		EditText amount = (EditText) activity
				.findViewById(R.id.expenseItemAmountEditText);
		assertEquals("amount is not right", amount.getText().toString(),
				"0.125");
	}

	public void testCurrencyValue() {
		Spinner currency = (Spinner) activity
				.findViewById(R.id.expenseItemCurrencySpinner);
		assertEquals("currency is not right", currency.getSelectedItem()
				.toString(), "CAD");
	}

	public void testDescriptionValue() {
		EditText description = (EditText) activity
				.findViewById(R.id.expesenItemDescriptionEditText);
		assertEquals("description is not right", description.getText()
				.toString(), "Test Item");
	}

	public void testEdit() throws ParseException {

		clickDone();

		Calendar newDate = Calendar.getInstance();
		newDate.set(2000, 00, 01);
		Date date = newDate.getTime();
		// without this, date is saved as DateTime
		// so format date into string and parse it back to get date at midnight;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy",
				Locale.CANADA);
		String inputDate = dateFormatter.format(date);
		Date dateToAssign = dateFormatter.parse(inputDate);

		BitmapDrawable dr = new BitmapDrawable(res, imageBig);
		Bitmap imageBigScaled = activity.convertToBitmap(dr, imageBig.getWidth(), imageBig.getHeight());
		ExpenseItem editedItem = new ExpenseItem("test item", dateToAssign, Category.fromString("air fare"), new BigDecimal("0.125"), Currency.CAD, "Test Edit", imageBigScaled);
		assertEquals("did not update item", controller.getExpenseClaim(0).getItemById(0), editedItem);
	}
	/**
	 * This tests that the image is successfully drawn on the ImageButton
	 */

	public void testDrawImageButton(){
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
	public void testScale(){
		BitmapDrawable dr = new BitmapDrawable(res, imageBig);
		Bitmap imageBigScaled = activity.convertToBitmap(dr, imageBig.getWidth(), imageBig.getHeight());
		assertTrue("image too big", imageBigScaled.getByteCount()<=65536);
	}
	/**
	 * Draw a new image on ImageButton
	 */
	private void drawNewImage(){
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				ImageButton button = (ImageButton) activity.findViewById(R.id.expenseItemReceiptImageButton);
				button.setImageBitmap(imageSmall);
			}
		});
		getInstrumentation().waitForIdleSync();
	}
	
	/**
	 * Update the global Claim List in main thread
	 */
	private void clickDone() {
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				EditText description = (EditText) activity
						.findViewById(R.id.expesenItemDescriptionEditText);
				description.setText("Test Edit");
				Button doneButton = (Button) activity
						.findViewById(R.id.expenseItemDoneButton);
				doneButton.callOnClick();
			}
		});
		getInstrumentation().waitForIdleSync();

	}

}
