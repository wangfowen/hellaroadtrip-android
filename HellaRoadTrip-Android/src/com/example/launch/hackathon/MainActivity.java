package com.example.launch.hackathon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

public class MainActivity extends Activity{
	
	public static Context appContext;
	JSONObject header = null;
	JSONObject response = null;
	
	JSONArray jArray;
	
	ArrayList<HashMap<String, String>> locations;
	
	//public static BundleListPackage<LogMessage> messages;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext = getApplicationContext();
        
        FragmentManager fm = getFragmentManager();
        
        if (findViewById(R.id.map) != null) {
        	FragmentTransaction ft = fm.beginTransaction();
        	ft.add(android.R.id.content, new MapViewFragment());
        	ft.commit();
        }
        
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
        	Bundle data = new Bundle();
        	data.putSerializable("data", locations);
            MapViewFragment details = new MapViewFragment();
            details.setArguments(data);
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

      
        /*ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        
        bar.addTab(bar.newTab()
                .setText("Map Fragment")
                .setTabListener(new TabListener<MapSampleFragment>(
                        this, "map_fragment", MapSampleFragment.class)));
        
        bar.addTab(bar.newTab()
                .setText("JSON Fragment")
                .setTabListener(new TabListener<JSONLocationParse>(
                        this, "json_fragment", JSONLocationParse.class)));*/
        
        
        
        /*if (findViewById(R.id.fragment_container) != null) {
	        if (savedInstanceState != null) {
	        	return;
	        }
        }*/
        
    }
    
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }*/

    
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menuitem_search:
				Toast.makeText(appContext, "search", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_add:
				Toast.makeText(appContext, "add", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_share:
				Toast.makeText(appContext, "share", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_feedback:
				Toast.makeText(appContext, "feedback", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_about:
				Toast.makeText(appContext, "about", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_quit:
				Toast.makeText(appContext, "quit", Toast.LENGTH_SHORT).show();
				return true;
		}
		return false;
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
    
    
    
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.hide(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.show(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.hide(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }
    }*/
    
    
}



