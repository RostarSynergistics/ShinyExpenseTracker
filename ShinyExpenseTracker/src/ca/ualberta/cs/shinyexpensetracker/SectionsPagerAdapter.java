package ca.ualberta.cs.shinyexpensetracker;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.activities.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.DestinationsListFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemListFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		switch (position) {
		case 0:
			//ClaimSummary activity
			return ClaimSummaryFragment.newInstance(position+1);
		case 1:
			//ExpenseItemList activity
			return ExpenseItemListFragment.newInstance(position+1);
		case 2:
			//DestiantionsList activity
			return DestinationsListFragment.newInstance(position+1);
		default:
			throw new RuntimeException("No such tab");
		}
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	public CharSequence getPageTitle(int position, Context context) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getResources().getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getResources().getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return context.getResources().getString(R.string.title_section3).toUpperCase(l);
		default:
			throw new RuntimeException("No such tab");
		}
	}
}