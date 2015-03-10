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
	public static void saveExpenseClaim(ExpenseClaim claim, ClaimDataExporter exporter) {
		exporter.export(claim);	
	}
	
	// TODO #60
	// Lazy-ish singleton. This is the least ugly I'm willing to make it.
	public static void addExpenseClaim(ExpenseClaim claim) {
		getExpenseClaimList().addClaim(claim);
	}
	
	// TODO #60
	// This will probably open an activity that's responsible for
	// editing the claim
	public void editExpenseClaim(ExpenseClaim claim) {
		getExpenseClaimList().editClaim(claim);
	}
	
	public static ExpenseClaim getExpenseClaim(int index){
		return claimlist.getClaim(index);
	}

	public static void removeExpenseClaim(ExpenseClaim claim) {
		getExpenseClaimList().removeClaim(claim);
	}
}