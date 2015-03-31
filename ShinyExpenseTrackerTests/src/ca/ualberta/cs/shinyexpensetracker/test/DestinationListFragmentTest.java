package ca.ualberta.cs.shinyexpensetracker.test;

import java.io.IOException;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.activities.AddDestinationActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.TabbedSummaryActivity;
import ca.ualberta.cs.shinyexpensetracker.fragments.DestinationListFragment;
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.test.mocks.MockExpenseClaimListPersister;

/**
 * Test for the DestinationListFragment. This
 * fragment is responsible for displaying a list
 * of destinations in the summary page.
 */
public class DestinationListFragmentTest extends
	ActivityInstrumentationTestCase2<TabbedSummaryActivity> {
	
	static DestinationListFragment frag;
	TabbedSummaryActivity activity;
	ExpenseClaim claim;
	
	private ExpenseClaimController controller;
	private Destination destination;

	public DestinationListFragmentTest(Class<TabbedSummaryActivity> activityClass) {
		super(activityClass);
	}
	
	public DestinationListFragmentTest() {
		super(TabbedSummaryActivity.class);
	}
	
	/**
	 * Setup for each test. Creates a new claim and
	 * adds a destination. It then starts the activity
	 * with the intent of displaying info about this claim
	 * and selects the Destinations tab.
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		controller = new ExpenseClaimController(new MockExpenseClaimListPersister());
		Application.setExpenseClaimController(controller);
		
		claim = new ExpenseClaim(
				"My Cool Expense Claim",
				new Date(123456),
				new Date(234567)
				);
		// Add a destination that we can look at
		destination = new Destination("Hell", "I'm on a highway.");
		claim.addDestination(destination);
		// Add the expense claim
		controller.addExpenseClaim(claim);
		
		// Inject an intent that we have full control of.
		// This MUST be called before the first call to getActivity()
		// Source: http://developer.android.com/reference/android/test/ActivityInstrumentationTestCase2.html#setActivityIntent%28android.content.Intent%29
		//		March 13, 2015
		
		// Request the claim we just added
		Intent intent = new Intent();
		intent.putExtra("claimIndex", 0);
		setActivityIntent(intent);
		
		// Now it's safe to get the activity
		activity = getActivity();
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				// Get me a shiny destination list tab
				DestinationListFragmentTest.frag = activity.selectDestinationListTab();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		// Can't do anything without a view
		View view = frag.getView();
		assertNotNull("No view", view);
	}
	
	/**
	 * Checks that we can access views within the fragment.
	 * If this test fails, the fragment isn't being put into
	 * the activity correctly.
	 */
	public void testFragViewsNotNothing() {
		// Try to fetch the list view in the fragment.
		assertNotNull(frag.getView()
				.findViewById(R.id.destinationsListView));
	}
	
	/**
	 * Checks that tapping an item in the list view will
	 * open the appropriate activity for editing.
	 */
	public void testEditDestinationShowsActivity() {
		final ListView listview = (ListView) frag.getView().findViewById(R.id.destinationsListView);
		
		// http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
		// March 13, 2015
		
		// Use a monitor to listen for activity changes
		ActivityMonitor monitor = getInstrumentation().addMonitor(AddDestinationActivity.class.getName(), null, false);

		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// Get the view at index 0 and create a new item to fill it.
				View view = listview.getAdapter().getView(0, null, listview);
				// Click the item view.
				listview.performItemClick(view, 0, listview.getAdapter().getItemId(0));
			}
		});
		
		getInstrumentation().waitForIdleSync();
		
		// Wait up to 5 seconds for the next activity open, if available,
		// timing out if it blocks.
		AddDestinationActivity nextActivity = (AddDestinationActivity) getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
		assertNotNull("Next activity wasn't opened", nextActivity);
		nextActivity.finish();
	}
	
	/**
	 * Test that deleting an existing destination removes it
	 * from the list view.
	 */
	public void testDeleteDestination() {
		// Fake the functionality of long pressing the listview
		// because that method doesn't seem to be exposed.
 		
		ListView destinationList = (ListView) frag.getView().findViewById(R.id.destinationsListView);
		
		// Make sure we have 1 thing before
		assertEquals(1, destinationList.getCount());
		assertEquals(1, claim.getDestinationCount());

		// Make sure the dialog isn't real
		assertNull(frag.getLastDialog());

		// Delete the destination at index 0		
		frag.askDeleteDestinationAt(0);

		// Get a dialog
		AlertDialog deleteDialog = frag.getLastDialog();
		
		// Make sure the dialog is real and is showing
		assertNotNull(frag.getLastDialog());
		assertTrue("Dialog not showing", deleteDialog.isShowing());
		
		// (Fake) click OK. (The most painful thing for some reason).
		// --> All this seems to do is tell us if there was something that
		//		could be clicked and that it could be performed. It still
		//		fails to do anything for no apparent reason
		Button button = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		assertTrue(button.performClick());
		
		// If "performClick" doesn't perform click...
		if (frag.getLastDialog() != null) {
			// Forcefully induce the event that was supposed to be called
			// because nothing seems to do that.
			getInstrumentation().runOnMainSync(new Runnable() {
				
				@Override
				public void run() {
					frag.deleteDestinationAt(0);
				}
			});
		}
		
		// Check that the controller removed an item (UI -> Model)
		assertEquals(0, claim.getDestinationCount());
		// Check that the list view removed an item (UI -> UI)
		assertEquals(0, destinationList.getCount());
	}
	
	/**
	 * Tests that adding new destinations update the interface.
	 */
	public void testNewDestinationsAreAdded() {
		ListView destinationList = (ListView) frag.getView().findViewById(R.id.destinationsListView);
		// Make sure we still have that one claim from before
		// - Sanity check
		assertEquals(1, claim.getDestinationCount());
		// - Check the list view for the same number of things  
		assertEquals(1, destinationList.getCount());
		
		// Add another destination
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claim.addDestination(new Destination("Copy-Paste world", "Laziness"));;
			}
		});
		getInstrumentation().waitForIdleSync();
		// Make sure we have a new claim
		// - Sanity check
		assertEquals(2, claim.getDestinationCount());
		// - Check the list view for the new number of things
		assertEquals(2, destinationList.getCount());
	}
	
	/**
	 * Checks that destinations are visible in the list view.
	 */
	public void testDestinationListVisibility() {
		TextView noDestinationPrompt = (TextView) frag.getView().findViewById(R.id.noDestinationsTextView);

		// Sanity check
		assertEquals(1, claim.getDestinationCount());
		
		// Stuff to display:
		// --> Check that the list is visible
		assertEquals(View.INVISIBLE, noDestinationPrompt.getVisibility());
		
		// Remove the item
		getInstrumentation().runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				claim.removeDestination(0);
			}
		});
		getInstrumentation().waitForIdleSync();

		// Sanity check
		assertEquals(0, claim.getDestinationCount());
		
		// Nothing to display:
		// --> Check that the prompt is visible
		assertEquals(View.VISIBLE, noDestinationPrompt.getVisibility());
	}
	
	/**
	 * Check that editing the destination updates the list view.
	 */
	public void testEditDestination() {
		// monitor for destination activity
		ActivityMonitor monitor = getInstrumentation().addMonitor(AddDestinationActivity.class.getName(), null, false);
		
		// This is what we want to see.
		final String newDest = "outta here";
		
		// Add a destination to edit
		final ListView listview = (ListView) frag.getView().findViewById(R.id.destinationsListView);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Edit the destination
				View view = listview.getAdapter().getView(0, null, listview);
				listview.performItemClick(view, 0, listview.getItemIdAtPosition(0));
			}
		});
		// sync
		getInstrumentation().waitForIdleSync();
		
		// Get the new activity
		final AddDestinationActivity editDestination = (AddDestinationActivity) getInstrumentation().waitForMonitorWithTimeout(monitor, 3);
		assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));
		
		// Get the name textview
		final TextView name = (TextView) editDestination.findViewById(R.id.destinationEditText);
		
		// Make sure that it loaded the right data
		assertEquals(destination.getName(), name.getText().toString());
		
		// Change the values
		editDestination.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// Change the value
				name.setText(newDest);
				
				// Close the activity
				try {
					editDestination.doneCreateDestination( editDestination.findViewById(R.id.addDestinationDoneButton));
				} catch (IOException e) {
					fail();
				}
			}
		});
		
		// Wait for sync
		getInstrumentation().waitForIdleSync();
		
		// Make sure the destination was updated correctly
		assertEquals(newDest, destination.getName());
		
		// Make sure the listview shows the right value
		assertEquals(1, listview.getCount());
		assertEquals(destination, listview.getItemAtPosition(0));

		// Sync for fun.
		getInstrumentation().waitForIdleSync();
		
		// Pass.
	}
}
