package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimFilter;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * This object does not apply any filters. This is useful
 * for testing that an 
 */
public class MockClaimFilterAll extends ExpenseClaimFilter {
	private int lastIndex = 0;
	private boolean returnedClaims = false;
	private boolean gotCount = false;

	public ExpenseClaim getClaim(int index) {
		lastIndex = index;
		// Don't throw out of bounds error
		return null;
	}

	public ArrayList<ExpenseClaim> getClaims() {
		return new ArrayList<ExpenseClaim>();
	}

	public int getCount() {
		returnedClaims = true;
		return 0;
	}
	
	public int getLastClaimIndexRequest() {
		gotCount = true;
		return lastIndex;
	}
	
	public boolean getDidReturnedClaims() {
		return returnedClaims;
	}
	
	public boolean getDidGetCount() {
		return gotCount;
	}

}
