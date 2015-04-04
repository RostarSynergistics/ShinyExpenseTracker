package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.math.BigDecimal;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class ESClaimManagerTest extends TestCase {
	
	ESClaimManager manager = new ESClaimManager();
	ExpenseClaimController controller;
	Destination destination = new Destination("Hawaii", "Vacation");
	ExpenseItem item = new ExpenseItem("Flight to YEG", null, Category.AIR_FARE, BigDecimal.valueOf(1000), Currency.CAD);
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
	}
	
	public void testAddClaim() throws IllegalStateException, IOException {
		ExpenseClaim claim = new ExpenseClaim("Rajan sucks", null, null, Status.IN_PROGRESS, null, "1a");
		claim.addDestination(destination);
		claim.addExpense(item);
		controller.addExpenseClaim(claim);
		manager.insertClaim(claim);
		
		assertEquals("Expense claim names are not equal", manager.getClaim("1a").getName(), 
				"Rajan sucks");
		assertEquals("Expense claim destinations are not equal", manager.getClaim("1a").getDestination(0).getName(), 
				"Hawaii");
		assertEquals("Expense claim destinations are not equal", manager.getClaim("1a").getDestination(0).getReasonForTravel(), 
				"Vacation");
		assertEquals("Expense claim items are not equal", manager.getClaim("1a").getExpense(0), 
				item);
	}
	
	public void testDeleteClaim() throws IOException {
		manager.deleteClaim("1a");
		assertNull("Expense claim at index 1a is not null", manager.getClaim("1a"));
	}
}
