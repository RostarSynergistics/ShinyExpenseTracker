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

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.*;
import ca.ualberta.cs.shinyexpensetracker.models.*;

public class ExpenseClaimControllerTest extends TestCase {

	private static ExpenseClaimList claimList;

	protected void setUp() throws Exception {
		super.setUp();
		claimList = new ExpenseClaimList();
		claimList.addClaim(new ExpenseClaim("Test"));
		ExpenseClaimController.getInstance().setClaimList(claimList);
	}

	
	public void testGetExpenseClaimList() {
		assertEquals(claimList, ExpenseClaimController.getInstance().getExpenseClaimList());
	}

	// TODO test if retrieval is possible when implemented
	public void testSaveExpenseClaim() {
		ExpenseClaimController.getInstance().saveExpenseClaim(new ExpenseClaim("Test"), new WebServiceExporter());
		fail();
	}

	public void testAddExpenseClaim(){
		ExpenseClaimController controller = ExpenseClaimController.getInstance();
		assertEquals(claimList, controller.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim("Test");
		controller.addExpenseClaim(newClaim);
		assertEquals(controller.getExpenseClaimList().getClaim(1) , newClaim);	
	}

	public void testGetExpenseClaim(){
		ExpenseClaimController controller = ExpenseClaimController.getInstance();
		ExpenseClaim claim = new ExpenseClaim("Test");
		controller.addExpenseClaim(claim);
		assertEquals(claim, controller.getExpenseClaim(1));
	}
}
