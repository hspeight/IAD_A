package com.itsadate.iad_a;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;
//import android.support.v7.app.ActionBarActivity;

public class FirstTime extends Activity {

    MyDBHandler dbHandler;

    int year;
    int NUM_EVENTS = 5;

    String[] info;
    String[] title;
    String[] date;
    int[] direction;
    int[] dy;
    int[] sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time);

        dbHandler = new MyDBHandler(this, null, null, 1);
        year = Calendar.getInstance().get(Calendar.YEAR);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

        title = new String[NUM_EVENTS];
        title[0] = "New Years Eve " + (year);
        title[1] = "Since I stopped smoking";
        title[2] = "This year's vacation";
        title[3] = "How old am I..?";
        title[4] = "A future event";

        info = new String[NUM_EVENTS];
        info[0] = "A count down to NYE";
        info[1] = "The best decision I ever made! (An example of an event showing years & days)";
        info[2] = "Can't wait, counting the seconds :-)";
        info[3] = "This is my age in days";
        info[4] = "You can create events that start some time in the future";

        date = new String[NUM_EVENTS];
        date[0] = "01/01/" + (year + 1) + " 00:00:00";
        date[1] = "11/07/2012 09:00:00";
        date[2] = dtf.print(getMyDTF(14 * 60 * 60 * 24 * 1000));
        date[3] = "14/11/1957 11:15:00";
        //System.out.println("!!- " + dtf.print(dt));
        //date[4] = dtf.print(dt);
        date[4] = dtf.print(getMyDTF(2 * 60 * 60 * 24 * 1000));

        direction = new int[NUM_EVENTS];
        direction[0] = 1; // down
        direction[1] = 0; // up
        direction[2] = 1; // down
        direction[3] = 0; // up
        direction[4] = 0; // up

        dy = new int[NUM_EVENTS];
        dy[0] = 0; // days only
        dy[1] = 1; // years/days
        dy[2] = 0; // days only
        dy[3] = 0; // days only
        dy[4] = 0; // days only

        sec = new int[NUM_EVENTS];
        sec[0] = 0; // 0 = dont show
        sec[1] = 0;
        sec[2] = 1;
        sec[3] = 0;
        sec[4] = 0;

    }

    public DateTime getMyDTF(int val) {

        long nowPlus24Hrs = (System.currentTimeMillis() + val);
        return new DateTime(nowPlus24Hrs, DateTimeZone.getDefault());
    }

    public void createSamples(View view) {

        for(int i = 0; i < title.length; i++ ) {
            Events myEvent = constructEvent(i);
            dbHandler.addEvent(myEvent);
        }
        firstTimeDoneWith();
        finish();
        //Toast.makeText(getApplicationContext(), "Create samples", Toast.LENGTH_SHORT).show();
    }
    public void dontCreateSamples(View view) {
        firstTimeDoneWith();
        finish(); // Get me outa here
    }

    public Events constructEvent(int i) {

        return new Events(title[i],
                info[i],
                direction[i], // direction
                getEpoch(date[i]),
                "A",
                0,
                0,
                sec[i], // show seconds ?
                dy[i], // days only or days & years
                null);

        //return myEvent;
    }

    public int getEpoch(String dateIn) {

        //int year = Calendar.getInstance().get(Calendar.YEAR);
        //year++;
        long epoch = 0;
        //String nextNYE = "01/01/" + year++ + " 00:00:00";
        //System.out.println("!!- " + nextNYE);
        try {
            epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).parse(dateIn).getTime();
           // System.out.println("!!- " + epoch);
           // System.out.println("!!- " + "!" + (int) (epoch / 1000));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //DateTime dt1 = new DateTime(epoch, DateTimeZone.getDefault());
        //DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        //System.out.println("!!- " + (int) (epoch / 1000));
        return (int) (epoch / 1000);
    }

    public void firstTimeDoneWith() {
        String FirstTimePref = "MyPreferences_ftp";
        SharedPreferences pref = getSharedPreferences (FirstTimePref, MODE_PRIVATE);
        //if(pref.getInt("FirstTime", 0) == 0) {
            SharedPreferences.Editor myEditor = pref.edit();
            myEditor.putInt("FirstTime", 1); // Set firsttime to true so we hopefully dont come here again
            myEditor.apply();
            //boolean result = AWarmWelcome();
        //}
    }
}