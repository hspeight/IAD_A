package com.itsadate.iad_a;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;
//import android.support.v7.app.ActionBarActivity;

/* If the user chooses to create sample events, add them into the DB with type=S and status=A
   otherwise add them with type=S and status=I
 */
public class FirstTime extends Activity {

    MyDBHandler dbHandler;

    int year;
    String status, type;
    int NUM_EVENTS = 6;

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
        dbHandler.deleteAllEvents("S"); // Samples i.e. not real ones

        year = Calendar.getInstance().get(Calendar.YEAR);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

        title = new String[NUM_EVENTS];
        title[0] = ""; // blank for template
        title[1] = "New Years Eve " + (year);
        title[2] = "Since I stopped smoking";
        title[3] = "This year's vacation";
        title[4] = "How old am I..?";
        title[5] = "A future count up event";
        //title[6] = "A future count down event";

        info = new String[NUM_EVENTS];
        info[0] = ""; // blank for template
        info[1] = "A count down to New Year's Eve";
        info[2] = "The best decision I ever made! (An example of an event showing years & days)";
        info[3] = "Can't wait, counting the seconds :-)";
        info[4] = "This is my age in days";
        info[5] = "Create events that count up from some time in the future";
        //info[6] = "Create events that count down from some time in the future";

        date = new String[NUM_EVENTS];
        date[0] = dtf.print(getMyDTF(14 * 60 * 60 * 24 * 1000)); // needs to be programatically set to current date/time
        date[1] = "01/01/" + (year + 1) + " 00:00:00";
        date[2] = "11/07/2012 09:00:00";
        date[3] = dtf.print(getMyDTF(14 * 60 * 60 * 24 * 1000));
        date[4] = "14/11/1957 11:15:00";
        date[5] = dtf.print(getMyDTF(2 * 60 * 60 * 24 * 1000));
        //date[6] = dtf.print(getMyDTF(2 * 60 * 60 * 24 * 1000));

        direction = new int[NUM_EVENTS]; // 0 = count up, 1 = count down
        direction[0] = 1;
        direction[1] = 1;
        direction[2] = 0;
        direction[3] = 1;
        direction[4] = 0;
        direction[5] = 0;
        //direction[6] = 1;

        dy = new int[NUM_EVENTS]; // 1 = display years
        dy[0] = 0;
        dy[1] = 0;
        dy[2] = 0;
        dy[3] = 0;
        dy[4] = 0;
        dy[5] = 0;
        //dy[6] = 0;

        sec = new int[NUM_EVENTS]; // 1 = display seconds
        sec[0] = 0;
        sec[1] = 0;
        sec[2] = 0;
        sec[3] = 0;
        sec[4] = 0;
        sec[5] = 0;
        //sec[6] = 0;

        //Log.d("First Time", " it's in");
        // hopefully we will only be in here once so set up the event template now
        status = "I"; // Inactive
        type = "T"; // Template
        Events myEvent = constructEvent(0); // template is first element
        dbHandler.addEvent(myEvent);
    }

    public DateTime getMyDTF(int val) {

        long nowPlus24Hrs = (System.currentTimeMillis() + val);
        return new DateTime(nowPlus24Hrs, DateTimeZone.getDefault());
    }

    public void createSamplesActive(View view) {
        status = "A"; // create active samples
        type = "S"; // Sample
        for(int i = 1; i < title.length; i++ ) {  // dont include template in element 0
            Events myEvent = constructEvent(i);
            dbHandler.addEvent(myEvent);
        }
        firstTimeDoneWith();
        finish();
        //Toast.makeText(getApplicationContext(), "Samples can be removed from the menu", Toast.LENGTH_LONG).show();
    }
    public void createSamplesInactive(View view) {
        status = "I"; // create inactive samples
        type = "S"; // Sample
        for(int i = 1; i < title.length; i++ ) {  // dont include template in element 0
            Events myEvent = constructEvent(i);
            dbHandler.addEvent(myEvent);
        }
        firstTimeDoneWith();
        finish(); // Get me outa here
    }

    public Events constructEvent(int i) {

        return new Events(title[i],
                info[i],
                direction[i], // direction
                getEpoch(date[i]),
                status,
                type,
                0,
                sec[i], // show seconds ?
                dy[i], // days only or days & years
                null,
                ""); //time units

        //return myEvent;
    }

    public int getEpoch(String dateIn) {

        //int year = Calendar.getInstance().get(Calendar.YEAR);
        //year++;
        long epoch = 0;
        //String nextNYE = "01/01/" + year++ + " 00:00:00";
        //System.out.println("!!- " + nextNYE);
        try {
            epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).parse(dateIn).getTime();
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