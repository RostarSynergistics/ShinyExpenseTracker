package ca.ualberta.cs.shinyexpensetracker.test;

//import java.util.Date;

import junit.framework.TestCase;

public class ExpenseClaimListTest extends TestCase {
//	// Not part of use cases, but an obvious test
//	public void testEmptyClaimList(){
//		ExpenseClaimList expenseClaimList = new ExpenseClaimList();
//    	Collection<ExpenseClaim> claims = expenseClaimList.getClaims();
//    	assertTrue("Empty Expense Claim List", claims.size() == 0);
//	}
//	
//	// Issues #17, #22
//	public void testAddClaimToClaimList(){
//		ExpenseClaimList expenseClaimList = new ExpenseClaimList();
//		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
//		
//		expenseClaimList.add(claim);
//		
//		ExpenseClaim retrievedClaim = expenseClaimList.get(claim);
//		
//		assertTrue("One iem in expense claim List", expenseClaimList.size() == 1);
//		assertTrue("retrieved the right claim", claim.equals(retrievedClaim));
//	}
//	
//	// Issues #21, #22
//	public void testRemoveClaimFromList(){
//		ExpenseClaimList expenseClaimList = new ExpenseClaimList();
//		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
//		
//		expenseClaimList.add(claim);
//		
//		expenseClaimList.remove(claim);
//		
//		assertTrue("Empty Expense Claim List", claims.size() == 0);
//	}
//	
//	// Covers issue #7
//	public void testGetNoSubmittedClaims() {
//		ExpenseClaimList expenseClaimList = new ExpenseClaimList();
//		ExpenseClaim claim1 = new ExpenseClaim("in_progress", new Date(123456), new Date(123457));
//		expenseClaimList.add(claim1);
//		ExpenseClaim claim2 = new ExpenseClaim("approved", new Date(123456), new Date(123457));
//		expenseClaimList.add(claim2);
//		
//		ArrayList<ExpenseClaim> retrievedClaims = expenseClaimList.getSubmitted();
//		
//		assertTrue("More claims than expected", retrievedClaims.size() == 0);
//	}
//	
//	// Covers issue #7
//	public void testGetSubmittedClaims() {
//		ExpenseClaimList expenseClaimList = new ExpenseClaimList();
//		ExpenseClaim claim1 = new ExpenseClaim("in_progress", new Date(123456), new Date(123457));
//		expenseClaimList.add(claim1);
//		ExpenseClaim claim2 = new ExpenseClaim("submitted", new Date(123456), new Date(123457));
//		expenseClaimList.add(claim2);
//		ExpenseClaim claim3 = new ExpenseClaim("approved", new Date(123456), new Date(123457));
//		expenseClaimList.add(claim3);
//		
//		ArrayList<ExpenseClaim> retrievedClaims = expenseClaimList.getSubmitted();
//		
//		assertTrue("More claims than expected", retrievedClaims.size() == 1);
//		assertTrue("Claim retrieved not the submitted one", retrievedClaims[0].status == "submitted");
//	}
}