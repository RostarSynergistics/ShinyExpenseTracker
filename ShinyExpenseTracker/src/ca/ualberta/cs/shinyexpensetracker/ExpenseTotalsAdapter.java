package ca.ualberta.cs.shinyexpensetracker;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem.Currency;

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
		
		// Fetch the createExpenseTotals total for this position
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
	public void createExpenseTotals() {
		BigDecimal cad = new BigDecimal(0);
		BigDecimal usd = new BigDecimal(0);
		BigDecimal gbp = new BigDecimal(0);
		BigDecimal chf = new BigDecimal(0);
		BigDecimal cny = new BigDecimal(0);
		BigDecimal eur = new BigDecimal(0);
		BigDecimal jpy = new BigDecimal(0);
		
		ExpenseItem expense;
		for (int i = 0; i < claim.getExpenseCount(); i++) {
			expense = claim.getExpense(i);
			switch (expense.getCurrency()) {
			case CAD:
				cad = cad.add(expense.getAmountSpent());
				break;
			case USD:
				usd = usd.add(expense.getAmountSpent());
				break;
			case GBP:
				gbp = gbp.add(expense.getAmountSpent());
				break;
			case CHF:
				chf = chf.add(expense.getAmountSpent());
				break;
			case CNY:
				cny = cny.add(expense.getAmountSpent());
				break;
			case EUR:
				eur = eur.add(expense.getAmountSpent());
				break;
			case JPY:
				jpy = jpy.add(expense.getAmountSpent());
				break;
			default:
				break;
			}
		}
		
		totals = new ArrayList<String>();
		if (!cad.equals(new BigDecimal(0))){
			totals.add(Currency.CAD + " " + cad.toString());
		} 
		if (!usd.equals(new BigDecimal(0))) {
			totals.add(Currency.USD + " " + usd.toString());
		} 
		if (!gbp.equals(new BigDecimal(0))) {
			totals.add(Currency.GBP + " " + gbp.toString());
		} 
		if (!chf.equals(new BigDecimal(0))) {
			totals.add(Currency.CHF + " " + chf.toString());
		} 
		if (!cny.equals(new BigDecimal(0))) { 
			totals.add(Currency.CNY + " " + cny.toString());
		} 
		if (!eur.equals(new BigDecimal(0))) {
			totals.add(Currency.EUR + " " + eur.toString());
		} 
		if (!jpy.equals(new BigDecimal(0))) {
			totals.add(Currency.JPY + " " + jpy.toString());
		}
	}
}
