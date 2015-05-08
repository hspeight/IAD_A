package com.itsadate.iad_a;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
//import android.widget.TextView;

public class CounterSettingsFragment extends PreferenceFragment {
    public int rowID;
    private static final String DEBUG_TAG = "CSF";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(DEBUG_TAG, "come on man");
        //Bundle bundle = savedInstanceState.getBundle("ROW_ID");
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                rowID = bundle.getInt("ROW_ID");
                Log.i(DEBUG_TAG, bundle.toString());
            } else {
                Log.i(DEBUG_TAG, "Empty bundle man");
            }
        } else {
            Log.i(DEBUG_TAG, "no args in CounterSettingsFragment");
        }
            // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.counter_settings);
        Preference dt = findPreference("date_and_time");
        dt.setSummary(Integer.toString(rowID));

    }

}
