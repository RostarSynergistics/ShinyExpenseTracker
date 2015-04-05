package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.cs.shinyexpensetracker.adapters.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockClaimFilterAll;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockClaimFilterNone;

/**
 * Test suite for the ClaimAdapter. This
 * adapter is responsible for displaying the list
 * of expense claims in sorted order
 */
public class ClaimListAdapterTest extends AndroidTestCase {
	private ClaimListAdapter adapter;
	private ExpenseClaimController controller;
	
	/**
	 * Setup for each test. Creates three expenses
	 * but does not add them to the claim. Adapter
	 * is set up to watch the claim. 
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Set up the thing we're trying to test
		controller = Application.getExpenseClaimController();
		// Wipe the claim list just in case
		while (controller.getCount() > 0) {
			controller.removeExpenseClaim(controller.getExpenseClaimList().getClaim(0));
		}
		
		adapter = new ClaimListAdapter(getContext());
	}
	
	private ExpenseClaim addClaim(ExpenseClaim claim) {
		try {
			controller.addExpenseClaim(claim);
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return claim;
	}
	
	private ExpenseClaim removeClaim(ExpenseClaim claim) {
		try {
			controller.removeExpenseClaim(claim);
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return claim;
	}

	/**
	 * Check that adding an item adds the item to the adapter
	 */
	public void testGetItem() {
		ExpenseClaim testClaim = addClaim(new ExpenseClaim("Test Claim 1", new Date(100)));
		assertEquals(testClaim, adapter.getItem(0));
	}
	
	/**
	 * Check that the position is the expected position
	 * when the item is added
	 */
	public void testGetItemId() {
		assertEquals(0, adapter.getItemId(0));
	}
	
	/**
	 * Check that the adapter can count
	 */
	public void testGetCount() {
		assertEquals("Sanity check failed: list not empty", 0, adapter.getCount());
		assertEquals(controller.getCount(), adapter.getCount());

		ExpenseClaim testClaim1 = addClaim(new ExpenseClaim("Test Claim 1"));
		assertEquals(controller.getCount(), adapter.getCount());
		assertEquals(1, adapter.getCount());
		
		ExpenseClaim testClaim2 = addClaim(new ExpenseClaim("Test Claim 2"));
		assertEquals(controller.getCount(), adapter.getCount());
		assertEquals(2, adapter.getCount());
		
		removeClaim(testClaim1);
		assertEquals(controller.getCount(), adapter.getCount());
		assertEquals(1, adapter.getCount());

		removeClaim(testClaim2);
		assertEquals(controller.getCount(), adapter.getCount());
		assertEquals(0, adapter.getCount());
	}

	/**
	 * Check that adding then removing doesn't change what
	 * we expect to see (consistent display)
	 */
	public void testConsistentGetItem() {
		ExpenseClaim testClaim1 = addClaim(new ExpenseClaim("Test Claim 1"));
		ExpenseClaim testClaim2 = addClaim(new ExpenseClaim("Test Claim 2"));
		removeClaim(testClaim1);
		
		assertEquals(testClaim2, adapter.getItem(0));
	}
	
	/**
	 * Checks that filtering can be done on the ExpenseClaimList
	 */
	public void testApplyFilter() {
		adapter.applyFilter(MockClaimFilterNone.class);
		addClaim(new ExpenseClaim("Test Claim"));
		
		assertEquals("testClaim was filtered", 1, adapter.getCount());

		adapter.applyFilter(MockClaimFilterAll.class);
		adapter.applyFilter(MockClaimFilterNone.class);
		
		assertEquals("testClaim was not filtered", 0, adapter.getCount());
		
		adapter.clearFilters();
		
		assertEquals("testClaim was filtered", 1, adapter.getCount());
		
	}
}
