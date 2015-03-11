package ca.ualberta.cs.shinyexpensetracker.test;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.*;
import ca.ualberta.cs.shinyexpensetracker.models.*;

public class ExpenseClaimControllerTest extends TestCase {

	private ExpenseClaimList claimList;

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
