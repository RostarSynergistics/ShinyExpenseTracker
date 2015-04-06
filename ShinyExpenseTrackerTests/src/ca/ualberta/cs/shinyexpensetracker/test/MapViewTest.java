/**  This Activity tests the MapView activity
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

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import ca.ualberta.cs.shinyexpensetracker.activities.MapViewActivity;

public class MapViewTest extends ActivityInstrumentationTestCase2<MapViewActivity> {

	public MapViewTest(Class<MapViewActivity> activityClass) {
		super(activityClass);
	}

	public MapViewTest() {
		super(MapViewActivity.class);
	}
	
	private MapViewActivity mapViewActivity;
	private Instrumentation instrumentation;
	
	protected void setUp() throws Exception {
		super.setUp();
		// FIXME: #187
		//        The tests in this file pass, but they hang
		//		  when the entire suite is run.
		fail();
		
		Intent mapViewIntent = new Intent();
		mapViewIntent.putExtra("latitude", 64.0);
		mapViewIntent.putExtra("longitude", 128.0);
		mapViewIntent.putExtra("requestCode", MapViewActivity.SET_GEOLOCATION);
		setActivityIntent(mapViewIntent);
		
		instrumentation = getInstrumentation();
		mapViewActivity = getActivity();
	}
	
	public void testSingleTapEventHandler() {
		final GeoPoint p = new GeoPoint(64.0, 128.0);
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				mapViewActivity.singleTapConfirmedHelper(p);
			}
		});
		instrumentation.waitForIdleSync();
		assertEquals("latitude wrong", mapViewActivity.getCoordinate().getLatitude(), p.getLatitude());
		assertEquals("latitude wrong", mapViewActivity.getCoordinate().getLongitude(), p.getLongitude());
		
		Marker marker = mapViewActivity.getLastMarker();
		GeoPoint markerP = marker.getPosition();
		assertEquals("marker latitude wrong", markerP.getLatitude(), p.getLatitude());
		assertEquals("marker latitude wrong", markerP.getLongitude(), p.getLongitude());
	}

	public void testPopupShowingOnBackPress() {
		final GeoPoint p = new GeoPoint(64.0, 128.0);
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				mapViewActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			}
		});
		instrumentation.waitForIdleSync();
		assertTrue(mapViewActivity.askSaveLocation(p.getLatitude(), p.getLongitude()).isShowing());
	}
}
