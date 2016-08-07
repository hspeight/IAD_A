package com.itsadate.iad_a;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CounterSettingsFragment extends PreferenceFragment {

    SharedPreferences prefs;
    ArrayList<String> selectedItems = new ArrayList<>();
    //String[] keys = {"evtitle","evdesc"};
    public String activityDataIn = "";
    static String[] tUnits = {"Not Used","Years","Days","Hours","Mins","Secs"};

    private static final String DEBUG_TAG = "CSF";
    MyDBHandler dbHandler;
    int RowID = 0;
    boolean newEvent = false;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.counter_settings);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.counter_settings_not_currently_in_use, false);

        //prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());

        //prefs = getPreferenceScreen().getSharedPreferences();

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        // required because date_and_time isn't detected by onPreferenceChange
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key .equals("date_and_time")) {
                    Preference datetime = findPreference("date_and_time");
                    datetime.setSummary(formatDateTime(prefs.getInt("date_and_time", 946728060))); // should change this to current date
                }
                if (key .equals("time_units")) {
                    Preference evunits = findPreference("time_units");
                    //Log.d("time_units from db is ", myEvent.get_timeunits());
                    evunits.setSummary(constructTimeUnitString(prefs.getString("time_units", "Not found")));
                }

                //Log.d(DEBUG_TAG, "Settings key changed: " + key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        if (getActivity().getIntent().getExtras() == null) {
            //Log.d("extras", "nothing there");
            RowID = dbHandler.getTemplateEventID();
            //Log.d("CSF", " " + RowID);
            newEvent = true;
        } else {
            RowID = getActivity().getIntent().getExtras().getInt("ROW_ID");
        }
        Events myEvent = dbHandler.getMyEvent(RowID);

        SharedPreferences.Editor editor;
        Map<String,?> keys = prefs.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            Log.d("map values>", entry.getKey() + ": " + entry.getValue().toString() + "!" + RowID);
            setListener(entry.getKey());
            switch (entry.getKey()) {
                case "evtitle":
                    Preference evtitle = findPreference("evtitle");
                    evtitle.setSummary(myEvent.get_eventname());
                    EditTextPreference evtitleTextPref = (EditTextPreference) findPreference("evtitle");
                    evtitleTextPref.setText(evtitle.getSummary().toString());
                    //evtitle.setOnPreferenceClickListener(exampleChangeListener);
                    break;
                case "evdesc":
                    Preference evdesc = findPreference("evdesc");
                    evdesc.setSummary(myEvent.get_eventinfo());
                    EditTextPreference evdescTextPref = (EditTextPreference) findPreference("evdesc");
                    evdescTextPref.setText(evdesc.getSummary().toString());
                    //evdesc.setOnPreferenceClickListener(exampleChangeListener);
                    break;
                case "date_and_time":
                    Preference datetime = findPreference("date_and_time");
                    datetime.setSummary(formatDateTime(myEvent.get_evtime()));
                    //SharedPreferences.Editor editor = prefs.edit();
                    editor = prefs.edit();
                    editor.putInt("date_and_time", myEvent.get_evtime()); // put in prefs so that date/time dialog can pick it up
                    editor.apply();
                    break;
                case "direction":
                    Preference direction = findPreference("direction");
                    direction.setSummary("" + myEvent.get_direction()); // need a better way to convert to char
                    break;
                case "time_units":
                    //Log.d("time_units from db is ", myEvent.get_timeunits());
                    Preference evunits = findPreference("time_units");
                    evunits.setSummary(constructTimeUnitString(myEvent.get_timeunits()));
                    editor = prefs.edit();
                    editor.putString("time_units", myEvent.get_timeunits());
                    editor.apply();
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
        //System.out.println("!!- activity dat in is " + activityDataIn);
        //Log.i(DEBUG_TAG, "RowID is " + getActivity().getIntent().getExtras().getInt("ROW_ID"));
    }

    public void setListener(final String key) {
        //Log.i(DEBUG_TAG, "key = " + key);
        Preference serverAddressPrefs = findPreference(key);

        serverAddressPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //Log.i(DEBUG_TAG, "blah " + newValue.toString());
               //Log.d(DEBUG_TAG, ">>>KEY IS<<<" + key);
                switch (key) {
                //    case "time_units":
                        //Log.d(DEBUG_TAG, ">>>TU");
                        //preference.setSummary(constructTimeUnitString(newValue));
                //        break;
                    default:
                        //Toast.makeText(getActivity(), "Cannot be blank", Toast.LENGTH_SHORT).show();
                        //Log.i(DEBUG_TAG, "about to set summary for " + key + " to " + newValue);
                        preference.setSummary((String) newValue);
                }
                return true;
            }
        });

        //SharedPreferences.OnSharedPreferenceChangeListener sharedPrefsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        //    @Override
        //    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //        Log.d(DEBUG_TAG, ">>> KEY IS <<<" + key);
                // update summary
        //    }
        //};

    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(DEBUG_TAG, "on pause entered");
        //Update database record with new values if anything has changed

        //dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        //Events myEvent = dbHandler.getMyEvent(RowID);
        String activityDataOut = collateActivityInfo();

        if (!activityDataIn.equals(activityDataOut)) {
            Log.i(DEBUG_TAG, "data changed");
            //if (newEvent) {
            //    addNewEvent(dbHandler.getMyEvent(RowID));
            //} else {
            updateEvent(dbHandler.getMyEvent(RowID));
            //}
        }
            //Log.i(DEBUG_TAG, "data has changed");
        //else
        //    Log.i(DEBUG_TAG, "dat dare data did not change");
        //Preference tu = findPreference("time_units");
        //tu.setSummary(prefs.getString("time_units", "Error"));
        //Preference dt = findPreference("date_and-time");
        //dt.setSummary(prefs.getString("date_and_time", "Error"));
        //prefs.edit().clear();
        //prefs.edit().apply();
    }

    public String constructTimeUnitString(Object newValue) {
        Log.d(DEBUG_TAG, "newValue.toString() is " + newValue.toString());
        //if (newValue.toString().length() > 2) {// length will be 2 if nothing was selected
        if (newValue.toString().length() > 0) {
            Log.d(DEBUG_TAG, "++++newvalue.tostring is " + newValue.toString());
            String unitString = "";
            int end = newValue.toString().length() - 1;
            //final String[] tokens = newValue.toString().substring(1, end).replace(" ", "").split(",");
            final String[] tokens = newValue.toString().replace(" ", "").split(",");
            for (String token : tokens) {
                //tot += Integer.parseInt(token);
                //Log.d(DEBUG_TAG, "++++newValue is " + token);
                unitString += tUnits[Integer.parseInt(token)] + ",";
            }
            Log.d(DEBUG_TAG, "++++unitstring is " + unitString);
            return unitString.substring(0, unitString.length() - 1); //remove trailing comma
        } else {
            return "Nowt selected";
        }

    }

    // Concatenate values into a string for comparison
    public String collateActivityInfo() {
        //Log.i(DEBUG_TAG, "collating info");
        Map<String,?> keys = prefs.getAll(); //use getAll() method of SharedPreferences to get all the keys
        String valToReturn = "";
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            //System.out.println("!!- entry.getValue().toString() is " + entry.getValue().toString());
            valToReturn += entry.getValue().toString();
            //Log.d(DEBUG_TAG, entry.getKey() + ": " + entry.getValue().toString());
        }
        return valToReturn;

    }

    private void updateEvent(Events myEvent) {
        //Log.d(DEBUG_TAG, "prefs.getString(time_units, Error)" + prefs.getString("time_units", "Error"));
        myEvent.set_eventname(prefs.getString("evtitle", "Error"));
        myEvent.set_eventinfo(prefs.getString("evdesc", "Error"));
        myEvent.set_evtime(prefs.getInt("date_and_time", 946728060));
        myEvent.set_direction(Integer.valueOf(prefs.getString("direction", "0")));
        myEvent.set_timeunits(prefs.getString("time_units", "Error"));
        //Log.d(DEBUG_TAG, ? + " is currently is the summary");
        if (newEvent) {
            myEvent.set_evtype("R");
            myEvent.set_evstatus("A");
            dbHandler.addEvent(myEvent);
        } else {
            dbHandler.updateEvent(myEvent);
        }
    }

    private String formatDateTime (int dateTime) {

        long millis = dateTime;
        millis *= 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());

        return sdf.format(new Date(millis));

    }
}