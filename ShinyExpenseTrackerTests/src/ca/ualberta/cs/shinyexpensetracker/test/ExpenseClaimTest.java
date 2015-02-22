package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

public class ExpenseClaimTest extends TestCase {
	// Issue #17
	public void testCreateClaim(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		assertTrue("created claim is not null", claim != null);
		assertEquals("created claim is equal to default claim", claim, new ExpenseClaim("name", new Date(123456), new Date(123457)));
	}
	
	// Issue #18
	public void testAddDestination(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.addDestination(new Destination("destination", "reason"));
		
		Collection<Destination> dests = claim.getDestinations();
		
		assertTrue("destination list is not null", dests != null);
		assertEquals("destination list contains default destination", dests.get(0), new Desination("destination", "reason"));
	}
	
	// Issue #25
	public void testAddTag(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		claim.attachTag(new Tag("tag"));
		
		TagList tags = claim.getTags();
		
		assertTrue("tag list is not null", tags != null);
		assertEquals("tag is 'tag'", tags.get(0), new Tag("tag"));
	}
	
	// Issue #5
	public void testAddExpenseItem(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		claim.attachExpenseItem(new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency"));
		
		ExpenseItemList expenseItems = claim.getExpenseItems();
		
		assertTrue("tag list is not null", expenseItems != null);
		assertEquals("tag is 'tag'", expenseItems.get(0), new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency"));
	}
	
	// Not part of use cases; here because it is an obvious extended functionality
	public void testRemoveDestination(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		Destination deest = new Destination("destination", "reason")
		claim.addDestination(deest);
		
		claim.removeDestination(deest);
		
		Collection<Destination> dests = claim.getDestinations();
		
		
		assertTrue("destination list is null", dests == null);
	}
	
	// Issue #26
	public void testRemoveTag(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		Tag tag = new Tag("tag")
		claim.attachTag(tag);
		
		claim.removeTag(tag);
		
		TagList tags = claim.getTags();
		
		assertTrue("tag list is null", tags == null);
	}
	
	// Issue #14
	public void testRemoveExpenseItem(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		ExpenseItem item = new ExpenseItem("Description", "Date", "Category", "Amount Spent", "Unit of Currency")
		claim.attachExpenseItem(item);
		
		claim.removeExpenseItem(item);
		
		assertTrue("tag list is null", expenseItems == null);
		
	}
	
	// Issues #10, #11, #12
	public void testClaimStatus(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.markSubmitted();
		assertTrue("Submitted", claim.isSubmitted());
		
		claim.unmarkSubmitted();
		claim.markReturned();
		assertTrue("Returned", claim.isReturned());
		
		claim.unmarkReturned();
		claim.markApproved();
		assertTrue("Approved", claim.isApproved());
	}
	
	// Issue #20
	public void testEditClaim(){
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		claim.updateEndDate(new Date(1234568));
		
		assertEquals("clam updated", claim, new ExpenseClaim("name", new Date(123456), new Date(123458)));
	}
}
