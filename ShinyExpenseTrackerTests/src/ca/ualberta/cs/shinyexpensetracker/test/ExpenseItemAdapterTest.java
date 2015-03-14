package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemAdapter;
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
				"The fanciest pants");

		classyHotel = new ExpenseItem(
				"Hotel President Wilson, Geneva",
				new Date(19860613),
				ExpenseItem.Category.ACCOMODATION,
				new BigDecimal(79264.53),
				ExpenseItem.Currency.EUR,
				"FACT: Classy hotels only exist in europe");
		
		scrumptiousFood = new ExpenseItem(
				"Cavier a la mode",
				new Date(19860705),
				ExpenseItem.Category.MEAL,
				new BigDecimal(894.50),
				ExpenseItem.Currency.CAD,
				"Who puts icecream on fish eggs?");
		
		// Set up the thing we're trying to test
		adapter = new ExpenseItemAdapter(claim, getContext());
	}

	/**
	 * Check that adding an item adds the item to the adapter
	 */
	public void testGetItem() {
		// Add the expense
		claim.addExpense(fancyPants);
		assertEquals(fancyPants, adapter.getItem(0));
		// Easy peasy.
	}
	
	/**
	 * Check that the position is the expected position
	 */
	public void testGetItemId() {
		claim.addExpense(classyHotel);
		assertEquals(0, adapter.getItemId(0));
	}
	
	/**
	 * Check that the adapter can count the same as the claim
	 */
	public void testGetCount() {
		// Add just one item
		claim.addExpense(fancyPants);
		assertEquals(1, adapter.getCount());

		// Add many an item
		claim.addExpense(classyHotel);
		claim.addExpense(scrumptiousFood);
		assertEquals(3, adapter.getCount());
		
		// Remove an item
		claim.removeExpense(fancyPants);
		assertEquals(2, adapter.getCount());
		
		// Remove all items
		claim.removeExpense(scrumptiousFood);
		claim.removeExpense(classyHotel);
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
		
		assertEquals(classyHotel, adapter.getItem(0));
	}
}
