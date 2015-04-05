package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.cs.shinyexpensetracker.adapters.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

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
	 * Checks that the claims are sorted.
	 * XXX Should this be tested only in ViewAllExpenseClaims?
	 */
	public void testItemsSorted() {
		ExpenseClaim[] testingClaims = {
				new ExpenseClaim("Old Claim", new Date(1000)),
				new ExpenseClaim("Mid Claim", new Date(2000)),
				new ExpenseClaim("New Claim", new Date(3000)),
		};
		int numTests = 0;

		// Check that our test case items compare correctly
		// -- Equality Cases
		assertEquals(0, testingClaims[0].compareTo(testingClaims[0]));
		assertEquals(0, testingClaims[1].compareTo(testingClaims[1]));
		assertEquals(0, testingClaims[2].compareTo(testingClaims[2]));
		// -- Inequality Cases
		assertEquals(-1, testingClaims[0].compareTo(testingClaims[1]));
		assertEquals(-1, testingClaims[0].compareTo(testingClaims[2]));
		assertEquals(-1, testingClaims[1].compareTo(testingClaims[2]));
		// -- Greater-than tests are done in the loop.

		// Iterate through all permutations
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					// Skip tests with equality--
					// These depend on stability of sort 
					if (i==j || j == k || i == k) {
						continue;
					}
					
					// Count tests actually executed
					numTests++;
					
					// Build up our list
					addClaim(testingClaims[i]);
					addClaim(testingClaims[j]);
					addClaim(testingClaims[k]);

					// Sanity check: 3 items in the list
					assertEquals(3, controller.getCount());
					
					// check index 0 is newer than index 1
					assertEquals("Comparison failed. Wanted <" + adapter.getItem(0) + "> newer than <" + adapter.getItem(1) + ">;",
							1, adapter.getItem(0).compareTo(adapter.getItem(1)));
					
					// Can skip 0 > 2 by transitivity.
					// Leave it in as a sanity check.
					assertEquals("Comparison failed. Wanted <" + adapter.getItem(0) + "> newer than <" + adapter.getItem(2) + ">;",
							1, adapter.getItem(0).compareTo(adapter.getItem(2)));
					
					// check index 1 is newer than index 2
					assertEquals("Comparison failed. Wanted <" + adapter.getItem(1) + "> newer than <" + adapter.getItem(2) + ">;",
							1, adapter.getItem(1).compareTo(adapter.getItem(2)));

					// Reset the test
					// --> Can't replace the ClaimList because we'll lose
					// 	   lose observers
					removeClaim(testingClaims[i]);
					removeClaim(testingClaims[j]);
					removeClaim(testingClaims[k]);
				}
			}
		}
		
		// Sanity check: make sure we run all permutations
		assertEquals(6, numTests);
	}
}
