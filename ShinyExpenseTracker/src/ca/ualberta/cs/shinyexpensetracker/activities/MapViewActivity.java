package ca.ualberta.cs.shinyexpensetracker.activities;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ca.ualberta.cs.shinyexpensetracker.R;

public class MapViewActivity extends Activity {

	private double latitude;
	private double longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			latitude = intent.getDoubleExtra("latitude", 39.03808);
			longitude = intent.getDoubleExtra("longitude", 125.7296);
			MapView map = (MapView) findViewById(R.id.map);
			map.setTileSource(TileSourceFactory.MAPNIK);
			map.setBuiltInZoomControls(true);
			map.setMultiTouchControls(true);

			GeoPoint startPoint = new GeoPoint(latitude, longitude);
			IMapController mapController = map.getController();
			mapController.setZoom(20);
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
}
