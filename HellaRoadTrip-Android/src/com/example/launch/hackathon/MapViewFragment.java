package com.example.launch.hackathon;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.launch.hackathon.common.GPSTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewFragment extends Fragment {
	
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
		
		locations = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("data");
		
		gps = new GPSTracker(getActivity());
		
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	 FragmentManager myFragmentManager = getFragmentManager();
         MapFragment mySupportMapFragment = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
         myMap = mySupportMapFragment.getMap();	         
         myMap.setMyLocationEnabled(true);
         
         CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(gps.getLatitude(), gps.getLongitude())).zoom(12.8f).build();
         CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
         myMap.moveCamera(camUpdate);
         
         
         
         /* 
          * myMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				i++;
				message = "Point: "+ i + " at "+ point.toString();
				myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
				Toast.makeText(getActivity(), "New marker at: "+point.toString(), Toast.LENGTH_LONG).show();
			}
		});
         */
         
         //drawMerchantLocations();
         
         
         
         
         Button gpsLocBtn = (Button)getView().findViewById(R.id.gps_loc_btn);
         gpsLocBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLocation(getView());
			}
		});
         
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
			
			Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		}
		else {
			gps.showSettingsAlert();
		}
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
