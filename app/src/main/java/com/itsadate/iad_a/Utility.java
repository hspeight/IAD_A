package com.itsadate.iad_a;

import android.app.Activity;
//import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.preference.PreferenceManager;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
import android.widget.EditText;

import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;



public class Utility extends Activity {

    EditText randnumber;
    public Events[] eventArray;
    MyDBHandler dbHandler;
    //Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        randnumber = (EditText) findViewById(R.id.editTextNumber);
        dbHandler = new MyDBHandler(this, null, null, 1);

        //ImageView imagePreview = (ImageView)findViewById(R.id.preview);
        //imagePreview.setAlpha(.33f);

        //imagePreview.setImageResource(R.drawable.wallpaper);

        // ---------------------------------------------------------------------
        //WallpaperManager myWallpaperManager
          //      = WallpaperManager.getInstance(getApplicationContext());
        //try {
            //imagePreview.setImageResource(R.drawable.wallpaper);
        //} catch (IOException e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
       // }
        //--------------------------------------------------------

    }


    public void loadEvents(View view) {

        long nowMinus24Hrs = (System.currentTimeMillis() - (60 * 60 * 24 * 1000));
        long nowPlus24Hrs = (System.currentTimeMillis() + (60 * 60 * 24 * 1000));
        long nowPlus1Min = (System.currentTimeMillis() + (60 * 1000));
        long nowPlus5Min = (System.currentTimeMillis() + (60 * 1000 * 5));
        //nowMinus24Hrs *= 1000;
        DateTime dt1 = new DateTime(nowMinus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt2 = new DateTime(nowPlus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt3 = new DateTime(nowPlus1Min, DateTimeZone.getDefault());
        DateTimeFormatter dtf3 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt4 = new DateTime(nowPlus5Min, DateTimeZone.getDefault());
        DateTimeFormatter dtf4 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
       // System.out.println("!!- " + dtf1.print(dt1) + "/" + dtf2.print(dt2));
        //System.out.println("!!- " + "Count up from a future date`0`" + nowPlus5Min / 1000 + "`I`1`0`0`1");

        String[] eString = new String[14];

        eString[0] = "Da Ting`Example of an event with optional text`0`1419825700`A`R`0`0`0";
        eString[1] = "Countdown to polling day`This is an example of an event with a very long optional text string that contains so much text it will probably go over multiple lines! Oh Gawd!`1`1430978400`A`R`0`1`0";
        eString[2] = "Count up from a future date`A`0`" + nowPlus5Min / 1000 + "`A`R`0`0`1";
        eString[3] = dtf4.print(dt4) + "``1`" + nowPlus5Min / 1000 + "`A`R`0`0`1";
        eString[4] = "This is the first ever event!``0`1423440390`A`R`1`1`1";
        eString[5] = "And this is the second``1`1424540190`I`R`0`0`0";
        eString[6] = "Curly Watts Anniversary Countdown``0`1414440190`I`R`1`1`0";
        eString[7] = "Since my 55th Birthday``0`1421317800`I`R`0`0`1";
        eString[8] = "Until my 56th Birthday``1`1452853800`A`R`1`1`0";
        eString[9] = dtf1.print(dt1) + "``0`" + nowMinus24Hrs / 1000 + "`A`R`1`0`1";
        eString[10] = dtf2.print(dt2) + "``0`" + nowPlus24Hrs / 1000 + "`A`R`0`0`1";
        eString[11] = dtf3.print(dt3) + "``1`" + nowPlus1Min / 1000 + "`A`R`0`0`1";
        eString[12] = "Event with a veeeeeeeeery long title to see how it looks in the list except it needs to be even longer``0`1414840190`A`R`0`0`1";
        eString[13] = "Since my 55th``0`1421318386`A`R`0`0`0";
        //eString[9] = dtf2.print(dt2) + "`1`" + nowPlus24Hrs / 1000 + "`0";

        //int i;

        //for (int i=0; i < eString.length; i++ ) {
        for (String s : eString) {

            //String[] parts = eString[i].split("`");
            String[] parts = s.split("`");

            //System.out.println("!!- " + i + " done");
            Events event = new Events(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4],
                    parts[5],Integer.parseInt(parts[6]),Integer.parseInt(parts[7]),Integer.parseInt(parts[8]),null,"[3]");
            dbHandler.addEvent(event);

        }

        long n = dbHandler.getRowCount("ALL"); // needs attention
        Toast.makeText(getApplicationContext(), "DB contains " + n + " events", Toast.LENGTH_SHORT).show();

    }

    public void clearEvents(View view) {

        dbHandler.deleteAllEvents("R"); // Real i.e. not samples
        dbHandler.deleteAllEvents("S");
        dbHandler.deleteAllEvents("T"); //Template
        Toast.makeText(getApplicationContext(), "All events cleared", Toast.LENGTH_SHORT).show();
    }

    public void clearPrefs(View view) {
        SharedPreferences settings;
        //SharedPreferences settings = getSharedPreferences("MyPreferences_001", Context.MODE_PRIVATE);
        //settings.edit().clear().apply();
        //settings = getSharedPreferences("MyPreferences_002", Context.MODE_PRIVATE);
        //settings.edit().clear().apply();
        settings = getSharedPreferences("MyPreferences_ftp", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().clear().apply();
        Toast.makeText(getApplicationContext(), "Phew!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void insertRandom (View view) {

        for(int i=0; i< Integer.parseInt(randnumber.getText().toString()); i++) {
            Events event = new Events("Event No " + i,
                                        "No optional text",
                                        0,
                                        1419825600,
                                        "A",
                                        "R",0,1,0,null, "[4]"
            );
            dbHandler.addEvent(event);
        }
    }
    public void dumpData(View view) {

        String evstring = dbHandler.getEventIDs("A"); // Fetch Id's of active events
        String[] foods = evstring.split(":");
        //System.out.println("!!- " + "foods=" + evstring);
        //String[] newfoods = rebuildArray(foods,initialId);
        //System.out.println("!!- " + "new foods=" + Arrays.toString(newfoods));

        //for (int i = 0; i < foods.length; i++) {
            //Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
           // System.out.println("!!- " + myEvent.get_id() + "/" +
           //         myEvent.get_eventname() + "/" +
           //         myEvent.get_bgimage()
            //);}
        //Toast.makeText(getApplicationContext(), "dumping", Toast.LENGTH_SHORT).show();
        //System.out.println("!!- " + "have a dump");
    }

    public void setInactive(View view) {

        String evstring = dbHandler.getEventIDs("A");
        //dbHandler.deleteAllEvents();
        //System.out.println("!!- " + evstring);
        String[] foods = evstring.split(":"); // array of row_id's
        eventArray = new Events[foods.length];
        for (int i = 0; i < foods.length; i++) {
            //eventArray[i] = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
            dbHandler.deleteEvent(Integer.parseInt(foods[i]));

        }
        //Toast.makeText(getApplicationContext(), "All events cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
