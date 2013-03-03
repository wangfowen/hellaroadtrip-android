package com.example.launch.hackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.launch.hackathon.common.DirectionTags;
import com.example.launch.hackathon.common.GPSTracker;
import com.example.launch.hackathon.common.JSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;

public class MapView extends Activity {

	GoogleMap myMap;
	GPSTracker gps;

	JSONArray jArray;

	private static final int NO_OF_LOCATIONS = 25;

	JSONObject header = null;
	JSONObject response = null;

	ArrayList<HashMap<String, String>> locations;
	HashMap<String, HashMap<String, String>> merchantOverlays;

	String message;

	int i = 0;
	double lat;
	double lng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// locations = (ArrayList<HashMap<String, String>>)
		// getArguments().getSerializable("data");
		setContentView(R.layout.map_fragment);
		gps = new GPSTracker(this);
		createLayoutContent();

		decodePoly();
		//parseLocations();
	}

	public void createLayoutContent() {

		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment mySupportMapFragment = (MapFragment) myFragmentManager
				.findFragmentById(R.id.map);
		myMap = mySupportMapFragment.getMap();
		myMap.setMyLocationEnabled(true);

		CameraPosition camPos = new CameraPosition.Builder()
				.target(new LatLng(gps.getLatitude(), gps.getLongitude()))
				.zoom(12.8f).build();
		CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
		myMap.moveCamera(camUpdate);

		myMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				i++;
				message = "Point: " + i + " at " + point.toString();
				Marker marker = myMap.addMarker(new MarkerOptions().position(
						point).title(point.toString()));
				Polyline line = myMap.addPolyline(new PolylineOptions()
						.add(new LatLng(gps.getLatitude(), gps.getLongitude()),
								marker.getPosition()).width(5).color(Color.RED));
				// Toast.makeText(getActivity(),
				// "New marker at: "+point.toString(),
				// Toast.LENGTH_LONG).show();
			}
		});

		// drawMerchantLocations();

		Button gpsLocBtn = (Button) this.findViewById(R.id.gps_loc_btn);
		gpsLocBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLocation(v);
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Toast.makeText(this, "Selected Item: " + item.getTitle(),
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			Toast.makeText(this, "Selected Item: none", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	}

	private void parseLocations() {
		AssetManager assetManager = getAssets();
		InputStream input;
		ArrayList<HashMap<String, JSONObject>> directions;

		try {
			input = assetManager.open("sample.json");
			directions = new JSONParser().getDirections(input);
			try {
				drawRouteOnMap(directions);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<LatLng> decodePoly() {
		String encoded; 
		InputStream input;

		List<LatLng> poly = new ArrayList<LatLng>();
		try {
			input = getAssets().open("polyline.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			encoded = reader.readLine();
			int index = 0, len = encoded.length();
			int lat = 0, lng = 0;

			while (index < len) {
				int b, shift = 0, result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lat += dlat;

				shift = 0;
				result = 0;
				do {
					b = encoded.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lng += dlng;

				LatLng p = new LatLng((int) (((double) lat)),
						 (((double) lng)));
				poly.add(p);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		

		//Log.e("poly", poly.toString());
		Log.e("poly(0)", "Latitude: " + poly.get(0).latitude + " Longitude: " + poly.get(0).longitude);
		return poly;
	}

	private void drawRouteOnMap(
			ArrayList<HashMap<String, JSONObject>> directions)
			throws JSONException {
		LatLng start, end;

		for (int i = 0; i < directions.size(); i++) {
			start = new LatLng(directions.get(i)
					.get(DirectionTags.TAG_START_LOCATION)
					.getDouble(DirectionTags.TAG_LAT), directions.get(i)
					.get(DirectionTags.TAG_START_LOCATION)
					.getDouble(DirectionTags.TAG_LNG));
			end = new LatLng(directions.get(i)
					.get(DirectionTags.TAG_END_LOCATION)
					.getDouble(DirectionTags.TAG_LAT), directions.get(i)
					.get(DirectionTags.TAG_END_LOCATION)
					.getDouble(DirectionTags.TAG_LNG));

			myMap.addPolyline(new PolylineOptions().geodesic(true)
					.add(start, end).width(5).color(Color.RED));
		}

	}

	/*
	 * private void drawMerchantLocations() { HashMap<String, String> merchant;
	 * String address; String iconName;
	 * 
	 * String[] icons = getResources().getStringArray(R.array.merchant_icons);
	 * merchantOverlays = new HashMap<String, HashMap<String, String>>();
	 * 
	 * for (int i = 0; i < locations.size(); i++) { merchant = locations.get(i);
	 * address = merchant.get(PayWaveTags.TAG_ADDRESS1) + " " +
	 * merchant.get(PayWaveTags.TAG_CITY) + ", " +
	 * merchant.get(PayWaveTags.TAG_STATE) + " " +
	 * merchant.get(PayWaveTags.TAG_ZIP); iconName =
	 * icons[Integer.parseInt(merchant.get(PayWaveTags.TAG_CATEGORY_ID))]; lat =
	 * Double.parseDouble(merchant.get(PayWaveTags.TAG_LATITUDE)); lng =
	 * Double.parseDouble(merchant.get(PayWaveTags.TAG_LONGITUDE)); Marker
	 * marker = myMap.addMarker(new MarkerOptions().position(new
	 * LatLng(lat,lng))
	 * .title(merchant.get("merchantName")).snippet(address).icon
	 * (BitmapDescriptorFactory.fromAsset(iconName)));
	 * merchantOverlays.put(marker.getId(), merchant);
	 * 
	 * 
	 * } }
	 */

	public void showLocation(View view) {

		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			Toast.makeText(
					this.getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {
			gps.showSettingsAlert();
		}
	}

}
