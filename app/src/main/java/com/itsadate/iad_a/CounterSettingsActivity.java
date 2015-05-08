package com.itsadate.iad_a;

//import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class CounterSettingsActivity extends PreferenceActivity {
    private static final String DEBUG_TAG = "CSA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DEBUG_TAG, "youre in the csa");
        Bundle bundle = getIntent().getExtras();

        Fragment CounterSettingsFragment = new CounterSettingsFragment();
        //bundle.putInt("ROW_IDTEST", 123);
        CounterSettingsFragment.setArguments(bundle);
        //if (bundle != null)
        //    Log.i(DEBUG_TAG, Integer.toString(bundle.getInt("ROW_ID")));
        //else
        //    Log.i(DEBUG_TAG, "nowt there");

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                //.replace(android.R.id.content, new CounterSettingsFragment())
                .replace(android.R.id.content, CounterSettingsFragment)
                .commit();
    }
}