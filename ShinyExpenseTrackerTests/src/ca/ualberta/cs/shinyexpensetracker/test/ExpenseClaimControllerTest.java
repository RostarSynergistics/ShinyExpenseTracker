package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.*;
import ca.ualberta.cs.shinyexpensetracker.models.*;

public class ExpenseClaimControllerTest extends TestCase {

	private ExpenseClaimList claimList;
	private ExpenseClaimController claimController;

	// TODO Create a proper expense claim when the expense claim constructor is
	// implemented
	protected void setUp() throws Exception {
		super.setUp();
		claimList = new ExpenseClaimList();
		claimList.addClaim(new ExpenseClaim());
		claimController = new ExpenseClaimController(claimList);
	}

	// TODO Change to use .equals if implemented
	public void testGetExpenseClaim() {
		assertEquals(claimList, claimController.getExpenseClaimList());
	}

	// TODO test if retrieval is possible when implemented
	public void testSaveExpenseClaim() {
		claimController.saveExpenseClaim(new ExpenseClaim());
		fail();

	}

	public void testAddExpenseClaim(){
		assertEquals(claimList,claimController.getExpenseClaimList());
		ExpenseClaim newClaim = new ExpenseClaim();
		claimController.addExpenseClaim(newClaim);
		assertEquals(claimController.getExpenseClaimList().getClaim(2) , newClaim);	
	}

	public void testEditExpenseClaim() {
		fail();
	}
}
