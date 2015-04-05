/**  This Activity lets Claimant set geolocation either automatically,
 * 	using internal GPS module, or on a map by putting a marker.
 * 	Geolocations can be chosen as a home geolocation for a user,
 * 	while filling out information on a destination (mandatory),
 * 	or while filling out information on an expense item (optional)
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
 * Issues #157, #158
 */

package ca.ualberta.cs.shinyexpensetracker.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;

public class GeolocationViewActivity extends Activity {

	private LocationManager lm;
	private Coordinate coordinates = new Coordinate();
	private Coordinate coordinatesUpdating = new Coordinate();
	
	public static final int SET_GEOLOCATION = 1;
	private static final Coordinate NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES = new Coordinate(39.03808, 125.7296);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geolocation_view);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null) {
			double latitudeUpdating = loc.getLatitude();
			double longitudeUpdating = loc.getLongitude();
			coordinatesUpdating.setLatitude(latitudeUpdating);
			coordinatesUpdating.setLongitude(longitudeUpdating);
		}
		// set up 'listener' to fire at most once every second
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geolocation_view, menu);
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
	
	/**
	 * On click of Set Automatically Using GPS, get the constantly updating coordinate values
	 * and save them to the "release version" values
	 */
	public void clickSetGeolocationAutomatically(View v) {
		
		coordinates.setLatitude(coordinatesUpdating.getLatitude());
		coordinates.setLongitude(coordinatesUpdating.getLongitude());
		if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {
			Toast.makeText(this, "GPS positioning not enabled.\nEnable GPS positioning or enter the coordiantes manually using the map", Toast.LENGTH_LONG).show();
		}
		else {
			returnCoordinatesToParentActivity();
		}
	}
	
	/**
	 * On click of Set Geolocation Using Map, navigate to the MapViewActivity to let the user
	 * choose geolocation on an interactive map
	 */
	public void clickSetGeolocationUsingMap(View v) {
		Intent mapViewIntent = new Intent(GeolocationViewActivity.this, MapViewActivity.class);
		mapViewIntent.putExtra("latitude", coordinatesUpdating.getLatitude());
		mapViewIntent.putExtra("longitude", coordinatesUpdating.getLongitude());
		mapViewIntent.putExtra("requestCode", SET_GEOLOCATION);
		startActivityForResult(mapViewIntent, SET_GEOLOCATION);
	}
	
	/**
	 * Accept result from the map activity and immediately return it
	 * to the parent activity
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check result is ok
		lm.removeUpdates(listener);
		if (resultCode == RESULT_OK) {
			double latitude = data.getDoubleExtra("latitude", NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES.getLatitude());
			double longitude = data.getDoubleExtra("longitude", NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES.getLongitude());
			coordinates.setLatitude(latitude);
			coordinates.setLongitude(longitude);
			returnCoordinatesToParentActivity();
		}
		else {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
		}
	}
	
	/**
	 * Finish the activity and send saved coordinates to the parent activity
	 */
	private void returnCoordinatesToParentActivity() {
		Intent geolocationResultIntent = new Intent(GeolocationViewActivity.this, ExpenseClaimListActivity.class);
		geolocationResultIntent.putExtra("latitude", coordinates.getLatitude());
		geolocationResultIntent.putExtra("longitude", coordinates.getLongitude());
		setResult(ExpenseClaimListActivity.RESULT_OK, geolocationResultIntent);
		finish();
	}
	
	/**
	 * Location listener that fires every time a location update is requested.
	 * Updates geolocation based on info from the GPS mdoule until 
	 * the listener is unbound from its location manager.
	 * The listener updates a different set of coordinate values because we don't want
	 * to update the TextView every time that the location is changed and we do want to save
	 * geolocation retrieved from the MapViewActivity  
	 */
	private final LocationListener listener = new LocationListener() {
		/*
		 * Adapted from joshua2ua's fork of MockLocationTester, file MockLocationTesterActivity.java on April 2, 2015
		 * source at: https://github.com/joshua2ua/MockLocationTester/blob/master/src/ualberta/cmput301/mocklocationtester/MockLocationTesterActivity.java
		 */
		public void onLocationChanged (Location location) {
			if (location != null) {
				coordinatesUpdating.setLatitude(location.getLatitude());
				coordinatesUpdating.setLongitude(location.getLongitude());
			}
		}
		public void onProviderDisabled (String provider) {
		}
		public void onProviderEnabled (String provider) {
		}
		public void onStatusChanged (String provider, int status, Bundle extras) {
		}
	};
}
