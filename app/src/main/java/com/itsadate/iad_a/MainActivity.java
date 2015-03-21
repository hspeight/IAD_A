package com.itsadate.iad_a;

// Code courtesy of http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
//import android.app.AlertDialog;

//import android.content.DialogInterface;
import android.content.Intent;

//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ExpandableListView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;

import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
//import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends Activity {

    int maxAllowableEvents = 12; // not yet in use. Wil be 9999 in paid version

    MyDBHandler dbHandler;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public Events[] eventArray;
    //public Events iadevent[];
    //public String rowID []; // Array of _id column from database
    //public int rowTime []; // Array of event time column from database

    //public ImageView play = null;
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
                //Duration duration = new Duration(rowTime[groupPosition],currentTime)
                //if (rowTime[groupPosition] <
                //System.out.println("!!- " + "groupPosition is  " + groupPosition + "/" + rowTime[groupPosition]);
                //System.out.println("!!- " + (rowTime[groupPosition] - System.currentTimeMillis() / 1000));

            }
        });

    }

    public void deleteIconClicked (View v){
        // System.out.println("!!-  tag is " + v.getTag().toString());
        // Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
        // deleteEvent(v.getTag().toString());
        //deleteEvent((int)v.getTag());
        //dbHandler.deleteEvent(rowID[(int)v.getTag()]);
        int eyd = eventArray[(int)v.getTag()].get_id();
        dbHandler.deleteEvent(eyd);
        //System.out.println("!!-  tag is " + rowID[(int)v.getTag()]);
        onPause(); // call onpause so that on onresume can be called to refresh list
        onResume();

    }
    public void editIconClicked (View v){

        int eyd = eventArray[(int)v.getTag()].get_id();
        Intent intent = new Intent(getBaseContext(), EventEditor.class);
        intent.putExtra("ROW_ID",eyd);
        startActivity(intent);

    }
    public void playIconClicked (View v){

        //System.out.println("!!- " + "Start time is  " + rowTime[(int)v.getTag()]);
        //Toast.makeText(getApplicationContext(), "Counter will start " +rowTime[(int)v.getTag()], Toast.LENGTH_SHORT).show();

        //Drawable playButton = v.getResources().getDrawable(R.drawable.ic_action_play_disabled);
        //playButton.setAlpha(Color.RED);
        int eyd = eventArray[(int)v.getTag()].get_id();
        Intent intent = new Intent(getBaseContext(), ShowCounter.class);
        intent.putExtra("ROW_ID",eyd);
        startActivity(intent);

    }

    // method to add parent & child events
    public void setGroupParents() {

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        String evstring = dbHandler.getActiveEventIDs("A");
        //System.out.println("!!- " + "*" + evstring + "*");
        String[] foods = evstring.split(":"); // array of row_id's
        eventArray = new Events[foods.length];
        //Events[] iadevent = new Events[foods.length]; // array of event objects
        //rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        //rowTime = new int[foods.length];
        //System.out.println("!!- " + "^" + foods.length + "^");
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (i = 0; i < foods.length; i++) {
                //System.out.println("!!- " + i + foods.length);
                //Events myEvent = dbHandler.getMyEvent(foods[i]);
                eventArray[i] = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
                //eventArray[i] = new Events(1,"This is a sring");
                //eventArray[i].e
                listDataHeader.add(eventArray[i].get_eventname());
                List<String> child = new ArrayList<>();
                child.add(formatDateTime(eventArray[i].get_evtime(), eventArray[i].get_direction()));
                listDataChild.put(listDataHeader.get(i), child);
                //rowID[i] = foods[i]; // store _id from database
                //rowTime[i] = iadevent[i].get_evtime();
            }
       // } else {
       //     Intent intent = new Intent(this, NoEventsExist.class);
       //     startActivity(intent);
            //System.out.println("!!- " + "here");
        }
    }

    public String formatDateTime(int eventTime,int direction){
        String[] d = new String[] {"up from\n","down to\n"};
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
                int eventsInPlay = dbHandler.getRowCount("A"); //get number of active rows
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
            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, Utility.class); //will change to Settings.class when created
                startActivity(settings);
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
}