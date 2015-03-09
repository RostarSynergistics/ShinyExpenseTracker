package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.*;
import ca.ualberta.cs.shinyexpensetracker.models.*;

public class ExpenseClaimControllerTest extends TestCase {

	private ExpenseClaimList claimList;

	protected void setUp() throws Exception {
		super.setUp();
		claimList = new ExpenseClaimList();
		claimList.addClaim(new ExpenseClaim());
		ExpenseClaimController.DEBUGGING = true;
		ExpenseClaimController.injectExpenseClaimList(claimList);
	}

	
	public void testGetExpenseClaimList() {
		assertEquals(claimList, ExpenseClaimController.getExpenseClaimList());
	}

	// TODO test if retrieval is possible when implemented
	public void testSaveExpenseClaim() {
		ExpenseClaimController.saveExpenseClaim(new ExpenseClaim(), new WebServiceExporter());
		fail();
	}

	public void testAddExpenseClaim(){
		assertEquals(claimList, ExpenseClaimController.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim();
		ExpenseClaimController.addExpenseClaim(newClaim);
		assertEquals(ExpenseClaimController.getExpenseClaimList().getClaim(1) , newClaim);	
	}

	public void testGetExpenseClaim(){
		ExpenseClaim claim = new ExpenseClaim("Test");
		ExpenseClaimController.addExpenseClaim(claim);
		assertEquals(claim, ExpenseClaimController.getExpenseClaim(1));
	}
}
