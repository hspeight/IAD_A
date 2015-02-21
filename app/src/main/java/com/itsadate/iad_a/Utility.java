package com.itsadate.iad_a;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Utility extends Activity {

    private static final String appKey = "m2vvdeh11l1bhwx";
    private static final String appSecret = "l0d08caoz7o4bi1";

    private static final int REQUEST_LINK_TO_DBX = 0;

    private TextView mTestOutput;
    private Button mLinkButton;


    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        dbHandler = new MyDBHandler(this, null, null, 1);

    }


    public void loadEvents(View view) {

        long nowMinus24Hrs = (System.currentTimeMillis() - (60 * 60 * 24 * 1000));
        long nowPlus24Hrs = (System.currentTimeMillis() + (60 * 60 * 24 * 1000));
        long nowPlus1Min = (System.currentTimeMillis() + (60 * 1000));
        //nowMinus24Hrs *= 1000;
        DateTime dt1 = new DateTime(nowMinus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt2 = new DateTime(nowPlus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt3 = new DateTime(nowPlus1Min, DateTimeZone.getDefault());
        DateTimeFormatter dtf3 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
       // System.out.println("!!- " + dtf1.print(dt1) + "/" + dtf2.print(dt2));
                //String twenty4HoursAgo = "24 hours ago";

        String[] eString = new String[9];

        eString[0] = "Da Ting`0`1419825600`0";
        eString[1] = "This is the first ever event!`0`1423440390`0";
        eString[2] = "And this is the second`1`1424540190`0";
        eString[3] = "Curly Watts Anniversary Countdown`0`1414440190`0";
        eString[4] = "Since my 55th Birthday`0`1421317800`0";
        eString[5] = "Until my 56th Birthday`1`1452853800`0";
        eString[6] = dtf1.print(dt1) + "`0`" + nowMinus24Hrs / 1000 + "`0";
        eString[7] = dtf2.print(dt2) + "`1`" + nowPlus24Hrs / 1000 + "`0";
        eString[8] = dtf3.print(dt3) + "`1`" + nowPlus1Min / 1000 + "`0";
        //eString[9] = dtf2.print(dt2) + "`1`" + nowPlus24Hrs / 1000 + "`0";

        int i;

        for ( i=0; i < eString.length; i++ ) {

            String[] parts = eString[i].split("`");

            //System.out.println("!!- " + i + " done");
            Events event = new Events(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            dbHandler.addEvent(event);

        }

        long n = dbHandler.getRowCount();
        Toast.makeText(getApplicationContext(), "DB contains " + n + " events", Toast.LENGTH_SHORT).show();

    }

    public void clearEvents(View view) {

        dbHandler.deleteAllEvents();
        Toast.makeText(getApplicationContext(), "All events cleared", Toast.LENGTH_SHORT).show();
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
