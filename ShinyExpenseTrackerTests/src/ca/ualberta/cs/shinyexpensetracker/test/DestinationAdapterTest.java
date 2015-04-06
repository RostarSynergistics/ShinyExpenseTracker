package ca.ualberta.cs.shinyexpensetracker.test;

import java.util.Date;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.adapters.DestinationItemAdapter;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

/**
 * Test suite for the DestinationAdapter. This
 * adapter is responsible for displaying destinations
 * in lists that request it.
 */
public class DestinationAdapterTest extends AndroidTestCase {
	private DestinationItemAdapter adapter;
	
	private ExpenseClaim claim;
	
	private Destination hollywood;
	private Destination classyHotel;
	private Destination bollywood;

	/**
	 * Setup for each test. Creates three expenses
	 * but does not add them to the claim. Adapter
	 * is set up to watch the claim. 
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Create our own amazing claim
		claim = new ExpenseClaim(
				"The Amazing Claim",
				new Date(19860607),
				new Date(19860809)
				);
		
		// Create some expenses
		hollywood = new Destination("Hollywood", "I'm famous!", new Coordinate(34.1, -118.33));
		classyHotel = new Destination("Geneva", "I have toooooo much monneeeeyyy!!", new Coordinate(46.2, 6.15));
		bollywood = new Destination("Bollywood", "I've always wanted to be in a musical", new Coordinate(18.975, 72.8258));
		
		// Set up the thing we're trying to test
		adapter = new DestinationItemAdapter(claim, getContext());
	}

	/**
	 * Check that adding an item adds the item to the adapter
	 */
	public void testGetItem() {
		// Add the destination
		claim.addDestination(hollywood);
		adapter.notifyDataSetChanged();
		assertEquals(hollywood, adapter.getItem(0));
		// Easy peasy.
	}
	
	/**
	 * Check that the position is the expected position
	 * when the item is added
	 */
	public void testGetItemId() {
		claim.addDestination(classyHotel);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getItemId(0));
	}
	
	/**
	 * Check that the adapter can count the same as the claim does
	 */
	public void testGetCount() {
		// Add just one item
		claim.addDestination(hollywood);
		adapter.notifyDataSetChanged();
		assertEquals(1, adapter.getCount());

		// Add many an item
		claim.addDestination(classyHotel);
		claim.addDestination(bollywood);
		adapter.notifyDataSetChanged();
		assertEquals(3, adapter.getCount());
		
		// Remove an item
		claim.removeDestination(hollywood);
		adapter.notifyDataSetChanged();
		assertEquals(2, adapter.getCount());
		
		// Remove all items
		claim.removeDestination(bollywood);
		claim.removeDestination(classyHotel);
		adapter.notifyDataSetChanged();
		assertEquals(0, adapter.getCount());
		
	}

	/**
	 * Check that adding then removing doesn't change what
	 * we expect to see (consistent display)
	 */
	public void testConsistentGetItem() {
		claim.addDestination(hollywood);
		claim.addDestination(classyHotel);
		claim.removeDestination(hollywood);
		adapter.notifyDataSetChanged();
		
		assertEquals(classyHotel, adapter.getItem(0));
	}
	
	/**
	 * Checks that the item data in the fields are what we expect
	 */
	public void testItemData() {
		claim.addDestination(bollywood);
		adapter.notifyDataSetChanged();
		View view = adapter.getView(0, null, null);

		TextView viewName = (TextView) view.findViewById(R.id.destinationItemName);

		assertEquals(viewName.getText().toString(), bollywood.getName());
	}
}
