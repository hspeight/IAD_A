package com.itsadate.iad_a;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class CounterSettingsFragment extends PreferenceFragment {

    SharedPreferences prefs;
    //String[] keys = {"evtitle","evdesc","evtype","time_units"};
    String[] keys = {"evtitle","evdesc","evtype"};
    String[] typeText = {"Countup","Countdown"};
    ArrayList<String> selectedItems = new ArrayList<>();
    //String[] keys = {"evtitle","evdesc"};

    private static final String DEBUG_TAG = "CSF";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.counter_settings);

        prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());

        //PreferenceManager.setDefaultValues(getActivity(), R.xml.counter_settings, false);
        //boolean enableBackground = prefs.getBoolean("enable_background", false);
        //String event_title = prefs.getString("evtitle", null);

        //Log.i(DEBUG_TAG, "title is " + event_title);

        //String type = prefs.getString("Type", null);
        //Set colors = prefs.getStringSet("colors", Collections.emptySet());
        //Log.i(DEBUG_TAG, "Type is " + type);

        for (String key : keys) {
            //Log.i(DEBUG_TAG, "key is " + key);
            if(key.equals("time_units")) {
                Set<String> selections = prefs.getStringSet("time_units", null);
                String[] selected = selections.toArray(new String[]{});
                for(String s : selected) {
                //for (int i = 0; i < selected.length; i++) {
                    Log.i(DEBUG_TAG, ">" + s);
                }
            }
            else
                setListener(key, prefs.getString(key, null));
        }
    }

    public void setListener(final String key, String stringKey) {
        Log.i(DEBUG_TAG, "key = " + key);
        Preference serverAddressPrefs = findPreference(key);
        if(key.equals("evtype"))
            serverAddressPrefs.setSummary(typeText[Integer.parseInt(stringKey)]);
        else
            serverAddressPrefs.setSummary(stringKey);
        //Log.i(DEBUG_TAG, "Summary for " + key + " is " + serverAddressPrefs.getSummary());
        //Log.i(DEBUG_TAG, "stringKey for " + key + " is " + stringKey);
        serverAddressPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                switch (key) {
                    case "evtype":
                        preference.setSummary(typeText[Integer.parseInt((String) newValue)]);
                        break;
                    default:
                        //Toast.makeText(getActivity(), "Cannot be blank", Toast.LENGTH_SHORT).show();
                        preference.setSummary((String) newValue);
                        //Log.i(DEBUG_TAG, "New summary for " + key + " is " + newValue);
                }

                return true;
            }
        });
    }

}

