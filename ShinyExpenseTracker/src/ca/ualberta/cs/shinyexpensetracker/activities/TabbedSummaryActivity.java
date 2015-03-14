package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.SectionsPagerAdapter;

// Source: https://github.com/astuetz/PagerSlidingTabStrip
// on March 11 2015
//http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/
//on March 12 2015

public class TabbedSummaryActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabbed_summary);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		context = getBaseContext();
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i, context))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabbed_summary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	/**
	 * Force selects the summary tab 
	 * @return the corresponding fragment.
	 */
	public ClaimSummaryFragment selectClaimSummaryTab() {
		// Select the 0'th tab
		getActionBar().selectTab(getActionBar().getTabAt(0));
		return (ClaimSummaryFragment) getCurrentFragment();
	}

	/**
	 * Force selects the expense list tab 
	 * @return the corresponding fragment.
	 */
	public ExpenseItemListFragment selectExpenseListTab() {
		// Select the 1st tab
		getActionBar().selectTab(getActionBar().getTabAt(1));
		return (ExpenseItemListFragment) getCurrentFragment();
	}

	/**
	 * Force selects the destination list tab
	 * @return the corresponding fragment.
	 */
	public DestinationsListFragment selectDestinationListTab() {
		// Select the 2nd tab
		getActionBar().selectTab(getActionBar().getTabAt(2));
		return null;
	}
	
	/**
	 * @return The currently visible fragment.
	 */
	public Fragment getCurrentFragment() {
		// http://stackoverflow.com/questions/18609261/getting-the-current-fragment-instance-in-the-viewpager
		// March 13, 2015
		int index = mViewPager.getCurrentItem();
		SectionsPagerAdapter adapter = ((SectionsPagerAdapter) mViewPager.getAdapter());
		Fragment fragment = adapter.getFragment(index);
		return fragment;
	}
}
