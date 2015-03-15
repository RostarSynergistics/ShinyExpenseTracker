package ca.ualberta.cs.shinyexpensetracker;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * This class extends BaseAdapter to display the expense totals of a claim
 * 
 * @author Sarah Morris
 */
public class ExpenseTotalsAdapter extends BaseAdapter implements ListAdapter {
	private ArrayList<String> totals;
	private ExpenseClaim claim;
	private Context context;
	
	public ExpenseTotalsAdapter(ExpenseClaim claim, Context context) {
		super();
		this.claim = claim;
		this.context = context;
		totals = new ArrayList<String>();
		createExpenseTotals();
	}
	
	@Override
	public int getCount() {
		return totals.size();
	}

	@Override
	public Object getItem(int position) {
		return totals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// This function gets called on each item so that we have something to put
		// into a list view.
		
		if (convertView == null) {
			// Need a new item view
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		// This is the textview we're setting the value for
		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		// Fetch the total for this position
		text.setText( getItem(position).toString() );
		
		// Return the converted view.
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		createExpenseTotals();
	}
	
	/**
	 * Generates the list of expense item totals
	 */
	private void createExpenseTotals() {
		ArrayList<ExpenseItem> expenses = claim.getExpenses();
		int cad = 0, usd = 0, gbp = 0, chf = 0, cny = 0, eur = 0, jpy = 0;
		for (int i = 0; i < expenses.size(); i++) {
			switch (expenses.get(i).getCurrency()) {
			case CAD:
				cad += expenses.get(i).getAmountSpent().intValue();
				break;
			case USD:
				usd += expenses.get(i).getAmountSpent().intValue();
				break;
			case GBP:
				gbp += expenses.get(i).getAmountSpent().intValue();
				break;
			case CHF:
				chf += expenses.get(i).getAmountSpent().intValue();
				break;
			case CNY:
				cny += expenses.get(i).getAmountSpent().intValue();
				break;
			case EUR:
				eur += expenses.get(i).getAmountSpent().intValue();
				break;
			case JPY:
				jpy += expenses.get(i).getAmountSpent().intValue();
				break;
			default:
				break;
			}
		}
		totals = new ArrayList<String>();
		if (cad != 0){
			totals.add(Currency.CAD + " " + cad);
		} 
		if (usd != 0) {
			totals.add(Currency.USD + " " + usd);
		} 
		if (gbp != 0) {
			totals.add(Currency.GBP + " " + gbp);
		} 
		if (chf != 0) {
			totals.add(Currency.CHF + " " + chf);
		} 
		if (cny != 0) { 
			totals.add(Currency.CNY + " " + cny);
		} 
		if (eur != 0) {
			totals.add(Currency.EUR + " " + eur);
		} 
		if (jpy != 0) {
			totals.add(Currency.JPY + " " + jpy);
		}
	}
}
