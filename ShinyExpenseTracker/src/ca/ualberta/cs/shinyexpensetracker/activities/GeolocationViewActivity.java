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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import ca.ualberta.cs.shinyexpensetracker.R;

public class GeolocationViewActivity extends Activity {

	private LocationManager lm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geolocation_view);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
	
	public void clickSetGeolocationAutomatically(View v) {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
	}
	
	public void clickSetGeolocationUsingMap(View v) {
		lm.removeUpdates(listener);
		// TODO: launch OpenStreetMapsActivity for result
	}
	/**
	 * Location listener that fires every time a location update is requested.
	 * Updates geolocation in the text view until the listener is unbound from
	 * its location manager  
	 */
	private final LocationListener listener = new LocationListener() {
		public void onLocationChanged (Location location) {
			TextView geolocationValue = (TextView) findViewById(R.id.geolocationValue);
			if (location != null) {
				double lattitude = location.getLatitude();
				double longitude = location.getLongitude();
				String geolocationValueText = "Latitude: " + String.valueOf(lattitude) + "\n " 
											+ "Longitude: " + String.valueOf(longitude);
				geolocationValue.setText(geolocationValueText);
				geolocationValue.invalidate();
			} else {
				geolocationValue.setText("Cannot get the location");
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
