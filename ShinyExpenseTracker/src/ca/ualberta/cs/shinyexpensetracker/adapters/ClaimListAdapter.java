// Sourced (Mar 8, 2015)
//   http://www.piwai.info/android-adapter-good-practices/
package ca.ualberta.cs.shinyexpensetracker.adapters;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.decorators.ExpenseClaimFilter;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

/**
 * This class extends the BaseAdapter to display and sort the claims with their
 * necessary information.
 */
public class ClaimListAdapter extends BaseAdapter {
	private final SimpleDateFormat dateFormat;
	private final Context context;

	private ExpenseClaimList claims;
	private ExpenseClaimFilter filteredClaims;
	
	public ClaimListAdapter(Context context) {
		super();
		this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
		this.context = context;
		this.claims = Application.getExpenseClaimController().getExpenseClaimList();
		this.filteredClaims = new ExpenseClaimFilter().decorate(claims);
	}

	@Override
	public int getCount() {
		return filteredClaims.getCount();
	}

	@Override
	public ExpenseClaim getItem(int position) {
		// gets called to build the list
		return filteredClaims.getClaimAtPosition(position);
	}

	@Override
	public long getItemId(int position) {
		// called to get the item that was clicked
//		return filteredClaims.getSourceIndex(getItem(position));
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		TextView name;
		TextView dates;
		TextView status;
		TextView tags;
		TextView destination;
		// Are we recycling?
		if (convertView == null) {
			// No.
			// Get a layout that we can use.
			convertView = LayoutInflater.from(context).inflate(R.layout.claim_list_item, parent, false);

			// Build a viewholder
			name = (TextView) convertView.findViewById(R.id.claimName);
			dates = (TextView) convertView.findViewById(R.id.claimDateRange);
			status = (TextView) convertView.findViewById(R.id.claimStatus);
			tags = (TextView) convertView.findViewById(R.id.claimTags);
			destination = (TextView) convertView.findViewById(R.id.claimDestination);

			// Tag the convert view so we can reuse this in the future
			vh = new ViewHolder(name, dates, status, tags, destination);
			convertView.setTag(vh);
		} else {
			// Yes.
			// Get the held viewholder so we can make changes
			vh = (ViewHolder) convertView.getTag();
			name = vh.name;
			dates = vh.dates;
			status = vh.status;
			tags = vh.tags;
			destination = vh.destination;
		}

		ExpenseClaim claim = getItem(position);

		// Name and status can't be null
		name.setText(claim.getName());
		status.setText(claim.getStatus().getText());
				
		LinearLayout background = (LinearLayout) convertView.findViewById(R.id.claimListItemBackground);
		background.setBackgroundColor(claim.getColor());

		// Start date can't be null, but end date might be.
		if (claim.getEndDate() == null) {
			dates.setText(dateFormat.format(claim.getStartDate()));
		} else {
			dates.setText(dateFormat.format(claim.getStartDate()) + " to " + dateFormat.format(claim.getEndDate()));
		}
		// Tag might be null
		if (claim.getTagList() == null) {
			tags.setText(null);
		} else {
			tags.setText(claim.getTagList().toString());
		}

		return convertView;
	}

	// ViewHolders are very nice.
	/**
	 * This holds the views related to a claim so that the view can be recycled
	 * efficiently.
	 */
	private static class ViewHolder {
		public final TextView name;
		public final TextView dates;
		public final TextView status;
		public final TextView tags;
		public final TextView destination;

		public ViewHolder(TextView name, TextView dates, TextView status, TextView tags, TextView destination) {
			super();
			this.name = name;
			this.dates = dates;
			this.status = status;
			this.tags = tags;
			this.destination = destination;
		}
	}
	
	/**
	 * Filters the claim list adapter.  
	 * 
	 * The supplied filter must be a class of an ExpenseClaimFilter
	 * object.
	 * 
	 * example:
	 * 		adapter.applyFilter( ExpenseClaimSortFilter.class );
	 * 
	 * @param filter the ExpenseClaimFilter class to apply.
	 */
	public void applyFilter(ExpenseClaimFilter filter) {
		filteredClaims = filter.decorate(filteredClaims);
		notifyDataSetChanged();
	}
	
	/**
	 * Clears any filters that were added by applyFilter
	 */
	public void clearFilters() {
		this.filteredClaims = new ExpenseClaimFilter().decorate(claims);
		notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		filteredClaims.update(claims);
	}
}
