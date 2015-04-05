package com.itsadate.iad_a;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ShowCounter extends Activity {

    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    public static final String MyPREFERENCES = "MyPreferences_002";
    int secs;
    long modSecs; //need to learn how to cast
    int modDays;
    long mins;
    int rowID;
    CountDownTimer cdt;
    View relLayout;
    int bgColor,counterColor,textColor;
    TextView textFuture;
    TextView textSecs, textMins, textHour, textDays, textYears;
    private boolean timerIsRunning;

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_counter);

        // Get instance to shared pref class
        SharedPreferences pref = getSharedPreferences (MyPREFERENCES, MODE_PRIVATE);
        bgColor = pref.getInt("timerBgColor", -16776961); // Get background color from pref file
        counterColor = pref.getInt("timerCounterColor", -16776961); // Get background color from pref file
        textColor = pref.getInt("timerTextColor", -1); // Get text color from pref file

        relLayout = findViewById(R.id.relLayoutCounterBG);
        relLayout.setBackgroundColor(bgColor);

        dbHandler = new MyDBHandler(this, null, null, 1);

        // There should always be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rowID = bundle.getInt("ROW_ID"); // why not pass the event then you dont need to read db again ???
            //displayEventInfo(bundle);
        } else {
            System.out.println("!!- " + "error"); // need to do something better than this
            //textTit.setText("Error reading event row data");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        displayEventInfo(rowID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (timerIsRunning)
            cdt.cancel(); //stop the timer
    }

    public void displayEventInfo(int rowID) {
        textSecs = (TextView) findViewById(R.id.textSecs);
        textMins = (TextView) findViewById(R.id.textMins);
        textHour = (TextView) findViewById(R.id.textHour);
        textDays = (TextView) findViewById(R.id.textDays);
        textYears = (TextView) findViewById(R.id.textYears);

        textSecs.setTextColor(counterColor);
        textMins.setTextColor(counterColor);
        textHour.setTextColor(counterColor);
        textDays.setTextColor(counterColor);
        textYears.setTextColor(counterColor);
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/digital-7.ttf");
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/courier-new-italic-1361512243.ttf");
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/komika-title-tall-1361511394.ttf");
        //Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/futurist-fixed-width-bold-1361537378.ttf");

        textYears.setTypeface(font);
        textDays.setTypeface(font);
        textHour.setTypeface(font);
        textMins.setTypeface(font);
        textSecs.setTypeface(font);


        TextView textTit = (TextView) findViewById(R.id.textEvTitle);
        textTit.setTextColor(textColor);
        TextView textInfo = (TextView) findViewById(R.id.textOptionalInfo);
        textInfo.setTextColor(textColor);
        TextView textYearsLbl = (TextView) findViewById(R.id.textViewYearsLabel);
        LinearLayout lin1 = (LinearLayout) findViewById(R.id.linLayout1); // Contains linear layouts for yy,dd,hh,mm,ss


        //String rowID;
        //String rowID = bundle.getString("ROW_ID");
        //final Events myEvent = dbHandler.getMyEvent(Integer.parseInt(rowID));
        final Events myEvent = dbHandler.getMyEvent(rowID);
        textTit.setText(myEvent.get_eventname());
        textInfo.setText(myEvent.get_eventinfo());
        final int countDirection = myEvent.get_direction();

        if (myEvent.get_incsec() == 0)
            textSecs.setVisibility(View.GONE); // completely remove the secs view
        else
            textSecs.setVisibility(View.VISIBLE);

        if (myEvent.get_dayyears() == 0) { // 0 = days only
            textYears.setVisibility(View.GONE);
            textYearsLbl.setVisibility(View.GONE);
            textDays.setTextScaleX(0.8f); // Should stop it overflowing if more than 3 digits
            lin1.setWeightSum(5); // Not exactly certain why this works but it does.
        } else {
            textYears.setVisibility(View.VISIBLE);
            textYearsLbl.setVisibility(View.VISIBLE);
        }

        //long td = System.currentTimeMillis() / 1000;
        final long timeDiff = (System.currentTimeMillis() / 1000) - myEvent.get_evtime() ;

        textFuture = (TextView) findViewById(R.id.textViewFuture);
        if (timeDiff < 0 && myEvent.get_direction() == 0) { // future count up
            //textFuture.setTypeface(font);
            textYears.setText("0");
            textDays.setText("0");
            textHour.setText("0");
            textMins.setText("0"); textSecs.setText("0");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
            textFuture.setText("Scheduled start: " + sdf.format((long)myEvent.get_evtime() * 1000));
            textFuture.setTextColor(textColor);
            textFuture.setVisibility(View.VISIBLE);
            //timerIsRunning = false;
        } else {
            textFuture.setVisibility(View.INVISIBLE);
            //timerIsRunning = true;

        }
        startTimer(myEvent);
    }

    public void startTimer(final Events myEvent) {
        //System.out.println("!!- " + rowID);
        long millis = myEvent.get_evtime();
        //String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (millis*1000)); //test code
        //System.out.println("!!- millis=" + millis + "/" + date);
        millis *= 1000;
        //DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        //DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        //String myDate = dtf.print(dt);

        //textInfo.setText(myDate);
        long td = System.currentTimeMillis() / 1000;

        final int timeDiff = ((int) td) - myEvent.get_evtime() ;

        final int millisToStart = 86500000; //86400000 = milliseconds in 1 day

        cdt = new CountDownTimer(millisToStart, 1000) {
            public void onTick(long millisUntilFinished) {
                //progressMade = (int) millisUntilFinished;
                secs = timeDiff + ((millisToStart / 1000) - ((int) (millisUntilFinished / 1000)));
                int millisUntilStart = myEvent.get_evtime();
                //System.out.println("!!- " + "timediff=" + timeDiff + " secs=" + secs + " until start=" + millisUntilStart);
                if (!(secs < 0 && myEvent.get_direction() == 0)) { // future count up but not there yet
                    timerIsRunning = true; // doing this every second until a better way is found
                    // so dont display ANYTHING
                    if (myEvent.get_direction() == 1) // 1 = countdown
                        secs *= -1;
                    //System.out.println("!!- secs=" + secs);
                    modSecs = secs % 60;
                    mins = (secs / 60) % 60;
                    // System.out.println("!!- secs =" + secs + "/modsecs=" + modSecs + "/mins=" +mins);
                    long hours = TimeUnit.SECONDS.toHours(secs) % 24;
                    int days = (int) TimeUnit.SECONDS.toDays(secs);
                    double years = days / 365.25;
                    if (myEvent.get_dayyears() == 0)
                        modDays = days;
                    else
                        modDays = (int) Math.floor(days % 365.25);
                    //System.out.println("!-" + hours);
                    //textSecs.setText(String.valueOf((timeDiff - (millisUntilFinished / 1000)) + 20000));
                    textSecs.setText(String.valueOf(modSecs));
                    textMins.setText(String.valueOf(mins));
                    textHour.setText(String.valueOf(hours));
                    textDays.setText(String.valueOf(modDays));
                    textYears.setText(String.valueOf((int) Math.floor(years)));

                    if (secs < 0) {
                        // go to a new new activity?
                        //cdt.cancel();
                        //textSecs.setText("Yippee!");
                        Intent cdf = new Intent(getBaseContext(), CountdownFinished.class);
                        startActivity(cdf);
                        finish();
                        // return;
                    }
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
        getMenuInflater().inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            Intent intent = new Intent(this, EventEditor.class);
            intent.putExtra("ROW_ID",rowID);
            startActivity(intent);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}