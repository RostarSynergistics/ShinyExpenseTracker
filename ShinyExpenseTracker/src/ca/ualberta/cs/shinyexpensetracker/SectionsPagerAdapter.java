package ca.ualberta.cs.shinyexpensetracker;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import ca.ualberta.cs.shinyexpensetracker.activities.ClaimSummaryFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.DestinationsListFragment;
import ca.ualberta.cs.shinyexpensetracker.activities.ExpenseItemListFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

// http://stackoverflow.com/questions/18609261/getting-the-current-fragment-instance-in-the-viewpager
// March 13
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	private SparseArray<Fragment> mPageReferenceMap;
	
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		mPageReferenceMap = new SparseArray<Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		Fragment frag;
		switch (position) {
		case 0:
			//ClaimSummary activity
			frag = ClaimSummaryFragment.newInstance(position+1);
			break;
		case 1:
			//ExpenseItemList activity
			frag = ExpenseItemListFragment.newInstance(position+1);
			break;
		case 2:
			//DestinationsList activity
			frag = DestinationsListFragment.newInstance(position+1);
			break;
		default:
			throw new RuntimeException("No such tab");
		}
		// Remember this page
		mPageReferenceMap.put(position, frag);
		return frag;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mPageReferenceMap.delete(position);
	}
	
	public Fragment getFragment(int position) {
		return mPageReferenceMap.get(position);
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