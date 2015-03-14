package com.itsadate.iad_a;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment {

    public final int DATE_PICKER = 1;
    public final int TIME_PICKER = 2;
    public final int DIALOG = 3;

    OnDateSetListener ondateSet;
    OnTimeSetListener ontimeSet;

    private Fragment mCurrentFragment;
//    public DatePickerFragment ( Fragment fragment ) {
//        mCurrentFragment = fragment;
//    }

    public DatePickerFragment() {
    }

    public void setCallBack(OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    public void setCallBack(OnTimeSetListener ontime) {
        ontimeSet = ontime;
    }


    private int year, month, day, hour, minute;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        hour = args.getInt("hour");
        minute = args.getInt("minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog myDialog = null;
        //Bundle bundle = new Bundle();
        Bundle bundle = getArguments();
        int id = bundle.getInt("dialog_id");
        switch (id) {
            case DATE_PICKER:
                myDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
                break;
            case TIME_PICKER:
                myDialog = new TimePickerDialog(getActivity(), ontimeSet, hour, minute, true);
                break;
            case DIALOG:
                //Define your custom dialog or alert dialog here and return it.
        }
        return myDialog;
    }
}