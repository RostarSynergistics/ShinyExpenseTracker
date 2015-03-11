package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.IView;

public class ExpenseClaimList extends Model<ExpenseClaimList> {
	private ArrayList<ExpenseClaim> claims;
	
	/* FIXME
	 * UML says 0..* ExpenseClaimList's are composed of
	 * 			1 ExpenseClaimController,
	 * and that 1 ExpenseClaimList is composed of ? Expense Claims
	 */
	
	public ExpenseClaimList(){
		claims = new ArrayList<ExpenseClaim>();
	}
	// FIXME UML says this takes no args
	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
	}
	
	// FIXME What does this do?
	// Assuming it takes the argument claim.
	public void editClaim(ExpenseClaim claim) {
		return;
	}
	
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
	}
}
