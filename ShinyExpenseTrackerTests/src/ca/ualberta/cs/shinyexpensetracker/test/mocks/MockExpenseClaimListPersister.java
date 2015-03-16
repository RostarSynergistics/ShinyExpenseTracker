package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;

public class MockExpenseClaimListPersister 
implements IExpenseClaimListPersister {
	private ExpenseClaimList list;
	
	public MockExpenseClaimListPersister() {
		this(new ExpenseClaimList());
	}
	
	public MockExpenseClaimListPersister(ExpenseClaimList list) {
		this.list = list;
	}

	@Override
	public ExpenseClaimList loadExpenseClaims() throws IOException {
		return list;
	}

	@Override
	public void saveExpenseClaims(ExpenseClaimList list) throws IOException {
		this.list = list;
	}
}
