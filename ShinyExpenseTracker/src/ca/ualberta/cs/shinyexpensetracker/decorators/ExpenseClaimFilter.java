package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cs.shinyexpensetracker.framework.IView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimListViewer;

/**
 * This class is the superclass for all expense claim list filters and
 * decorators.
 * 
 * Subclasses must implement the getters for claims and count of claims.
 * Subclasses have access to the source object which they can use to get the
 * data from the thing they're decorating.
 * Specifically, they should override the getClaims() method,
 * and optionally the getClaim(index) and getCount().
 */

public class ExpenseClaimFilter extends ExpenseClaimList
		implements IView<ExpenseClaimList>, ExpenseClaimListViewer {
	protected ExpenseClaimList source = null;
	
	public final ExpenseClaimFilter decorate(ExpenseClaimList source) {

		if (this.source != null) {
			throw new RuntimeException("Cannot replace the decorator source");
		}
		this.source = source;
		source.addView(this);
		return this;
	}

	/**
	 * Decorates an ExpenseClaimFilter object with this object.
	 * Once decorated, this method cannot be called again.
	 * @param source The source object to decorate
	 * @return this object post decoration.
	 */
	public final ExpenseClaimFilter decorate(ExpenseClaimFilter source) {
		if (this.source != null) {
			throw new RuntimeException("Cannot replace the decorator source");
		}
		this.source = source;
		source.addView(this);
		return this;
	}
	
	@Override
	public void addClaim(ExpenseClaim claim) {
		source.addClaim(claim);
		onDatasetChanged();
	}

	@Override
	public void deleteClaim(UUID id) {
		source.deleteClaim(id);
		onDatasetChanged();
	}

	@Override
	public void update(ExpenseClaimList m) {
		onDatasetChanged();
	}

	/**
	 * Allows subclasses to be notified that the dataset has been changed and
	 * that they need to be updated.
	 * 
	 * It should be overridden if you need to know when the source object has
	 * changed.
	 */

	protected void onDatasetChanged() {}
	

	/**
	 * Returns a claim from the claimList based on index passed in
	 * @param index
	 * @return
	 */
	public ExpenseClaim getClaimAtPosition(int index) {
		return source.getClaims().get(index);
	}

	/**
	 * returns array list of claims
	 * @return
	 */
	public ArrayList<ExpenseClaim> getClaims() {
		return source.getClaims();
	}

	/**
	 * returns number of claims in claimList
	 * @return
	 */
	public int getCount() {
		return source.getCount();
	}
	
	/**
	 * Returns the real index of the item at 
	 * the given position in the filtered list.
	 * 
	 * This should be overloaded if data is filtered out
	 */
	public int getSourceIndex(ExpenseClaim claim) {
		return source.getClaims().indexOf(claim);
	}
}
