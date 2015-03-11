package ca.ualberta.cs.shinyexpensetracker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ExpenseClaimList extends Model<ExpenseClaimList> {
	private ArrayList<ExpenseClaim> claims;
	
	/* FIXME
	 * UML says 0..* ExpenseClaimList's are composed of
	 * 			1 ExpenseClaimController,
	 * and that 1 ExpenseClaimList is composed of ? Expense Claims
	 */
	
	public ExpenseClaimList() {
		claims = new ArrayList<ExpenseClaim>();
	}

	// FIXME UML says this takes no args
	public ExpenseClaim getClaim(int index) {
		return claims.get(index);
	}
	
	public void addClaim(ExpenseClaim claim) {
		claims.add(claim);
		notifyViews();
	}
	
	// FIXME What does this do?
	// Assuming it takes the argument claim.
	public void editClaim(ExpenseClaim claim) {
		notifyViews();
	}
	
	public void removeClaim(ExpenseClaim claim) {
		claims.remove(claim);
		notifyViews();
	}
<<<<<<< HEAD
	
	public int size(){
		return claims.size();
	}
	
	public ArrayList<ExpenseClaim> getClaims(){
		return claims;
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
=======
>>>>>>> 30e28fb247e27e7d6f08c6610ef06b7b3fea862b
	
	public ArrayList<ExpenseClaim> getAllClaims() {
		return this.claims;
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
}
