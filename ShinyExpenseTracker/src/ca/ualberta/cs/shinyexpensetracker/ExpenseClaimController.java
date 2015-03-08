package ca.ualberta.cs.shinyexpensetracker;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

// FIXME #60 - Methods needed for #22 were turned into a static method.
//			 - #60 is responsible for fixing this.
public class ExpenseClaimController {
	public static boolean DEBUGGING = false;
	private static ExpenseClaimList claimlist;
	
	// TODO #60 - Can this be removed? Dependency injection?
	private ExpenseClaimController() {
		super();
		getExpenseClaimList();
	}
	
	public static ExpenseClaimList injectExpenseClaimList(ExpenseClaimList list) {
		if (!DEBUGGING) {
			throw new RuntimeException("Expense Claim Controller not in debug mode");
		}
		claimlist = list;
		return getExpenseClaimList();
	}
	
	// TODO #60	
	public static ExpenseClaimList getExpenseClaimList() {
		if (claimlist == null) {
			claimlist = new ExpenseClaimList();
		}
		return claimlist;
	}
	
	// TODO #60
	public void saveExpenseClaim(ExpenseClaim claim) {
		// save to somewhere
	}
	
	// TODO #60
	public static void addExpenseClaim(ExpenseClaim claim) {
		claimlist.addClaim(claim);
	}
	
	// FIXME What does this do? How does it do this?
	// #60
	public void editExpenseClaim(ExpenseClaim claim) {
		claimlist.editClaim(claim);
	}
}
