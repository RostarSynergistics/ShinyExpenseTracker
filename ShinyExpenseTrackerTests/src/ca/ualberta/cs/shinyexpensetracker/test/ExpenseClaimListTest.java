package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import junit.framework.TestCase;

public class ExpenseClaimListTest extends TestCase {
	public void testEmptyClaimList(){
		ExpenseClaimList expenseClaimList = new expenseClaimList();
    	Collection<ExpenseClaim> claims = expenseClaimList.getClaims();
    	assertTrue("Empty Expense Claim List", claims.size() == 0);
	}
	
	public void testAddClaimToClaimList(){
		ExpenseClaimList expenseClaimList = new expenseClaimList();
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		expenseClaimList.add(claim);
		
		ExpenseClaim retrievedClaim = expenseClaimList.get(claim);
		
		assertTrue("One iem in expense claim List", expenseClaimList.size() == 1);
		assertTrue("retrieved the right claim", claim.equals(retrievedClaim));
	}
	
	public void testRemoveClaimFromList(){
		ExpenseClaimList expenseClaimList = new expenseClaimList();
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		expenseClaimList.add(claim);
		
		expenseClaimList.remove(claim);
		
		assertTrue("Empty Expense Claim List", claims.size() == 0);
	}
	
	public void testEditClaimInList(){
		ExpenseClaimList expenseClaimList = new expenseClaimList();
		ExpenseClaim claim = new ExpenseClaim("name", new Date(123456), new Date(123457));
		
		expenseClaimList.add(claim);
		
		ExpenseClaim retrievedClaim = expenseClaimList.get(claim);
		retrievedClaim.edit();
		
		assertFalse("retrieved claim is not equal to the inserted claim", claim.equals(retrievedClaim));
	}
}
