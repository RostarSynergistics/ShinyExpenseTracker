package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import ca.ualberta.cs.shinyexpensetracker.es.ESClaimManager;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim.Status;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Category;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ESClaimManagerTest extends TestCase {
	
	ESClaimManager manager = new ESClaimManager();
	Destination destination = new Destination("Vancouver", "Vacation", new Coordinate(49.28, -123.12));
	TagList tagList = new TagList();
	Tag exTag = new Tag("Example");
	ExpenseItem item = new ExpenseItem("Flight to YEG", null, Category.AIR_FARE, BigDecimal.valueOf(1000), Currency.CAD);
	ExpenseClaimList list;
	ExpenseClaimList list2;
	ExpenseClaim claim;
	ExpenseClaim claim2;
	
	@SuppressWarnings("deprecation") // Because its easier 
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Date sdate = new Date(1990, 11, 11);
		Date edate = new Date(1990, 11, 11);
		
		tagList.addTag(exTag);
		claim = new ExpenseClaim("BC trip", sdate, edate, Status.IN_PROGRESS, tagList);
		claim.addDestination(destination);
		claim.addExpense(item);
		list = new ExpenseClaimList();
		list2 = new ExpenseClaimList();
		list.addClaim(claim);
		claim2 = new ExpenseClaim("AB Trip", null, null, Status.IN_PROGRESS, tagList);
		claim2.addDestination(destination);
		claim2.addExpense(item);
		list.addClaim(claim2);
		
		list2.addClaim(claim2);
		list2.addClaim(claim);
			
	}
	
	/**
	 * Tests whether or not ElasticSearch adds a selected claim
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void testAddClaim() throws IllegalStateException, IOException{
		
		//Posting to the server
		manager.insertClaimList(list);
	
		//Getting the list from the server and seeing if it is the same
		ExpenseClaimList gottenList = manager.getClaimList();
		assertEquals(gottenList.getClaim(0), claim);
		assertEquals(gottenList.getClaim(1), claim2);
		
		//Testing adding a whole new list
		manager.insertClaimList(list2);
		gottenList = manager.getClaimList();
		
		//Testing it is not equal to the old list 
		assertFalse(gottenList.getClaim(0).equals(claim));
		
		assertEquals(gottenList.getClaim(0), claim2);
		assertEquals(gottenList.getClaim(1), claim);
	}

	
	
}