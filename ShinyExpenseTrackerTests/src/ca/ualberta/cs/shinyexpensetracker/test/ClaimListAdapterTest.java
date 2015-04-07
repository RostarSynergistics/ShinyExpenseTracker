package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import android.test.AndroidTestCase;
import ca.ualberta.cs.shinyexpensetracker.adapters.ClaimListAdapter;
import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimSubmittedFilter;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.framework.ValidationException;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.Status;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockClaimFilterAll;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockClaimFilterNone;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Test suite for the ClaimAdapter. This adapter is responsible for displaying
 * the list of expense claims in sorted order
 */
public class ClaimListAdapterTest extends AndroidTestCase {
	private ClaimListAdapter adapter;
	private ExpenseClaimController controller;
	private ExpenseClaimList list;

	/**
	 * Setup for each test. Creates three expenses but does not add them to the
	 * claim. Adapter is set up to watch the claim.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		list = new ExpenseClaimList();

		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(list));
		Application.setExpenseClaimController(controller);

		adapter = new ClaimListAdapter(getContext());
	}

	private ExpenseClaim addClaim(String name) throws ValidationException {
		final ExpenseClaim claim = new ExpenseClaim(name, new Date(1000), new Date(2000));
		list.addClaim(claim);
		adapter.notifyDataSetChanged();
		return claim;
	}

	private ExpenseClaim removeClaim(ExpenseClaim claim) {
		list.deleteClaim(claim.getID());
		adapter.notifyDataSetChanged();
		return claim;
	}

	/**
	 * Check that adding an item adds the item to the adapter
	 * 
	 * @throws ValidationException
	 */
	public void testGetItem() throws ValidationException {
		ExpenseClaim testClaim = addClaim("Test Claim 1");
		assertEquals(testClaim, adapter.getItem(0));
	}

	/**
	 * Check that the position is the expected position when the item is added
	 */
	public void testGetItemId() {
		assertEquals(0, adapter.getItemId(0));
	}

	/**
	 * Check that the adapter can count
	 * 
	 * @throws ValidationException
	 */
	public void testGetCount() throws ValidationException {
		assertEquals("Sanity check failed: list not empty", 0, adapter.getCount());
		assertEquals(controller.getCount(), adapter.getCount());

		ExpenseClaim testClaim1 = addClaim("Test Claim 1");
		assertEquals(controller.getCount(), adapter.getCount());
		assertEquals(1, adapter.getCount());

		ExpenseClaim testClaim2 = addClaim("Test Claim 2");
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
	 * Check that adding then removing doesn't change what we expect to see
	 * (consistent display)
	 * 
	 * @throws ValidationException
	 */
	public void testConsistentGetItem() throws ValidationException {
		ExpenseClaim testClaim1 = addClaim("Test Claim 1");
		ExpenseClaim testClaim2 = addClaim("Test Claim 2");
		removeClaim(testClaim1);

		assertEquals(testClaim2, adapter.getItem(0));
	}

	/**
	 * Checks that filtering can be done on the ExpenseClaimList
	 * 
	 * @throws ValidationException
	 */
	public void testApplyFilter() throws ValidationException {
		adapter.applyFilter(new MockClaimFilterNone());
		addClaim("Test Claim");

		assertEquals("testClaim was filtered", 1, adapter.getCount());

		adapter.applyFilter(new MockClaimFilterAll());
		adapter.applyFilter(new MockClaimFilterNone());

		assertEquals("testClaim was not filtered", 0, adapter.getCount());

		adapter.clearFilters();

		assertEquals("testClaim was filtered", 1, adapter.getCount());

	}

	public void testApplySubmittedFilter() throws ValidationException {
		adapter.applyFilter(new ExpenseClaimSubmittedFilter());
		ExpenseClaim claim = addClaim("Test claim");
		claim.setStatus(Status.SUBMITTED);

		assertEquals(1, adapter.getCount());

		ExpenseClaim claim2 = addClaim("Test claim 2");
		claim2.setStatus(Status.SUBMITTED);

		assertEquals(2, adapter.getCount());

		ExpenseClaim claim3 = addClaim("Test claim 3");
		claim3.setStatus(Status.IN_PROGRESS);
		assertEquals(2, adapter.getCount());
	}
}
