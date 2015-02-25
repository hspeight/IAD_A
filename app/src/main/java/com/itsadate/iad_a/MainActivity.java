package com.itsadate.iad_a;

// Code courtesy of http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.ImageView;

import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends Activity {

    int maxAllowableEvents = 5; // not yet in use. Wil be 9999 in paid version

    MyDBHandler dbHandler;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public String rowID []; // Array of _id column form database
    public ImageView bin = null;
    //public int binImageRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new MyDBHandler(this, null, null, 1);
        setContentView(R.layout.main_activity);

        setGroupParents();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override   // collapse list
            public void onGroupExpand(int groupPosition) {
                //setImageRowPosition(groupPosition);
                if(groupPosition != previousItem )
                    expListView.collapseGroup(previousItem );
                previousItem = groupPosition;

            }
        });

    }

    public void deleteIconClicked (View v){
        // System.out.println("!!-  tag is " + v.getTag().toString());
        // Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
        // deleteEvent(v.getTag().toString());
        //deleteEvent((int)v.getTag());
        dbHandler.deleteEvent(rowID[(int)v.getTag()]);
        //System.out.println("!!-  tag is " + rowID[(int)v.getTag()]);
        onPause(); // call onpause so that on onresume can be called to refresh list
        onResume();

    }
    public void editIconClicked (View v){

        Intent intent = new Intent(getBaseContext(), EventEditor.class);
        intent.putExtra("ROW_ID",rowID[(int)v.getTag()]);
        startActivity(intent);

    }
    public void playIconClicked (View v){

        Intent intent = new Intent(getBaseContext(), ShowCounter.class);
        intent.putExtra("ROW_ID",rowID[(int)v.getTag()]);
        startActivity(intent);

    }

    // method to add parent & child events
    public void setGroupParents() {

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        String evstring = dbHandler.getActiveEventIDs("A");
        //System.out.println("!!- " + "*" + evstring + "*");
        String[] foods = evstring.split(":");
        rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        //System.out.println("!!- " + "^" + foods.length + "^");
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (i = 0; i < foods.length; i++) {
                //System.out.println("!!- " + i + foods.length);
                Events myEvent = dbHandler.getMyEvent(foods[i]);
                listDataHeader.add(myEvent.get_eventname());
                List<String> child = new ArrayList<>();
                child.add(formatDateTime(myEvent.get_evtime(), myEvent.get_direction()));
                listDataChild.put(listDataHeader.get(i), child);
                rowID[i] = foods[i]; // store _id from database
            }
       // } else {
       //     Intent intent = new Intent(this, NoEventsExist.class);
       //     startActivity(intent);
            //System.out.println("!!- " + "here");
        }
    }

    public String formatDateTime(int eventTime,int direction){
        String[] d = new String[] {"up from","down to"};
        long millis = eventTime;
        millis *= 1000;
        DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");

        return "Count " + d[direction] + " " + dtf.print(dt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Not yet implemented
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent addAct = new Intent(MainActivity.this, EventEditor.class);
                long eventsInPlay = dbHandler.getRowCount("A"); //get number of active rows
                if (eventsInPlay >= maxAllowableEvents) {

                    Toast.makeText(getApplicationContext(), "You have too many events (" + eventsInPlay + ")" , Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(addAct);
                }
                return true;
            case R.id.action_utility:
                Intent utility = new Intent(MainActivity.this, Utility.class);
                startActivity(utility);
                return true;
            case R.id.action_deleted:
                Intent deleted = new Intent(MainActivity.this, DeletedItems.class);
                startActivity(deleted);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setGroupParents();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    public boolean deleteEvent(final int rowid){
        //System.out.println("!!- " + rowid);
        //final Events myEvent = dbHandler.getMyEvent(rowID);

        //Toast.makeText(getApplicationContext(), "Delete clicked "  + rowID, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Event?")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Click OK to delete the event")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean result = dbHandler.deleteEvent(rowID[rowid]);
                        if (result) {
                            Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
                            //finish();
                            onPause(); // call onpause so that on onresume can be called to refresh list
                            onResume();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

}