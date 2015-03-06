package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.*;
import ca.ualberta.cs.shinyexpensetracker.models.*;

public class ExpenseClaimControllerTest extends TestCase {

	private ExpenseClaimList claimList;
	private ExpenseClaimController claimController;

	protected void setUp() throws Exception {
		super.setUp();
		claimList = new ExpenseClaimList();
		claimList.addClaim(new ExpenseClaim());
		claimController = new ExpenseClaimController(claimList);
	}

	
	public void testGetExpenseClaimList() {
		assertEquals(claimList, claimController.getExpenseClaimList());
	}

	// TODO test if retrieval is possible when implemented
	public void testSaveExpenseClaim() {
		claimController.saveExpenseClaim(new ExpenseClaim(), new WebServiceExporter());
		fail();
	}

	public void testAddExpenseClaim(){
		assertEquals(claimList,claimController.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim();
		claimController.addExpenseClaim(newClaim);
		assertEquals(claimController.getExpenseClaimList().getClaim(1) , newClaim);	
	}

	public void testGetExpenseClaim(){
		ExpenseClaim claim = new ExpenseClaim();
		claimController.addExpenseClaim(claim);
		assertEquals(claimController.getExpenseClaim(1), claim);
	}
}
