/**
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Covers Issue 
 */

package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.ConcreteExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Tests for the ExpenseClaimController.
 * 
 * The controller object acts as the central for all
 * interactions within the app, providing a consistent
 * storage for all stored claims. 
 *
 */
public class ExpenseClaimControllerTest extends TestCase {
	private ExpenseClaimController controller;
	private ExpenseClaimList claimList;
	
	/**
	 * Setup for the tests.
	 * Creates a controller with a mock list object
	 * that provides faked persistent storage.
	 * Adds one claim for testing.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		claimList = new ConcreteExpenseClaimList();
		claimList.addClaim(new ExpenseClaim("Test"));
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister(claimList));
	}

	
	/**
	 * Check if we always get the same claim list.
	 */
	public void testGetExpenseClaimList() {
		assertEquals(claimList, controller.getExpenseClaimList());
	}

	/**
	 * Tests if adding an expense claim is reflected correctly in
	 * the controller.
	 */
	public void testAddExpenseClaim() throws IOException {
		assertEquals(claimList, controller.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim("Test");
		controller.addExpenseClaim(newClaim);
		assertEquals(controller.getExpenseClaimList().getClaim(1) , newClaim);	
	}

	/**
	 * Tests if we get the correct claim after adding.
	 */
	public void testGetExpenseClaim() throws IOException {
		ExpenseClaim claim = new ExpenseClaim("Test");
		controller.addExpenseClaim(claim);
		assertEquals(claim, controller.getExpenseClaim(1));
	}
}
