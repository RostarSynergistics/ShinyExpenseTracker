/**  This Activity tests the GeolocationView activity
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


package ca.ualberta.cs.shinyexpensetracker.test;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ca.ualberta.cs.shinyexpensetracker.activities.GeolocationViewActivity;
import ca.ualberta.cs.shinyexpensetracker.activities.MapViewActivity;

public class GeolocationViewTest extends
		ActivityInstrumentationTestCase2<GeolocationViewActivity> {

	public GeolocationViewTest() {
		super(GeolocationViewActivity.class);
	}


	private GeolocationViewActivity geolocationViewActivity;
	private Instrumentation instrumentation;
	private Button setGeolocationAutomatically;
	private Button setGeolocationUsingMap;
	
	public GeolocationViewTest(Class<GeolocationViewActivity> activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
		super.setUp();

		
		geolocationViewActivity = getActivity();
		instrumentation = getInstrumentation();
		setGeolocationAutomatically = (Button)geolocationViewActivity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.automaticGeolocationSetupButton);
		setGeolocationUsingMap = (Button)geolocationViewActivity.findViewById(ca.ualberta.cs.shinyexpensetracker.R.id.mapAssistedGeolocationSetupButton);
	}

	/**
	 * Test if the text view contains correct values after pressing Set Automatically Using GPS
	 */
	public void testGeolocationFetch() {
		LocationManager lm = (LocationManager) instrumentation.getTargetContext().getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		assertNotNull("there is no last known location", loc);
		
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				setGeolocationAutomatically.performClick();
			}
		});
		assertTrue("did not finish activity", geolocationViewActivity.isFinishing());
	}
	
	/**
	 * Test if MapViewActivity is launched after pressing Set Location Using Map
	 */
	public void testProperActivityLaunched() {
		ActivityMonitor am = getInstrumentation().addMonitor(MapViewActivity.class.getName(), null, true);
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				setGeolocationUsingMap.performClick();
			}
		});
		assertEquals("MapViewActivity is not launched", am.getHits(), 1);
	}
}
