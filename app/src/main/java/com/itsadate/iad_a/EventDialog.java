package com.itsadate.iad_a;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class EventDialog extends DialogFragment {

    private EditText mEditText;

    //public EventDialog() {
    //    // Empty constructor required for DialogFragment
    //}

    @Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Save changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(), "Future date not allowed", Toast.LENGTH_SHORT).show();
                        ((EventEditor)getActivity()).doPositiveClick(getView());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User said No
                        ((EventEditor)getActivity()).doNegativeClick();
                    }
                });
        // Create the AlertDialog object and return it
        //return builder.create();
        return builder.create();


    }
}