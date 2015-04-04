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
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

public class ESClaimManagerTest extends TestCase {
	
	ESClaimManager manager = new ESClaimManager();
	Destination destination = new Destination("Vancouver", "Vacation");
	TagList tagList = new TagList();
	Tag exTag = new Tag("Example");
	ExpenseItem item = new ExpenseItem("Flight to YEG", null, Category.AIR_FARE, BigDecimal.valueOf(1000), Currency.CAD);
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Tests whether or not ElasticSearch adds a selected claim
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void testAddClaim() throws IllegalStateException, IOException {
		tagList.addTag(exTag);
		ExpenseClaim claim = new ExpenseClaim("BC trip", null, null, Status.IN_PROGRESS, tagList, "1a");
		claim.addDestination(destination);
		claim.addExpense(item);
		manager.insertClaim(claim);
		
		assertEquals("Expense claim names are not equal", manager.getClaim("1a").getName(), 
				"BC trip");
		
		assertEquals("Expense claim destination names are not equal", manager.getClaim("1a").getDestination(0).getName(), 
				"Vancouver");
		
		assertEquals("Expense claim destination reasons for travel are not equal", manager.getClaim("1a").getDestination(0).getReasonForTravel(), 
				"Vacation");
		
		assertEquals("Expense claim status is not IN PROGRESS", manager.getClaim("1a").getStatus(),
				Status.IN_PROGRESS);
		
		assertEquals("Expense claim tag is not Example", manager.getClaim("1a").getTagList().getTagById(0),
				exTag);
		
		assertEquals("Expense claim items are not equal", manager.getClaim("1a").getExpense(0), 
				item);
	}

	/**
	 * Test whether or not ElasticSearch deletes a selected claim
	 * @throws IOException
	 */
	
	public void testDeleteClaim() throws IOException {
		manager.deleteClaim("1a");
		assertNull("Expense claim at index 1a is not null", manager.getClaim("1a"));
	}
}
