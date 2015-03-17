package com.itsadate.iad_a;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ShowCounter extends Activity {

    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    Calendar c = Calendar.getInstance();
    int secs;
    long modSecs; //need to learn how to cast
    int modDays;
    long mins;
    String rowID;
    CountDownTimer cdt;
    ProgressBar mProgressBar;
//    boolean mbActive;
//    int progressMade = 300000; // in ms --> 10s

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_counter);

        //final TextView textView2 = (TextView)findViewById(R.id.textView2);
        //Button myButton;
        dbHandler = new MyDBHandler(this, null, null, 1);

        // There should always be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rowID = bundle.getString("ROW_ID");
            //displayEventInfo(bundle);
        } else {
            System.out.println("!!- " + "error"); // need to do something better than this
            //textTit.setText("Error reading event row data");
        }


/*
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        final Thread timerThread = new Thread() {
            @Override
            public void run() {
                mbActive = true;
                try {
                    int waited = 0;
                    while(mbActive && (waited < progressMade)) {
                        sleep(200);
                        if(mbActive) {
                            waited += 200;
                            updateProgress(waited);
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    // do nothing
                }
            }
        };
        timerThread.start();
*/
    }
/*
    public void updateProgress(final int timePassed) {

        if(null != mProgressBar) {

            // Ignore rounding error here

            final int progress = mProgressBar.getMax() * timePassed / progressMade;

            mProgressBar.setProgress(progress);

        }

    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        displayEventInfo(rowID);
       // System.out.println("!!- resumed with rowid " + rowID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cdt.cancel(); //stop the timer
    }

    //String myDate = "2014-12-29 04:00:07 +0000";

        // final long timeDiff = Math.abs(startDate.getTime() - System.currentTimeMillis());
    public void displayEventInfo(String rowID) {
        final TextView textSecs = (TextView) findViewById(R.id.textSecs);
        final TextView textMins = (TextView) findViewById(R.id.textMins);
        final TextView textHour = (TextView) findViewById(R.id.textHour);
        final TextView textDays = (TextView) findViewById(R.id.textDays);
        final TextView textYears = (TextView) findViewById(R.id.textYears);
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/digital-7.ttf");
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/courier-new-italic-1361512243.ttf");
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/komika-title-tall-1361511394.ttf");
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/futurist-fixed-width-bold-1361537378.ttf");

        textYears.setTypeface(font);
        textDays.setTypeface(font);
        textHour.setTypeface(font);
        textMins.setTypeface(font);
        textSecs.setTypeface(font);

        TextView textTit = (TextView) findViewById(R.id.textTitle);
        TextView textStartDate = (TextView) findViewById(R.id.textStartDate);


        //String rowID;
        //String rowID = bundle.getString("ROW_ID");
        Events myEvent = dbHandler.getMyEvent(rowID);
        textTit.setText(myEvent.get_eventname());
        final int countDirection = myEvent.get_direction();

        if (myEvent.get_incsec() == 0)
            textSecs.setVisibility(View.GONE); // completely remove the secs view

        //System.out.println("!!- " + rowID);
        long millis = myEvent.get_evtime();
        //String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (millis*1000)); //test code
        //System.out.println("!!- millis=" + millis + "/" + date);
        millis *= 1000;
        DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        String myDate = dtf.print(dt);

        textStartDate.setText(myDate);
        long td = System.currentTimeMillis() / 1000;

        final int timeDiff = ((int) td) - myEvent.get_evtime() ;

        final int millisToStart = 86500000; //86400000 = milliseconds in 1 day

        cdt = new CountDownTimer(millisToStart, 1000) {
            public void onTick(long millisUntilFinished) {
                //progressMade = (int) millisUntilFinished;
                secs = timeDiff + ((millisToStart / 1000) - ((int) (millisUntilFinished / 1000)));
                if (countDirection == 1)
                    secs *= -1;
                //System.out.println("!!- secs=" + secs);
                modSecs = secs % 60;
                mins = (secs / 60) % 60;
               // System.out.println("!!- secs =" + secs + "/modsecs=" + modSecs + "/mins=" +mins);
                long hours = TimeUnit.SECONDS.toHours(secs) % 24;
                int days = (int) TimeUnit.SECONDS.toDays(secs);
                double years = days / 365.25;
                modDays = (int) Math.floor(days % 365.25);
                //System.out.println("!-" + hours);
                //textSecs.setText(String.valueOf((timeDiff - (millisUntilFinished / 1000)) + 20000));
                textSecs.setText(String.valueOf(modSecs));
                textMins.setText(String.valueOf(mins));
                textHour.setText(String.valueOf(hours));
                textDays.setText(String.valueOf(modDays));
                textYears.setText(String.valueOf((int)Math.floor(years)));

                if (secs < 0) {
                    // go to a new new activity?
                    //cdt.cancel();
                    //System.out.println("!!- cancelled");
                    //textSecs.setText("Yippee!");
                    Intent cdf = new Intent(getBaseContext(), CountdownFinished.class);
                    startActivity(cdf);
                    finish();
                   // return;
                }

            }
            public void onFinish(){
                textSecs.setText("done!");
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //    return true;
            //}
            //else if (id == R.id.action_add) {
            /** Called when the user clicks the add button */
            Intent intent = new Intent(this, ShowCounter.class);
            startActivity(intent);

            /* Context context = getApplicationContext();
            CharSequence text = "Add action pressed!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show(); */
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}