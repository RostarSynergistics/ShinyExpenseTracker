package ca.ualberta.cs.shinyexpensetracker;

import java.util.ArrayList;



import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ExpenseClaimController {
	private ExpenseClaimList claimlist;
	private static ExpenseClaimController claimController;
	
	
	private ExpenseClaimController() {
		super();
		this.claimlist = new ExpenseClaimList();
		}
	
	private ExpenseClaimController(ExpenseClaimList claimlist) {
		super();
		this.claimlist = claimlist;
	}
	
	public ExpenseClaimController getInstance(){
		if(claimController == null){
			claimController = new ExpenseClaimController();
			return claimController;
		}
		else {
			return claimController;
		}
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
