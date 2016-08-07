package com.itsadate.iad_a;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom preference for time selection. Persistent and
 * stored separately in the underlying shared preferences
 */
public class TimePreferenceCustom extends DialogPreference {
    private static final String DEBUG_TAG = "TPC";
    // widgets for picking date & time
    private TimePicker timePicker;
    private DatePicker datePicker;

    /** Default hour */
    //private static final int DEFAULT_HOUR = 8;

    /** Default minute */
    //private static final int DEFAULT_MINUTE = 24;

    //private int rowid;

    //SharedPreferences sharedpreferences;
    SharedPreferences sharedpreferences = PreferenceManager
            .getDefaultSharedPreferences(getContext());
    //public static final String MyPREFERENCES = "transientPrefs";

    /**
     * Creates a preference for choosing a time based on its XML declaration.
     *
     * @param context
     * @param attributes
     */
    public TimePreferenceCustom(Context context,
                                AttributeSet attributes) {
        super(context, attributes);
        //SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setPersistent(false);
        //Log.i(DEBUG_TAG, "attributecount="+Integer.toString(attributes.getAttributeCount()));
    }

    /**
     * Initialize time picker to currently stored time preferences.
     *
     * @param view
     * The dialog preference's host view
     */
    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        timePicker = (TimePicker) view.findViewById(R.id.prefTimePicker);
        datePicker = (DatePicker) view.findViewById(R.id.prefDatePicker);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-kk-mm", java.util.Locale.getDefault());
        String myDate = sdf.format( (long) sharedpreferences.getInt("date_and_time", 58705860) * 1000); // needs to be changed to current date & time
        String[] output = myDate.split("-");

        timePicker.setCurrentHour(Integer.parseInt(output[3]));
        timePicker.setCurrentMinute(Integer.parseInt(output[4]));
        datePicker.init(Integer.parseInt(output[2]),    // Year
                Integer.parseInt(output[1]) -1,            // Month
                Integer.parseInt(output[0]),            // Day
                null);
        //datePicker.set(sharedpreferences.getInt("year", 1970));
       //Log.i(DEBUG_TAG, "in onBindDialogView");
        timePicker.setIs24HourView(DateFormat.is24HourFormat(timePicker.getContext()));
        //System.out.println("!!- " + getSharedPreferences().getInt(getKey() + ".hour", DEFAULT_HOUR));

    }

    /**
     * Handles closing of dialog. If user intended to save the settings, selected
     * hour and minute are stored in the preferences with keys KEY.hour and
     * KEY.minute, where KEY is the preference's KEY.
     *
     * @param okToSave
     * True if user wanted to save settings, false otherwise
     */
    @Override
    protected void onDialogClosed(boolean okToSave) {
        super.onDialogClosed(okToSave);
        //Log.d(DEBUG_TAG,"in it");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        timePicker.clearFocus();
        //SharedPreferences.Editor editor = getEditor();
        if (okToSave) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh.mm", java.util.Locale.getDefault());
                String str =  "" + datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() +
                        "-" + timePicker.getCurrentHour() + "." + timePicker.getCurrentMinute();
                //String str = "1969-12-31-23.59";
                Date date = df.parse(str);
                //long epoch = date.getTime();
                int epoch = (int)(date.getTime() / 1000);
                editor.putInt("date_and_time", epoch);
                editor.apply();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        //} else {
            //Log.i(DEBUG_TAG,"not ok");
        //    editor.putBoolean("updated", false);
        }
    }
}