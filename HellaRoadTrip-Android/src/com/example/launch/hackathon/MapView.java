package com.example.launch.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.launch.hackathon.async.Async;
import com.example.launch.hackathon.common.GPSTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
		
		//locations = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("data");
		setContentView(R.layout.map_fragment);
		gps = new GPSTracker(this);
		createLayoutContent();
		
		
	}

    public void createLayoutContent(){
    	
    	 FragmentManager myFragmentManager = getFragmentManager();
         MapFragment mySupportMapFragment = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
         myMap = mySupportMapFragment.getMap();	         
         myMap.setMyLocationEnabled(true);
         
         CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(gps.getLatitude(), gps.getLongitude())).zoom(12.8f).build();
         CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
         myMap.moveCamera(camUpdate);
         
         
         
         
           myMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				i++;
				message = "Point: "+ i + " at "+ point.toString();
				Marker marker = myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
				 Polyline line = myMap.addPolyline(new PolylineOptions()
			       .add(new LatLng(gps.getLatitude(), gps.getLongitude()), marker.getPosition())
			       .width(5)
			       .color(Color.RED));
				//Toast.makeText(getActivity(), "New marker at: "+point.toString(), Toast.LENGTH_LONG).show();
			}
		});
         
         
         //drawMerchantLocations();
         
         
         
         
         Button gpsLocBtn = (Button)this.findViewById(R.id.gps_loc_btn);
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
    	switch(item.getItemId()) {
    		case R.id.action_add:
    			new Async().execute(new String[] {""});
    			//Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    			createAddMarkerDialog().show();
    			return true;
    		default:
    			Toast.makeText(this, "Selected Item: none", Toast.LENGTH_SHORT).show();
    			return true;
    	}
    }
        
    
	/*private void drawMerchantLocations() {
		HashMap<String, String> merchant;
         String address;
         String iconName;
         
         String[] icons = getResources().getStringArray(R.array.merchant_icons);
         merchantOverlays = new HashMap<String, HashMap<String, String>>();
         
         for (int i = 0; i < locations.size(); i++) {
        	 merchant = locations.get(i);
        	 address = merchant.get(PayWaveTags.TAG_ADDRESS1) + " " + merchant.get(PayWaveTags.TAG_CITY) + ", " + merchant.get(PayWaveTags.TAG_STATE) + " " + merchant.get(PayWaveTags.TAG_ZIP);
        	 iconName = icons[Integer.parseInt(merchant.get(PayWaveTags.TAG_CATEGORY_ID))];
        	 lat = Double.parseDouble(merchant.get(PayWaveTags.TAG_LATITUDE));
        	 lng = Double.parseDouble(merchant.get(PayWaveTags.TAG_LONGITUDE));
        	 Marker marker = myMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(merchant.get("merchantName")).snippet(address).icon(BitmapDescriptorFactory.fromAsset(iconName)));
        	 merchantOverlays.put(marker.getId(), merchant);
        	 
        	 
         }
	}*/
    
    public void showLocation(View view) {
		
    	if(gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			
			Toast.makeText(this.getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		}
		else {
			gps.showSettingsAlert();
		}
    }
    
    public AlertDialog createAddMarkerDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.drivers);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.marker_types, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     spinner.setAdapter(adapter);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
        // Add action buttons
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	   
                	   
                	   
                       myMap.setOnMapClickListener(new OnMapClickListener() {
						
						@Override
						public void onMapClick(LatLng point) {
							i++;
							message = "Point: "+ i + " at "+ point.toString();
							Marker marker = myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
							myMap.addPolyline(new PolylineOptions()
						       .add(new LatLng(gps.getLatitude(), gps.getLongitude()), marker.getPosition())
						       .width(5)
						       .color(Color.BLACK));
							
						}
					});
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });  
        
        return builder.create();
    }
    
    /*private void getSortedLocations(HashMap<Integer, Double> distance, ArrayList<HashMap<String, String>> data) {
    	

        List<Integer> mapKeys = new ArrayList<Integer>(distance.keySet());
        List<Double> mapValues = new ArrayList<Double>(distance.values());
        
        //Collections.sort(mapKeys);
        Collections.sort(mapValues);
        
        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        
        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
        	double val = (Double) valueIt.next();
        	Iterator<Integer> keyIt = mapKeys.iterator();
        	
        	while (keyIt.hasNext()) {
        		Object key = keyIt.next();
        		
        		double dist_1 = distance.get(key);
        		double dist_2 = val;
        		
        		if (dist_1 == dist_2) {
        			distance.remove(key);
        			mapKeys.remove(key);
        			sortedMap.put((Integer) key, val);
        			Log.i("sort", sortedMap.toString());
        			break;
        		}
        	}
        }

       // for (int i : sortedMap.keySet()) {
      //  	Log.i("sort", i + " " + sortedMap.get(i));
       // }
    }*/

}
