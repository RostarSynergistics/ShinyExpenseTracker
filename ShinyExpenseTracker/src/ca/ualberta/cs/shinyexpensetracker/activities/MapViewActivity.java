package ca.ualberta.cs.shinyexpensetracker.activities;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ca.ualberta.cs.shinyexpensetracker.R;

public class MapViewActivity extends Activity implements MapEventsReceiver {

	static public final int SET_GEOLOCATION = 1;
	static public final int DISPLAY_GEOLOCATIONS = 2;
	
	private double latitude;
	private double longitude;
	private int requestCode;
	private MapView map;
	private Marker lastMarker = null;
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Marker getLastMarker() {
		return lastMarker;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			latitude = intent.getDoubleExtra("latitude", 39.03808);
			longitude = intent.getDoubleExtra("longitude", 125.7296);
			requestCode = intent.getIntExtra("requestCode", 0);
			map = (MapView) findViewById(R.id.map);
			map.setTileSource(TileSourceFactory.MAPNIK);
			map.setBuiltInZoomControls(true);
			map.setMultiTouchControls(true);

			MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
			map.getOverlays().add(0, mapEventsOverlay);
			
			GeoPoint startPoint = new GeoPoint(latitude, longitude);
			IMapController mapController = map.getController();
			mapController.setZoom(18);
			mapController.setCenter(startPoint);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
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
	public boolean longPressHelper(GeoPoint p) {
		if (requestCode == DISPLAY_GEOLOCATIONS || lastMarker == null) {
			return false;
		}
		GeoPoint position = lastMarker.getPosition();
		askSaveLocation(position.getLatitude(), position.getLongitude());
		return true;
	}

	@Override
	public boolean singleTapConfirmedHelper(GeoPoint p) {
		if (requestCode == DISPLAY_GEOLOCATIONS) {
			return false;
		}
		if (lastMarker != null) {
			map.getOverlays().remove(lastMarker);
		}
		Toast.makeText(this, "Latitude: "+p.getLatitude()+"\nLongitude: "+p.getLongitude(), Toast.LENGTH_SHORT).show();
		Marker newMarker = new Marker(map);
		newMarker.setPosition(p);
		newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
		map.getOverlays().add(newMarker);
		lastMarker = newMarker;
		map.invalidate();
		return true;
	}
	
	public AlertDialog askSaveLocation(final double latitude, final double longitude) {
		// Alert Dialog (Mar 7, 2015):
		// http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
		// http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog

		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Location saving")
				.setMessage("Save this location:\n\tLatitude: " + String.valueOf(latitude) + "\n\tLongitude: " + String.valueOf(longitude) + "?")
				// If OK, return to parent activity. (Positive action);
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent geolocationResultIntent = new Intent();
							geolocationResultIntent.putExtra("latitude", latitude);
							geolocationResultIntent.putExtra("longitude", longitude);
							setResult(ExpenseClaimListActivity.RESULT_OK, geolocationResultIntent);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						dialog.dismiss();
						finish();
					}
				})
				// If cancel, do nothing
				.setNeutralButton("Cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
						dialog.dismiss();
					}
				}).create();

		dialog.show();
		return dialog;
	}
}
