package com.itsadate.iad_a;

//import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CounterSettingsActivity extends PreferenceActivity {
    private static final String DEBUG_TAG = "CSA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(DEBUG_TAG, "youre in the csa " + getIntent().getExtras());
        Bundle bundle = getIntent().getExtras();

        Fragment CounterSettingsFragment = new CounterSettingsFragment();

        //Allow the RowID to be read inside the fragment
        CounterSettingsFragment.setArguments(bundle);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                //.replace(android.R.id.content, new CounterSettingsFragment())
                .replace(android.R.id.content, CounterSettingsFragment)
                .commit();


    }





}