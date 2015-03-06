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
	
	
	public ExpenseClaimList getExpenseClaimList() {
		return claimlist;
	}
	
	
	public void saveExpenseClaim(ExpenseClaim claim, ClaimDataExporter exporter) {
		exporter.export(claim);	
	}
	

	public void addExpenseClaim(ExpenseClaim claim) {
		claimlist.addClaim(claim);
	}
	
	public ExpenseClaim getExpenseClaim(int index){
		return claimlist.getClaim(index);
	}
}
