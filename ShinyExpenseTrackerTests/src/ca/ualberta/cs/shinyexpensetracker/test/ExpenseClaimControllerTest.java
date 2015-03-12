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
		ExpenseClaimController.DEBUGGING = true;
		ExpenseClaimController.injectExpenseClaimList(claimList);
	}

	
	public void testGetExpenseClaimList() {
		assertEquals(claimList, ExpenseClaimController.getExpenseClaimList());
	}

	// TODO test if retrieval is possible when implemented
	public void testSaveExpenseClaim() {
		ExpenseClaimController.saveExpenseClaim(new ExpenseClaim("Test"), new WebServiceExporter());
		fail();
	}

	public void testAddExpenseClaim(){
		assertEquals(claimList, ExpenseClaimController.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim("Test");
		ExpenseClaimController.addExpenseClaim(newClaim);
		assertEquals(ExpenseClaimController.getExpenseClaimList().getClaim(1) , newClaim);	
	}

	public void testGetExpenseClaim(){
		ExpenseClaim claim = new ExpenseClaim("Test");
		ExpenseClaimController.addExpenseClaim(claim);
		assertEquals(claim, ExpenseClaimController.getExpenseClaim(1));
	}
}
