package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.ualberta.cs.shinyexpensetracker.IView;

public class ExpenseClaimList implements IModel<IView<ExpenseClaimList>> {
	private ArrayList<ExpenseClaim> claims;
	
	private transient ArrayList<IView<ExpenseClaimList>> views; // FIXME: Not initialized
	/* FIXME
	 * UML says 0..* ExpenseClaimList's are composed of
	 * 			1 ExpenseClaimController,
	 * and that 1 ExpenseClaimList is composed of ? Expense Claims
	 */
	
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
		views = new ArrayList<IView<ExpenseClaimList>>();
	}

	// FIXME UML says this takes no args
	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
		notifyViews();
	}

	@Override
	public void addView(IView<ExpenseClaimList> v) {
		views.add(v);
	}

	@Override
	public void removeView(IView<ExpenseClaimList> v) {
		views.remove(v);
	}

	@Override
	public void notifyViews() {
		for (IView<ExpenseClaimList> v : views) {
			v.update(this);
		}
	}
	
	public int getCount() {
		return this.claims.size();
	}

	public void sort() {
		Comparator<? super ExpenseClaim> reverse_compare = new Comparator<ExpenseClaim>() {
			public int compare(ExpenseClaim lhs, ExpenseClaim rhs) {
				return rhs.compareTo(lhs);
			};
		};
		Collections.sort(claims, reverse_compare);
	}
	
	/**
	 * Replaces the old claim with the new claim.
	 * @param oldClaim
	 * @param newClaim
	 */
	public void updateExpenseClaim(ExpenseClaim oldClaim, ExpenseClaim newClaim) {
		int index = claims.indexOf(oldClaim);
		claims.set(index, newClaim);
	}
}
