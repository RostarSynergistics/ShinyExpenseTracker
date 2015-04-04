package ca.ualberta.cs.shinyexpensetracker.test;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.shinyexpensetracker.R;
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
	private MapView map;
	
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		
		Intent mapViewIntent = new Intent();
		mapViewIntent.putExtra("latitude", 128.0);
		mapViewIntent.putExtra("longitude", 64.0);
		mapViewIntent.putExtra("requestCode", MapViewActivity.SET_GEOLOCATION);
		setActivityIntent(mapViewIntent);
		
		mapViewActivity = getActivity();
		map = (MapView) mapViewActivity.findViewById(R.id.map);
	}
	
	public void testSingleTapEventHandler  () {
		final GeoPoint p = new GeoPoint(128.0, 64.0);
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				mapViewActivity.singleTapConfirmedHelper(p);
			}
		});
		assertEquals("latitude wrong", mapViewActivity.getLatitude(), p.getLatitude());
		assertEquals("latitude wrong", mapViewActivity.getLongitude(), p.getLongitude());
		
		Marker marker = mapViewActivity.getLastMarker();
		GeoPoint markerP = marker.getPosition();
		assertEquals("marker latitude wrong", markerP.getLatitude(), p.getLatitude());
		assertEquals("marker latitude wrong", markerP.getLongitude(), p.getLongitude());
	}

	public void testPopupShowingOnLongClick() {
		final GeoPoint p = new GeoPoint(128.0, 64.0);
		instrumentation.runOnMainSync(new Runnable() {
			public void run() {
				mapViewActivity.longPressHelper(p);
			}
		});
		assertTrue(mapViewActivity.askSaveLocation(p.getLatitude(), p.getLongitude()).isShowing());
	}
}
