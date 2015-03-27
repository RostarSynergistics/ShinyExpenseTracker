package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.adapters.ExpenseItemAdapter;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;

/**
 * Class for testing the ExpenseItemAdapter
 */
public class ExpenseItemAdapterTest extends AndroidTestCase {
	private ExpenseItemAdapter adapter;
	
	private ExpenseClaim claim;
	
	private ExpenseItem fancyPants;
	private ExpenseItem classyHotel;
	private ExpenseItem scrumptiousFood;

	protected void setUp() throws Exception {
		super.setUp();
		// Create our own amazing claim
		claim = new ExpenseClaim(
				"The Amazing Claim",
				new Date(19860607),
				new Date(19860809)
				);
		
		// Create some expenses
		fancyPants = new ExpenseItem(
				"Fancy Pants",
				new Date(19860609),
				ExpenseItem.Category.SUPPLIES,
				new BigDecimal(100.50),
				ExpenseItem.Currency.CAD,
				"The fanciest pants",
				null,
				false);

		classyHotel = new ExpenseItem(
				"Hotel President Wilson, Geneva",
				new Date(19860613),
				ExpenseItem.Category.ACCOMODATION,
				new BigDecimal(79264.53),
				ExpenseItem.Currency.EUR,
				"FACT: Classy hotels only exist in europe",
				// Empty bitmap
				Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565),
				false);
		
		scrumptiousFood = new ExpenseItem(
				"Cavier a la mode",
				new Date(19860705),
				ExpenseItem.Category.MEAL,
				new BigDecimal(894.50),
				ExpenseItem.Currency.CAD,
				"Who puts icecream on fish eggs?",
				null,
				true);
		
		// Set up the thing we're trying to test
		adapter = new ExpenseItemAdapter(claim, getContext());
	}

	/**
	 * Check that adding an item adds the item to the adapter
	 */
	public void testGetItem() {
		// Add the expense
		claim.addExpense(fancyPants);
		adapter.notifyDataSetChanged();
		assertEquals(fancyPants, adapter.getItem(0));
		// Easy peasy.
	}
	
	/**
	 * Check that the position is the expected position
	 */
	public void testGetItemId() {
		claim.addExpense(classyHotel);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getItemId(0));
	}
	
	/**
	 * Check that the adapter can count the same as the claim
	 */
	public void testGetCount() {
		// Add just one item
		claim.addExpense(fancyPants);
		adapter.notifyDataSetChanged();
		assertEquals(1, adapter.getCount());

		// Add many an item
		claim.addExpense(classyHotel);
		claim.addExpense(scrumptiousFood);
		adapter.notifyDataSetChanged();
		assertEquals(3, adapter.getCount());
		
		// Remove an item
		claim.removeExpense(fancyPants);
		adapter.notifyDataSetChanged();
		assertEquals(2, adapter.getCount());
		
		// Remove all items
		claim.removeExpense(scrumptiousFood);
		claim.removeExpense(classyHotel);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getCount());
		
	}

	/**
	 * Check that adding then removing doesn't change what
	 * we expect to see. 
	 */
	public void testConsistentGetItem() {
		claim.addExpense(fancyPants);
		claim.addExpense(classyHotel);
		claim.removeExpense(fancyPants);
		adapter.notifyDataSetChanged();
		
		assertEquals(classyHotel, adapter.getItem(0));
	}
	
	/**
	 * Checks that the item data in the fields are what we expect
	 */
	public void testItemData() {
		claim.addExpense(scrumptiousFood);
		adapter.notifyDataSetChanged();
		View view = adapter.getView(0, null, null);

		TextView viewName = (TextView) view.findViewById(R.id.expenseItemName);
		TextView viewValue = (TextView) view.findViewById(R.id.expenseItemValue);
		TextView viewDate = (TextView) view.findViewById(R.id.expenseItemDate);
		TextView viewCategory = (TextView) view.findViewById(R.id.expenseItemCategory);

		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
		
		assertEquals(viewName.getText().toString(), scrumptiousFood.getName());
		assertEquals(viewValue.getText().toString(), scrumptiousFood.getValueString());
		assertEquals(viewDate.getText().toString(), df.format(scrumptiousFood.getDate()));
		assertEquals(viewCategory.getText().toString(), scrumptiousFood.getCategory().toString());
		
	}
	
	/**
	 * Checks if indicators appear as expected
	 */
	public void testIndicators() {
		// Flag = True, bitmap = null
		claim.addExpense(scrumptiousFood);
		adapter.notifyDataSetChanged();
		View view = adapter.getView(0, null, null);

		ImageView receiptIndicator = (ImageView) view.findViewById(R.id.expenseItemReceiptIndicator);
		CheckBox incompletenessFlag = (CheckBox) view.findViewById(R.id.expenseItemCompletenessFlag);
		
		// Ensure that the photo indicator is not set (no photo)
		assertNull(scrumptiousFood.getReceiptPhoto());
		assertNull(((BitmapDrawable)receiptIndicator.getDrawable()).getBitmap());
		
		// Ensure that the manual flag is flagged (incomplete)
		assertTrue(scrumptiousFood.getIsMarkedIncomplete());
		assertTrue(incompletenessFlag.isChecked());

		claim.removeExpense(scrumptiousFood);
		
		// Flag = False, bitmap = (exists)
		claim.addExpense(classyHotel);
		adapter.notifyDataSetChanged();
		view = adapter.getView(0, null, null);
		receiptIndicator = (ImageView) view.findViewById(R.id.expenseItemReceiptIndicator);
		incompletenessFlag = (CheckBox) view.findViewById(R.id.expenseItemCompletenessFlag);

		// Ensure that the photo indicator is set (photo exists)
		assertNotNull(classyHotel.getReceiptPhoto());
		assertNotNull(((BitmapDrawable)receiptIndicator.getDrawable()).getBitmap());
		
		// Ensure that the manual flag is not flagged (not incomplete)
		assertFalse(classyHotel.getIsMarkedIncomplete());
		assertFalse(incompletenessFlag.isChecked());
	}
	
	public void testMarkIncomplete() {
		// Flag = True
		claim.addExpense(scrumptiousFood);
		adapter.notifyDataSetChanged();
		View view = adapter.getView(0, null, null);
		
		CheckBox incompletenessFlag = (CheckBox) view.findViewById(R.id.expenseItemCompletenessFlag);
		
		// Sanity check
		assertTrue(scrumptiousFood.getIsMarkedIncomplete());
		assertEquals(scrumptiousFood.getIsMarkedIncomplete(),
				incompletenessFlag.isChecked());
		
		// Toggle the value
		incompletenessFlag.performClick();
		
		adapter.notifyDataSetChanged();
		view = adapter.getView(0, null, null);
		incompletenessFlag = (CheckBox) view.findViewById(R.id.expenseItemCompletenessFlag);
		
		// Check if value changed
		assertFalse(scrumptiousFood.getIsMarkedIncomplete());
		assertEquals(scrumptiousFood.getIsMarkedIncomplete(),
				incompletenessFlag.isChecked());
		
		// Toggle once more and check if the values match
		incompletenessFlag.performClick();
		
		adapter.notifyDataSetChanged();
		view = adapter.getView(0, null, null);
		incompletenessFlag = (CheckBox) view.findViewById(R.id.expenseItemCompletenessFlag);
		
		// Check if value changed
		assertTrue(scrumptiousFood.getIsMarkedIncomplete());
		assertEquals(scrumptiousFood.getIsMarkedIncomplete(),
				incompletenessFlag.isChecked());
		
	}
}
