package com.itsadate.iad_a;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class EventDialog extends DialogFragment {
    OnDataPass dataPasser;
    String alertMessage;
    String buttonPos;
    String buttonNeg;
    //private String whichAct = "EventEditor";

    //public EventDialog() {
    //    // Empty constructor required for DialogFragment
    //}

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //System.out.println("!!- "  + "dialog created");

        Bundle bundle = this.getArguments();
        alertMessage = bundle.getString("dialogMessage");
        buttonPos = bundle.getString("buttonPos");
        buttonNeg = bundle.getString("buttonNeg");
    }

    @Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMessage)
                //.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                .setPositiveButton(buttonPos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        passData("Yes");
                        //Toast.makeText(getActivity(), "Yes clicked", Toast.LENGTH_SHORT).show();
                        //((EventEditor)getActivity()).doPositiveClick(getView());
                    }
                })
                .setNegativeButton(buttonNeg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        passData("No");
                        //Toast.makeText(getActivity(), "No clicked - " + getActivity().getCallingActivity(), Toast.LENGTH_SHORT).show();
                        // User said No
                        //((EventEditor)getActivity()).doNegativeClick();
                    }
                });
        // Create the AlertDialog object and return it
        //return builder.create();
        return builder.create();


    }

    //In your fragment, declare the interface...
    public interface OnDataPass {
        public void onDataPass(String data);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataPasser = (EventDialog.OnDataPass) a;
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }


}