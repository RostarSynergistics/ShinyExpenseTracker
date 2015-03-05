package ca.ualberta.cs.shinyexpensetracker;

import java.util.ArrayList;



import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ExpenseClaimController {
	private ExpenseClaimList claimlist;

	public ExpenseClaimController() {
		super();
		this.claimlist = new ExpenseClaimList();
	}
	
	public ExpenseClaimController(ExpenseClaimList claimlist) {
		super();
		this.claimlist = claimlist;
	}
	
	// FIXME Where does this come from?
	public ExpenseClaimList getExpenseClaimList() {
		return claimlist;
	}
	
	// FIXME What does this do? UML doesn't specify what or how.
	public void saveExpenseClaim(ExpenseClaim claim) {
		WebServiceExporter exporter = new WebServiceExporter();
		exporter.export(claim);
		
	}
	
	// Renamed from "addExpenseClaimToList" ???
	public void addExpenseClaim(ExpenseClaim claim) {
		claimlist.addClaim(claim);
	}
	
	// FIXME wtf does "addExpenseClaimToList(ExpenseClaimList)" mean??
	// How does it use an ExpenseClaimList??
	// Same with editExpenseClaim(...);
	
	public void editExpenseClaim(ExpenseClaim claim) {
		claimlist.editClaim(claim);
	}
}
