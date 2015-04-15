package com.itsadate.iad_a;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;


//import android.content.Intent;
//import android.view.View.OnClickListener;

public class BackupAndRestore extends Activity {

    //private static final String appKey = "m2vvdeh11l1bhwx";
    //private static final String appSecret = "l0d08caoz7o4bi1";

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);

        //dbHandler = new MyDBHandler(this, null, null, 1);

    }

    public void backupEvents(View view) {

        //dbHandler.copyAllEvents();
        //Toast.makeText(getApplicationContext(), "Backup successful", Toast.LENGTH_SHORT).show();
    }

}
