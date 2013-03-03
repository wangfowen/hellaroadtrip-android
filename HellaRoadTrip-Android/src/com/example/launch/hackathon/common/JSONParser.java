package com.example.launch.hackathon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class JSONParser {
	static InputStream in = null;
	static JSONObject jObject = null;
	static String result = "";

	private JSONObject header = null;
	private JSONObject response = null;

	JSONArray jArray;

	LatLng SFO_LATLNG = new LatLng(37.61660000000001, -122.383890);

	

	public JSONParser() {

	}

	public JSONObject parseJSON(InputStream input) {

		// in = getResources().getAssets().open("raw/sample.json");

		// AssetManager assetManager = ctx.getAssets();
		// in = assetManager.open("sample.json");

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			input.close();
			result = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data" + e.toString());
		}
		// JSONArray locArray = jObject.getJSONArray("locations");

		// } catch (IOException e) {
		// e.printStackTrace();
		// return null;
		// }
		// return locArray;
		return jObject;

	}

	public ArrayList<HashMap<String, JSONObject>> getDirections(InputStream input) {
		
		JSONObject response = null;
		
		JSONObject json = parseJSON(input);
		
		//ArrayList<HashMap<String, JSONArray>> steps = new ArrayList<HashMap<String, JSONArray>>();

		ArrayList<HashMap<String, JSONObject>> steps = new ArrayList<HashMap<String, JSONObject>>();

		try {

			//header = json.getJSONObject(TAG_HEADER);
			jArray = json.getJSONArray(DirectionTags.TAG_HEADER);
			
			//jArray = header.getJSONArray(TAG_END_LOCATION);

			for (int i = 0; i < jArray.length(); i++) {
				response = jArray.getJSONObject(i);

				
				HashMap<String, JSONObject> hMap = new HashMap<String, JSONObject>();
				//HashMap<String, JSONArray> hMap = new HashMap<String, JSONArray>();

				hMap.put(DirectionTags.TAG_END_LOCATION, response.getJSONObject(DirectionTags.TAG_END_LOCATION));
				hMap.put(DirectionTags.TAG_START_LOCATION,response.getJSONObject(DirectionTags.TAG_START_LOCATION));
				
				
				steps.add(hMap);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return steps;
	}
	

}
