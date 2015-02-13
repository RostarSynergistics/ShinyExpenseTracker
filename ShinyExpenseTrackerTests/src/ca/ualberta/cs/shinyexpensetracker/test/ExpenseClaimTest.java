package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

public class ExpenseClaimTest extends TestCase {
	public void testCreateClaim(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		assertTrue("created claim is not null", claim != null);
		assertEquals("created claim is equal to default claim", claim, new ExpenseClaim("name", new Date(123456), new Date(123457)));
	}
	
	public void testAddDestination(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.addDestination(new Destination("destination", "reason"));
		
		Collection<Destination> dests = claim.getDestinations();
		
		assertTrue("destination list is not null", dests != null);
		assertEquals("destination list contains default destination", dests.get(0), new Desination("destination", "reason"));
	}
	
	public void testAddTag(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		claim.attachTag(new Tag("tag"));
		
		TagList tags = claim.getTags();
		
		assertTrue("tag list is not null", tags != null);
		assertEquals("tag is 'tag'", tags.get(0), new Tag("tag"));
	}
	
	public void testAddExpenseItem(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		claim.attachExpenseItem(new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency"));
		
		ExpenseItemList expenseItems = claim.getExpenseItems();
		
		assertTrue("tag list is not null", expenseItems != null);
		assertEquals("tag is 'tag'", expenseItems.get(0), new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency"));
	}
	
	public void testRemoveDestination(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		Destination deest = new Destination("destination", "reason")
		claim.addDestination(deest);
		
		claim.removeDestination(deest);
		
		Collection<Destination> dests = claim.getDestinations();
		
		
		assertTrue("destination list is null", dests == null);
	}
	
	public void testRemoveTag(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		Tag tag = new Tag("tag")
		claim.attachTag(tag);
		
		claim.removeTag(tag);
		
		TagList tags = claim.getTags();
		
		assertTrue("tag list is null", tags == null);
	}
	
	public void testRemoveExpenseItem(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		ExpenseItem item = new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency")
		claim.attachExpenseItem(item);
		
		claim.removeExpenseItem(item);
		
		assertTrue("tag list is null", expenseItems == null);
		
	}
	
	public void testClaimStatus(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.markSubmitted();
		assertTrue("Submitted", claim.isSubmitted());
		
		claim.unmarkSubmitted();
		claim.markReturned();
		assertTrue("Submitted", claim.isReturned());
		
		claim.unmarkReturned();
		claim.markApproved();
		assertTrue("Submitted", claim.isApproved());
	}
	
	public void testEditClaim(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.updateEndDate(new Date(1234568));
		
		assertEquals("clam updated", claim, new ExpenseClaim("name", new Date(123456), new Date(123458)));
	}
}
