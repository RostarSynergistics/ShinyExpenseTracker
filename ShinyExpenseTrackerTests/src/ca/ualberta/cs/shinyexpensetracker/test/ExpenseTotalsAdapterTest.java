package ca.ualberta.cs.shinyexpensetracker.test;

import java.math.BigDecimal;
import java.util.Date;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.adapters.ExpenseTotalsAdapter;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;

/**
 * class for testing the ExpenseTotalsAdapter
 * @author Sarah Morris
 */
public class ExpenseTotalsAdapterTest extends AndroidTestCase {

	private ExpenseTotalsAdapter adapter;
	
	private ExpenseClaim claim;
	
	private ExpenseItem expense1;
	private ExpenseItem expense2;
	private ExpenseItem expense3;
	
	protected void setUp() throws Exception{
		super.setUp();
		
		//create a claim
		claim = new ExpenseClaim("test claim", new Date(1000), new Date(2000));
		
		//create some expenses
		expense1 = new ExpenseItem("expense1", 
				new Date(1100),
				Category.PRIVATE_AUTOMOBILE,
				new BigDecimal(50.99),
				Currency.EUR);
		
		expense2 = new ExpenseItem("expense2",
				new Date(2000),
				Category.ACCOMODATION,
				new BigDecimal(450.40),
				Currency.CAD);
		
		expense3 = new ExpenseItem("expense3",
				new Date(1234),
				Category.FUEL,
				new BigDecimal(20.00),
				Currency.EUR);
		
		adapter = new ExpenseTotalsAdapter(claim, getContext());
	}
	
	/**
	 * Check that adding an item adds the item to the adapter
	 */
	public void testGetItem() {
		// Add the expense
		claim.addExpense(expense1);
		adapter.notifyDataSetChanged();
		assertEquals(expense1.getCurrency().toString() + " " + expense1.getAmountSpent().toString()
				, adapter.getItem(0));
	}
	
	/**
	 * Check that the position is the expected position
	 */
	public void testGetItemId() {
		claim.addExpense(expense2);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getItemId(0));
	}
	
	/**
	 * Check that the adapter can count the same as the claim
	 */
	public void testGetCount() {
		// Add just one item
		claim.addExpense(expense1);
		adapter.notifyDataSetChanged();
		assertEquals(1, adapter.getCount());

		// Add many an item
		// expense1 currency == expense3 currency therefore there will only be two items in the list
		claim.addExpense(expense2);
		claim.addExpense(expense3);
		adapter.notifyDataSetChanged();
		assertEquals(2, adapter.getCount());
		
		// Remove an item
		claim.removeExpense(expense1);
		adapter.notifyDataSetChanged();
		assertEquals(2, adapter.getCount());
		
		// Remove all items
		claim.removeExpense(expense2);
		claim.removeExpense(expense3);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getCount());
	}
	
	/**
	 * Check that adding then removing doesn't change what
	 * we expect to see. 
	 */
	public void testConsistentGetItem() {
		claim.addExpense(expense1);
		claim.addExpense(expense2);
		claim.removeExpense(expense2);
		adapter.notifyDataSetChanged();
		
		assertEquals(expense1.getCurrency().toString() + " " + expense1.getAmountSpent().toString(), 
				adapter.getItem(0));
		
	}

	/**
	 * Check that getting the expense currency gives the expected values
	 */
	public void testCreateExpenseTotals() {
		claim.addExpense(expense1);
		adapter.notifyDataSetChanged();
		View view = adapter.getView(0, null, null);

		TextView total = (TextView) view.findViewById(android.R.id.text1);
		
		assertEquals("Expense amount not as expencted", 
				expense1.getCurrency().toString() + " " + expense1.getAmountSpent().toString(),
				total.getText().toString());
		
		claim.addExpense(expense3);
		adapter.notifyDataSetChanged();
		view = adapter.getView(0, null, null);
		
		total = (TextView) view.findViewById(android.R.id.text1);
		
		BigDecimal sum = expense1.getAmountSpent().add(expense3.getAmountSpent());
		assertEquals(expense1.getCurrency().toString() + " " + sum.toString(), total.getText().toString());
		
	}
}
