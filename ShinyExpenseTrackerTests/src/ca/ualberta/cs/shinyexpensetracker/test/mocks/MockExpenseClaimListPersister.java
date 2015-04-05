package ca.ualberta.cs.shinyexpensetracker.test.mocks;

import java.io.IOException;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.persistence.IExpenseClaimListPersister;

/**
 * Implementation of IExpenseClaimListPersister that just saves and loads it from
 * an instance variable.
 */
public class MockExpenseClaimListPersister implements IExpenseClaimListPersister {
	private ExpenseClaimList list;
	private Boolean saveWasCalled = false;
	private Boolean loadWasCalled = false;
	
	public MockExpenseClaimListPersister() {
		this(new ExpenseClaimList());
	}
	
	public MockExpenseClaimListPersister(ExpenseClaimList list) {
		this.list = list;
	}

	@Override
	public ExpenseClaimList loadExpenseClaims() throws IOException {
		loadWasCalled = true;
		return list;
	}

	@Override
	public void saveExpenseClaims(ExpenseClaimList list) throws IOException {
		saveWasCalled = true;
		this.list = list;
	}
	
	public Boolean wasSaveCalled() {
		return saveWasCalled;
	}

	public Boolean wasLoadCalled() {
		return loadWasCalled;
	}
}
