package com.example.launch.hackathon.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class JSONParser {
	static InputStream in = null;
	static JSONObject jObject = null;
	static String result = "";

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
	
	
	
	public ArrayList<LatLng> getDirections(InputStream input) {
		ArrayList<LatLng> steps = new ArrayList<LatLng>();
		
		/*AssetManager assetManager = getAssets();
		InputStream input;
		JSONObject json = null;
		
		try {
			input = assetManager.open("sample.json");
			json = new JSONParser().getDirections(input);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		return steps;
	}

}
