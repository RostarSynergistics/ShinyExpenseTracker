package ca.ualberta.cs.shinyexpensetracker.decorators;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimListViewer;
import ca.ualberta.cs.shinyexpensetracker.models.Tag;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class ExpenseListTagFilter extends ExpenseClaimFilter implements ExpenseClaimListViewer {

	private ArrayList<ExpenseClaim> cachedClaims;
	private TagList filter;

	public ExpenseListTagFilter() {
		filter = new TagList();
	}

	public ExpenseListTagFilter(TagList tags) {
		filter = tags;
	}

	@Override
	public ExpenseClaim getClaimAtPosition(int position) {
		return getClaims().get(position);
	}

	@Override
	public ArrayList<ExpenseClaim> getClaims() {
		if (cachedClaims == null) {
			// Filter claims that don't have the matching tag
			cachedClaims = new ArrayList<ExpenseClaim>();
			for (ExpenseClaim claim : source.getClaims()) {
				boolean pass = true;
				// Check if the claim has any tags in the filter
				for (Tag tag : filter.getTags()) {
					if (claim.getTagList().contains(tag)) {
						pass = false;
						break;
					}
				}
				// If it doesn't, it can be added.
				if (pass) {
					cachedClaims.add(claim);
				}
			}
		}
		return cachedClaims;
	}

	@Override
	public int getCount() {
		return getClaims().size();
	}

	@Override
	protected void onDatasetChanged() {
		super.onDatasetChanged();
		cachedClaims = null;
	}
}
