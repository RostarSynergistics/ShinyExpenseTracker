package ca.ualberta.cs.shinyexpensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * Adapter for the list Views that display Destinations.
 * It fills the destination_list_item.xml layout file to display
 * Destination Items.
 * 
 * You can use this in place of an ArrayAdapter for Destinations.
 * This gives better flexibility for the adapter.
 * 
 * Example usage:
 * public class MyActivity extends Activity) {
 *  
 *  public setupListView() {
 *  	adapter = new Adapter( claimToWatch, this ) 
 * 		listview.setAdapter(adapter);
 *		...
 * }
 *
 */
public class DestinationItemAdapter extends BaseAdapter implements ListAdapter {
	private ExpenseClaim claim;
	private Context context;

	/**
	 * @param claim Claim to fetch expenses from
	 * @param context activity context
	 */
	public DestinationItemAdapter(ExpenseClaim claim, Context context) {
		this.claim = claim;
		this.context = context;
	}

	@Override
	public int getCount() {
		return claim.getDestinationCount();
	}

	@Override
	public Destination getItem(int position) {
		return claim.getDestination(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Are we recycling an existing view?
		if (convertView == null) {
			// No.
			// Inflate a new one and store it in convertView
			convertView = LayoutInflater.from(context).inflate(R.layout.destination_list_item, parent, false);
		} else {
			// Yes.
			// Recycle the existing view
		}
		
		// Fetch the text views that we're going to fill
		TextView destinationName = (TextView) convertView.findViewById(R.id.destinationItemName);
		
		// Get the destination context for this view
		Destination destination = getItem(position);
		
		// Fill in the values
		destinationName.setText(destination.getName().toString());

		// Return the converted view
		return convertView;
	}

}
