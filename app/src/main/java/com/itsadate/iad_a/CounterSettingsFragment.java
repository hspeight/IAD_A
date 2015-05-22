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
import java.util.Map;
import java.util.Set;

public class CounterSettingsFragment extends PreferenceFragment {

    SharedPreferences prefs;
    //String[] keys = {"evtitle","evdesc","evtype","time_units"};
    String[] keys = {"evtitle","evdesc","evtype"};
    String[] typeText = {"Countup","Countdown"};
    ArrayList<String> selectedItems = new ArrayList<>();
    //String[] keys = {"evtitle","evdesc"};
    public String activityDataIn = "";
    static String[] tUnits = {"Not Used","Years","Days","Hours","Mins","Secs"};

    private static final String DEBUG_TAG = "CSF";
    MyDBHandler dbHandler;
    int RowID = 0;

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

        //for (String key : keys) {
        //    //Log.i(DEBUG_TAG, "key is " + key);
        //    setListener(key, prefs.getString(key, null));
        //}

        RowID = getActivity().getIntent().getExtras().getInt("ROW_ID");
        dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        Events myEvent = dbHandler.getMyEvent(RowID);

        Map<String,?> keys = prefs.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            setListener(entry.getKey());
            switch (entry.getKey()) {
                case "evtitle":
                    Preference evtitle = findPreference("evtitle");
                    evtitle.setSummary(myEvent.get_eventname());
                    break;
                default:
                    //Toast.makeText(getActivity(), "Cannot be blank", Toast.LENGTH_SHORT).show();
                   // preference.setSummary((String) newValue);
                    //Log.i(DEBUG_TAG, "blah " + entry.getKey() + " is " + entry.getValue().toString());
            }
            //Log.i(DEBUG_TAG, entry.getKey() + ": " + entry.getValue().toString());
        }



        //prefs.
        activityDataIn = collateActivityInfo();
        //Log.i(DEBUG_TAG, "RowID is " + getActivity().getIntent().getExtras().getInt("ROW_ID"));
    }

    public void setListener(final String key) {
        //Log.i(DEBUG_TAG, "key = " + key);
        Preference serverAddressPrefs = findPreference(key);
        //if(key.equals("evtype"))
        //    serverAddressPrefs.setSummary(typeText[Integer.parseInt(stringKey)]);
        //else
        //    serverAddressPrefs.setSummary(stringKey);
        //Log.i(DEBUG_TAG, "Summary for " + key + " is " + serverAddressPrefs.getSummary());
        //Log.i(DEBUG_TAG, "stringKey for " + key + " is " + stringKey);
        serverAddressPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                switch (key) {
                    //case "evtitle":
                    //    preference.setSummary(typeText[Integer.parseInt((String) newValue)]);
                    //    //preference.setSummary(key);
                    //    break;
                    //case "evdesc":
                    //    preference.setSummary(typeText[Integer.parseInt((String) newValue)]);
                    //    //preference.setSummary(key);
                    //    break;
                    case "evtype":
                        preference.setSummary(typeText[Integer.parseInt((String) newValue)]);
                        break;
                    case "time_units":
                        //preference.setSummary(typeText[Integer.parseInt((String) newValue)]);
                        //preference.getEditor().apply();
                        //preference.notify();
                        //SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        //edit.putString("time_units", "1-2-3");

                        //int end = newValue.toString().length() -1;
                        if (newValue.toString().length() > 2) // length will be 2 if nothing is selected
                            preference.setSummary(constructUnitString(newValue));
                        else
                            preference.setSummary("");

                        //Log.i(DEBUG_TAG, "end is " + end);

                        //Log.i(DEBUG_TAG, "and the winner is " + tot);


                        //preference.setSummary(buildTimeUnitString());
                        break;
                    default:
                        //Toast.makeText(getActivity(), "Cannot be blank", Toast.LENGTH_SHORT).show();
                        //Log.i(DEBUG_TAG, "about to set summary for " + key + " to " + newValue);
                        preference.setSummary((String) newValue);

                }

                return true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //Update database record with new values if anything has changed

        //dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        //Events myEvent = dbHandler.getMyEvent(RowID);
        String activityDataOut = collateActivityInfo();

        if (!activityDataIn.equals(activityDataOut))
            updateDetails(dbHandler.getMyEvent(RowID));
            //Log.i(DEBUG_TAG, "data has changed");
        //else
        //    Log.i(DEBUG_TAG, "dat dare data did not change");
        Preference tu = findPreference("time_units");
        tu.setSummary(prefs.getString("time_units", "Error"));
    }

    public String constructUnitString(Object newValue) {

        String unitString = "";
        int end = newValue.toString().length() -1;
        final String[] tokens = newValue.toString().substring(1, end).replace(" ", "").split(",");

        for (String token : tokens) {
            //tot += Integer.parseInt(token);
            //Log.i(DEBUG_TAG, "newValue is " + token);
            unitString += tUnits[Integer.parseInt(token)] + ",";
        }
        return unitString.substring(0,unitString.length()-1); //remove trailing comma
    }

    // Concatenate values into a string for comparison
    public String collateActivityInfo() {
        //Log.i(DEBUG_TAG, "collating info");
        Map<String,?> keys = prefs.getAll(); //use getAll() method of SharedPreferences to get all the keys
        String valToReturn = "";
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            valToReturn += entry.getValue().toString();
            //Log.i(DEBUG_TAG, entry.getKey() + ": " + entry.getValue().toString());
        }
        return valToReturn;

    }

    //private Events readDetails(Events myEvent) {
    //
    //    return myEvent;

    //}

    private void updateDetails(Events myEvent) {
        //Log.i(DEBUG_TAG, "Some data is " + myEvent.get_eventname() + "*^*" + myEvent.get_eventinfo());

        myEvent.set_eventname(prefs.getString("evtitle", "Error"));
        myEvent.set_eventinfo(prefs.getString("evdesc", "Error"));
        dbHandler.updateEvent(myEvent);
    }
}

