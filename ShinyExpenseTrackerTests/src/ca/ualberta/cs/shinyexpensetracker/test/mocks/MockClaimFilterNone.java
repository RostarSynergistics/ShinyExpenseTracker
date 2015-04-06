package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimFilter;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * This object does not apply any filters. This is useful for testing that an
 */
public class MockClaimFilterNone extends ExpenseClaimFilter {
	private int lastIndex = 0;
	private boolean returnedClaims = false;
	private boolean gotCount = false;

	public ExpenseClaim getClaim(int index) {
		lastIndex = index;
		return source.getClaimAtPosition(index);
	}

	public ArrayList<ExpenseClaim> getClaims() {
		return source.getClaims();
	}

	public int getCount() {
		returnedClaims = true;
		return source.getCount();
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
