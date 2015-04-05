/**  This Activity lets Claimant set on a map by putting a marker.
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
import ca.ualberta.cs.shinyexpensetracker.framework.Application;
import ca.ualberta.cs.shinyexpensetracker.framework.ExpenseClaimController;
import ca.ualberta.cs.shinyexpensetracker.models.Coordinate;
import ca.ualberta.cs.shinyexpensetracker.models.Destination;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseItem;
import ca.ualberta.cs.shinyexpensetracker.models.User;

public class MapViewActivity extends Activity implements MapEventsReceiver {

	// constants to compare which activity called this one
	// the first means setting geolocation for Claimant, expense item or Destination
	// second means displaying locations stored on the device
	static public final int SET_GEOLOCATION = 1;
	static public final int DISPLAY_GEOLOCATIONS = 2;
	private static final Coordinate NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES = new Coordinate(39.03808, 125.7296);
	
	private Coordinate coordinate = new Coordinate();
	private int requestCode;
	private MapView map;
	private Marker lastMarker = null;
	private User user = Application.getUser();
	private ExpenseClaimController controller;
	
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public Marker getLastMarker() {
		return lastMarker;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Map initialization taken from the osmdroid bonus pack tutorials
		// at https://code.google.com/p/osmbonuspack/wiki/Tutorial_0
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		map = (MapView) findViewById(R.id.map);
		map.setTileSource(TileSourceFactory.MAPNIK);
		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
			map.getOverlays().add(0, mapEventsOverlay);
			requestCode = intent.getIntExtra("requestCode", 0);
			if (requestCode == SET_GEOLOCATION) {
				double latitude = intent.getDoubleExtra("latitude", NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES.getLatitude());
				double longitude = intent.getDoubleExtra("longitude", NORTH_KOREA_CONCENTRATION_CAMP_COORDINATES.getLongitude());
				coordinate.setLatitude(latitude);
				coordinate.setLongitude(longitude);
				
				GeoPoint startPoint = new GeoPoint(coordinate.getLatitude(), coordinate.getLongitude());
				IMapController mapController = map.getController();
				mapController.setZoom(18);
				mapController.setCenter(startPoint);
			}
			else if (requestCode == DISPLAY_GEOLOCATIONS) {
				
				
				// get all claims
				controller = Application.getExpenseClaimController();
				ExpenseClaimList claimList = controller.getExpenseClaimList();
				
				// put home geolocation on map
				GeoPoint startPoint = null;
				IMapController mapController = map.getController();
				coordinate = user.getHomeGeolocation();
				if (coordinate != null) {
					startPoint = new GeoPoint(coordinate.getLatitude(), coordinate.getLongitude());
					Marker newMarker = new Marker(map);
					newMarker.setPosition(startPoint);
					newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
				}
				// no time to implement Iterable interface!
				for (int i = 0; i < claimList.getCount(); i++) {
					ExpenseClaim claim = claimList.getClaim(i);
					// put destinations' geolocations on the map
					for (Destination dest: claim.getDestinations()) {
						Coordinate loc = dest.getGeolocation();
						GeoPoint destPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
						Marker destMarker = new Marker(map);
						destMarker.setPosition(destPoint);
						destMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
						map.getOverlays().add(destMarker);
					}
					//put expense items' geolocations on the map
					for (ExpenseItem item: claim.getExpenseItems()) {
						Coordinate loc = item.getGeolocation();
						if (loc != null) {
							GeoPoint itemPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
							Marker itemMarker = new Marker(map);
							itemMarker.setPosition(itemPoint);
							itemMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
							map.getOverlays().add(itemMarker);
						}
					}
				}
				mapController.setZoom(3);
				
				if (startPoint != null) {
					// center at home geolocation, if there is any
					mapController.setCenter(startPoint);
				}
				else {
					// otherwise, at default location
					mapController.setCenter(new GeoPoint(0.0, 0.0));
				}
				map.invalidate();
			}
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

	/**
	 * Handle long tap.
	 * Launch prompt asking user if they want to save the chosen location to the device
	 */
	@Override
	public void onBackPressed(){
		//if (requestCode == DISPLAY_GEOLOCATIONS || lastMarker == null) {
			//setResult(ExpenseClaimListActivity.RESULT_CANCELED, new Intent());
			//finish();
		//}
		//Toast.makeText(this, Boolean.toString(requestCode == DISPLAY_GEOLOCATIONS || lastMarker == null), Toast.LENGTH_LONG).show();
		if (requestCode != DISPLAY_GEOLOCATIONS && lastMarker != null) {	
			GeoPoint position = lastMarker.getPosition();
			askSaveLocation(position.getLatitude(), position.getLongitude());
		}
		else {
			setResult(ExpenseClaimListActivity.RESULT_CANCELED, new Intent());
			finish();
		}
	}

	/**
	 * Handle short tap.
	 * Display a marker on the selected coordinates and generate a Toast telling the chosen location
	 */
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
	
	/**
	 * Prompt asking user if they want to save the chosen location to the device
	 */
	public AlertDialog askSaveLocation(final double latitude, final double longitude) {
		// Alert Dialog (Mar 7, 2015):
		// http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
		// http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog

		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Location saving")
				.setMessage("Save this location:\n\tLatitude: " + String.valueOf(latitude) + "\n\tLongitude: " + String.valueOf(longitude) + "?")
				// If OK, return to parent activity. (Positive action);
				.setPositiveButton("Save", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Intent geolocationResultIntent = new Intent();
							geolocationResultIntent.putExtra("latitude", latitude);
							geolocationResultIntent.putExtra("longitude", longitude);
							setResult(GeolocationViewActivity.RESULT_OK, geolocationResultIntent);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						dialog.dismiss();
						finish();
					}
				})
				// If cancel, do nothing
				.setNeutralButton("Back to Map", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
						dialog.dismiss();
					}
				})
				.setNegativeButton("Don't Save", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Don't save and go back to parent activity
						try {
							setResult(ExpenseClaimListActivity.RESULT_CANCELED, new Intent());
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						dialog.dismiss();
						finish();
					}
				}).create();

		dialog.show();
		return dialog;
	}

	@Override
	public boolean longPressHelper(GeoPoint arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
