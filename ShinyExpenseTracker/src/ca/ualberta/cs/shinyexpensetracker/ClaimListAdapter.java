/** 
 * This class extends the BaseAdapter to display and sort
 * the claims with their necessary information.
 * 
 *  Copyright (C) 2015  github.com/RostarSynergistics
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Issue #61
 * @author Tristan Meleshko
 */

// Sourced (Mar 8, 2015)
//   http://www.piwai.info/android-adapter-good-practices/
package ca.ualberta.cs.shinyexpensetracker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ClaimListAdapter extends BaseAdapter {
	private final SimpleDateFormat dateFormat;
	private final Context context;
	private ExpenseClaimController controller;
	
	public ClaimListAdapter(Context context) throws IOException {
		super();
		this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
		this.context = context;
		this.controller = new ExpenseClaimController(context);
	}

	@Override
	public int getCount() {
		return controller.getCount();
	}

	@Override
	public ExpenseClaim getItem(int position) {
		return controller.getExpenseClaim(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		TextView name;
		TextView dates;
		TextView status;
		TextView tags;
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
			
			// Tag the convert view so we can reuse this in the future
			vh = new ViewHolder(name, dates, status, tags);
			convertView.setTag(vh);
		} else {
			// Yes.
			// Get the held viewholder so we can make changes
			vh = (ViewHolder) convertView.getTag();
			name = vh.name;
			dates = vh.dates;
			status = vh.status;
			tags = vh.tags;
		}

		ExpenseClaim claim = getItem(position);
		
		// Name and status can't be null
		name.setText(claim.getName());
		status.setText(claim.getStatus().getText());
				
		
		// Start date can't be null, but end date might be.
		if (claim.getEndDate() == null) {
			dates.setText(dateFormat.format(claim.getStartDate()));			
		} else {
			dates.setText(
					dateFormat.format(claim.getStartDate())
					+ " to " +
					dateFormat.format(claim.getEndDate()));
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
	// This holds the views related to a claim so that
	// the view can be recycled efficiently.
	
	private static class ViewHolder {
		public final TextView name;
		public final TextView dates;
		public final TextView status;
		public final TextView tags;
		
		public ViewHolder(TextView name, TextView dates, TextView status,
				TextView tags) {
			super();
			this.name = name;
			this.dates = dates;
			this.status = status;
			this.tags = tags;
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		makeSortedList();
	}

	private void makeSortedList() {
		controller.sort();
	}
}
