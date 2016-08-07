package com.itsadate.iad_a;

// Code courtesy of http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;

import android.content.Intent;

import android.content.SharedPreferences;

import android.os.Bundle;


import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.Toast;
//import android.widget.FrameLayout;
//import android.widget.ImageView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
//import org.joda.time.Duration;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends Activity
    implements EventDialog.OnDataPass {
    int maxAllowableEvents = 12; // not yet in use. Wil be 9999 in paid version

    MyDBHandler dbHandler;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    View linLayout;
    //View linLayoutParent;
    List<String> listDataHeader;
    List<String> listDataSubHeader;
    HashMap<String, List<String>> listDataChild;
    public Events[] eventArray;
    public static final String MyPREFERENCES = "MyPreferences_001";
    public static final String FirstTimePref = "MyPreferences_ftp";
    //public int firstTime = 0;
    //private ArrayList<Events> eventitem = new ArrayList<>();
    public String menuAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get instance to shared pref for first time check
        //SharedPreferences pref = getSharedPreferences (FirstTimePref, MODE_PRIVATE);
        //checkIfFirstTime(pref);

        dbHandler = new MyDBHandler(this, null, null, 1);
        setContentView(R.layout.main_activity);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        // for explanation see http://developer.android.com/guide/topics/ui/settings.html#Fragment

        setGroupParents();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataSubHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override   // collapse list
            public void onGroupExpand(int groupPosition) {
                //setImageRowPosition(groupPosition);
                if(groupPosition != previousItem ) {
                    expListView.collapseGroup(previousItem);
                    //expListView.setAlpha(0.75f);
                }
                previousItem = groupPosition;

            }
        });

        linLayout = findViewById(R.id.linLayoutMainBG);

        // If this is the fo
        //if( getSharedPreferences("FirstTime", 0).getBoolean("check", true))
        //{
        //    getSharedPreferences("FirstTime", 0) .edit().putBoolean("check", false);
        //    boolean result = AWarmWelcome();
        //}

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
        //Intent intent = new Intent(getBaseContext(), EventEditor.class);
        Intent intent = new Intent(getBaseContext(), CounterSettingsActivity.class);
        intent.putExtra("ROW_ID",eyd);
        startActivity(intent);

    }
    public void playIconClicked (View v){

        //System.out.println("!!- " + "Start time is  " + rowTime[(int)v.getTag()]);
        //Toast.makeText(getApplicationContext(), "Counter will start " +rowTime[(int)v.getTag()], Toast.LENGTH_SHORT).show();

        //Drawable playButton = v.getResources().getDrawable(R.drawable.ic_action_play_disabled);
        //playButton.setAlpha(Color.RED);
        int eyd = eventArray[(int)v.getTag()].get_id();
        //Intent intent = new Intent(getBaseContext(), ShowCounter.class);
        Intent intent = new Intent(getBaseContext(), ScreenSlidePageractivity.class);
        //intent.putExtra("ROW_ID",eyd);
        intent.putExtra("ROW_ID", (int)v.getTag());
        startActivity(intent);

    }

    // method to add parent & child events
    public void setGroupParents() {

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        //expListView.setBackgroundColor(Color.RED);
        String evstring = dbHandler.getEventIDs("A");
        //System.out.println("!!- " + "*" + evstring + "*");
        String[] foods = evstring.split(":"); // array of row_id's
        eventArray = new Events[foods.length];
        //Events[] iadevent = new Events[foods.length]; // array of event objects
        //rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        //rowTime = new int[foods.length];
        //System.out.println("!!- " + "^" + foods.length + "^");
        listDataHeader = new ArrayList<String>();
        listDataSubHeader = new ArrayList<String>();
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
                //generateInfo(eventArray[i].get_direction(), eventArray[i].get_evtime());
                listDataSubHeader.add(generateInfo(eventArray[i].get_direction(), eventArray[i].get_evtime()));
                List<String> child = new ArrayList<>();
                //child.add(formatDateTime(eventArray[i].get_evtime(), eventArray[i].get_direction()));
                child.add(eventArray[i].get_eventinfo()); // No text to display but required so that child will expand
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

    public String generateInfo(int direction, int dateTime) {

        String indicator = "\u25B2" + " From "; // Up arrow
        if(direction == 1)
            indicator = "\u25BC" + " To "; // Down arrow

        long millis = dateTime;
        millis *= 1000;
        //DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        //DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy hh:mm a", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());

        return sdf.format(new Date(millis));
        //return indicator + dtf.print(dt);

    }
/*
    public String formatDateTime(int eventTime,int direction){
        String[] d = new String[] {"up from\n","down to\n"};
        long millis = eventTime;
        millis *= 1000;
        DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");

        return "Count " + d[direction] + " " + dtf.print(dt);
    }
*/
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
        //System.out.println("!!- " + " Add ID is " + R.id.action_add + " and samples id is " + R.id.action_samples);
        menuAction = "";
        switch (item.getItemId()) {
            case R.id.action_add:
                //Intent addAct = new Intent(MainActivity.this, EventEditor.class);
                Intent addAct = new Intent(MainActivity.this, CounterSettingsActivity.class);
                //Intent.putExtra("ROW_ID","11");
                int eventsInPlay = dbHandler.getRowCount("ALL"); //get number of active rows
                //Toast.makeText(getApplicationContext(), "You already have " + eventsInPlay + " events", Toast.LENGTH_SHORT).show();
                if (eventsInPlay >= maxAllowableEvents) {
                    boolean response = setupDialog("Upgrade to Pro?","Yes","No");
                        //Toast.makeText(getApplicationContext(), "OK, Your loss pal" , Toast.LENGTH_SHORT).show();

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
            case R.id.action_samples:
                menuAction = "Sample";
                boolean response = setupDialog("Manage Sample Events","Restore","Delete");
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class); //will change to Settings.class when created
                //Intent settings = new Intent(MainActivity.this, ColorPickerPreference.class); //will change to Settings.class when created
                startActivity(settings);
                return true;
            case R.id.action_backrest:
                Intent backrest = new Intent(MainActivity.this, BackupAndRestore.class);
                startActivity(backrest);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AWarmWelcome() {
        //Toast.makeText(getApplicationContext(), "Welcome my friend" , Toast.LENGTH_SHORT).show();
        Intent firsttime = new Intent(MainActivity.this, FirstTime.class);
        startActivity(firsttime);
        //return true;
    }

    public boolean setupDialog(String message, String btnPos, String btnNeg) {
        //
        EventDialog eventDialog = new EventDialog();

        Bundle bundle = new Bundle();
        //System.out.println("!!- " + " rows selected=" + rowidsSelected);

        bundle.putString("dialogMessage", message);
        bundle.putString("buttonPos", btnPos);
        bundle.putString("buttonNeg", btnNeg);
        eventDialog.setArguments(bundle);
        //eventDialog.show(fm, "fragment_edit_name");
        eventDialog.show(getFragmentManager(), "dialog");

        return true;
    }

    @Override
    public void onDataPass(String data) {

        //if (data .equals("Yes")) { // Yes button was clicked in Alertdialog
        //    Toast.makeText(getApplicationContext(), "Good decision", Toast.LENGTH_SHORT).show();
        //} else {
        //    Toast.makeText(getApplicationContext(), "OK, Your loss pal" , Toast.LENGTH_SHORT).show();
        //}
        if(menuAction .equals("Sample")) {
            if (data .equals("No")) { // Sample menu item was selected and clear selected from dialog
                //Toast.makeText(getApplicationContext(), "Sample " + data, Toast.LENGTH_SHORT).show();
                dbHandler.manageSampleEvents("I"); // samples already exist but make them inactive
            } else {
                dbHandler.manageSampleEvents("A"); // create samples and make them inactive
            //    dbHandler.insertSampleEvents();
            }
            onPause(); // call onpause so that on onresume can be called to refresh list
            onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get instance to shared pref for first time check
        SharedPreferences pref = getSharedPreferences (FirstTimePref, MODE_PRIVATE);
        if(pref.getInt("FirstTime", 0) == 0) {
            AWarmWelcome();
            //if(firstTime == 0)
            //finish();
        }
        //System.out.println("!!- " + "starting main activity");
    }

    @Override
    protected void onResume() {
        super.onResume();

        setGroupParents();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataSubHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Get instance to shared pref class
        SharedPreferences pref = getSharedPreferences (MyPREFERENCES, MODE_PRIVATE);

        int bgColor = pref.getInt("listBgColor", -16776961); // Get background color from pref file
        linLayout.setBackgroundColor(bgColor);

        //setTitle(getTitle() + " (" + dbHandler.getRowCount("A") + ")"); // put number of active events in title bar
        setTitle(getString(R.string.app_name) + " (" + dbHandler.getRowCount("A") + ")"); // put number of active events in title bar

    }

    //public void clearSampleEvents() {

    //}
}