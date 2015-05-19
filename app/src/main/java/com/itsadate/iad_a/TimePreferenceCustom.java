package com.itsadate.iad_a;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Custom preference for time selection. Hour and minute are persistent and
 * stored separately as ints in the underlying shared preferences under keys
 * KEY.hour and KEY.minute, where KEY is the preference's key.
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

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "transientPrefs";

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
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String Uname = userDetails.getString("username", "");
        //Log.i(DEBUG_TAG, "Time=" + sharedpreferences.getInt("hour",9) + sharedpreferences.getInt("mins",45));
        timePicker.setCurrentHour(sharedpreferences.getInt("hour", 0));
        timePicker.setCurrentMinute(sharedpreferences.getInt("mins", 0));
        datePicker.init(sharedpreferences.getInt("year", 1970),
                sharedpreferences.getInt("month", 0),
                sharedpreferences.getInt("day", 1),
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
        //System.out.println("!!- " + okToSave);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        timePicker.clearFocus();
        //SharedPreferences.Editor editor = getEditor();
        if (okToSave) {
            //Log.i(DEBUG_TAG,"ok");
            editor.putInt("hour", timePicker.getCurrentHour());
            //editor.putInt(getKey() + ".hour", timePicker.getCurrentHour());
            editor.putInt("mins", timePicker.getCurrentMinute());
            //editor.putInt(getKey() + ".minute", timePicker.getCurrentMinute());
            editor.putInt("year", datePicker.getYear());
            editor.putInt("month", datePicker.getMonth());
            editor.putInt("day", datePicker.getDayOfMonth());
            editor.putBoolean("updated", true);
            editor.apply();
        } else {
            //Log.i(DEBUG_TAG,"not ok");
            editor.putBoolean("updated", false);
        }
    }
}