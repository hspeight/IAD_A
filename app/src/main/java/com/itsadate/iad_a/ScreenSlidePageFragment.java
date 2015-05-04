/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itsadate.iad_a;

import android.annotation.TargetApi;
import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
//import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ScreenSlidePageFragment extends Fragment {
    /*
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_ID = "id";
    public static final String ARG_TITLE = "title";
    public static final String ARG_INFO = "info";
    public static final String ARG_TIME = "time";
    public static final String ARG_DIRECTION = "direction";
    public static final String ARG_INCSEC = "incsec";
    public static final String ARG_USEDAYYEAR = "usedayyear";
    //public static final String ARG_BGCOLOR = "bgcolor";

    private int mId;
    private String mTitle;
    private String mInfo;
    private int mTime;
    private int mDirection;
    private int mIncsec;
    private int mUsedayyear;
    private int mBgcolor;
    private int mDigitcolor;
    private int mTextcolor;

    private ImageView courteney;
    int imgWidth;
    int imgHeight;

    TextView textSecs;
    TextView textMins;
    TextView textHour;
    TextView textDays;
    TextView textYears;

    CountDownTimer cdt;

    public static final String MyPREFERENCES = "MyPreferences_002";
    //int bgColor,counterColor,textColor;
    private static ScreenSlidePageFragment fragment;
    /*
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    //public static ScreenSlidePageFragment create(int num, String title) {
    public static ScreenSlidePageFragment create(SwipeItem myEvent) {
        //ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        //System.out.println("!!- " + event.get_id());
        //args.putInt(ARG_PAGE, num);
        //args.putInt(ARG_ID, num);
        args.putInt(ARG_ID, myEvent.getId());
        args.putString(ARG_TITLE, myEvent.getTitle());
        args.putString(ARG_INFO, myEvent.getInfo());
        args.putInt(ARG_TIME, myEvent.getTime());
        args.putInt(ARG_DIRECTION, myEvent.getDirection());
        args.putInt(ARG_INCSEC, myEvent.getIncsec());
        args.putInt(ARG_USEDAYYEAR, myEvent.getDayyear());
        //args.putInt(ARG_BGCOLOR, Color.CYAN);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); // required so that edit menu will show in fragment

        mId = getArguments().getInt(ARG_ID);
        mTitle = getArguments().getString(ARG_TITLE);
        mInfo = getArguments().getString(ARG_INFO);
        mTime = getArguments().getInt(ARG_TIME);
        mDirection = getArguments().getInt(ARG_DIRECTION);
        mIncsec = getArguments().getInt(ARG_INCSEC);
        mUsedayyear = getArguments().getInt(ARG_USEDAYYEAR);

        // Get instance to shared pref class
        SharedPreferences pref = getActivity().getSharedPreferences (MyPREFERENCES, Context.MODE_PRIVATE);
        mBgcolor = pref.getInt("timerBgColor", -16776961); // Get background color from pref file
        mDigitcolor = pref.getInt("timerCounterColor", -16776961); // Get timer digits color from pref file
        mTextcolor = pref.getInt("timerTextColor", -1); // Get text color from pref file

        //System.out.println("!!- " + "page no=" + getArguments().getInt(ARG_PAGE));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) //setBackground requires api 16
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.show_counter, container, false);
        TextView evTitle = (TextView) rootView.findViewById(R.id.textEvTitle);
        TextView evInfo = (TextView) rootView.findViewById(R.id.textOptionalInfo);
        textSecs = (TextView) rootView.findViewById(R.id.textSecs);
        textMins = (TextView) rootView.findViewById(R.id.textMins);
        textHour = (TextView) rootView.findViewById(R.id.textHour);
        textDays = (TextView) rootView.findViewById(R.id.textDays);
        textYears = (TextView) rootView.findViewById(R.id.textYears);
        //TextView textYearsLbl = (TextView) rootView.findViewById(R.id.textViewYearsLabel);
        TextView textFuture = (TextView) rootView.findViewById(R.id.textViewFuture);
        LinearLayout relLayout = (LinearLayout) rootView.findViewById(R.id.linLayoutCounterBG);
        relLayout.setBackgroundColor(mBgcolor);
        //LinearLayout lin1 = (LinearLayout) rootView.findViewById(R.id.linLayout1); // Contains linear layouts for yy,dd,hh,mm,ss
        courteney = (ImageView) rootView.findViewById(R.id.imageViewImg);
        //System.out.println("!!- " + courteney.getHeight() + "/" + courteney.getWidth());
        // Got the blank placeholder tip from https://github.com/square/picasso/issues/457
        Picasso.with(getActivity()).load(R.drawable.jennifer_aniston)
                .placeholder(R.drawable.blank_placeholder)
                .error(R.drawable.image_not_found)
                .fit()
                .centerInside()
                .into(courteney);

        evTitle.setText(mTitle);
        evTitle.setTextColor(mTextcolor);
        evInfo.setText(mInfo);
        evInfo.setTextColor(mTextcolor);

        textSecs.setTextColor(Color.RED);
        textMins.setTextColor(mDigitcolor);
        textHour.setTextColor(mDigitcolor);
        textDays.setTextColor(mDigitcolor);
        textYears.setTextColor(mDigitcolor);

        final LinearLayout linYears = (LinearLayout) rootView.findViewById(R.id.linearLayoutYears);
        //final LinearLayout linDays = (LinearLayout) rootView.findViewById(R.id.linearLayoutdays);
        //final LinearLayout linHours = (LinearLayout) rootView.findViewById(R.id.linearLayouthrs);
        //final LinearLayout linMins = (LinearLayout) rootView.findViewById(R.id.linearLayoutmins);
        final LinearLayout linSecs = (LinearLayout) rootView.findViewById(R.id.linearLayoutsecs);

        if (mIncsec == 0)
            linSecs.setVisibility(View.GONE); // completely remove the secs view
        else
            linSecs.setVisibility(View.VISIBLE);

        if (mUsedayyear == 0) { // 0 = days only

            //System.out.println("!!- " + "years lin layout width " + linYears.getWidth());
            linYears.setVisibility(View.GONE);
            // Gets the layout params that will allow you to resize the layout

            //ViewGroup.LayoutParams params = linDays.getLayoutParams();
            //params.width = 30; // doesn't matter what value this is as long as all 3 are the same
            //params = linHours.getLayoutParams();
            //params.width = 10;
            //params = linMins.getLayoutParams();
            //params.width = 10;
            //params = linSecs.getLayoutParams();
            //params.width = 1;
            //textYears.setVisibility(View.GONE);
            //textYearsLbl.setVisibility(View.GONE);
            //textDays.setTextScaleX(0.8f); // Should stop it overflowing if more than 3 digits
            //textDays.measure(0, 0);       //must call measure!
            //System.out.println("!!- " + textDays.getMeasuredWidth());
            //textDays.getMeasuredWidth(); //get width
            //textDays.setWidth(textDays.getMeasuredWidth() * 2);
            //lin1.setWeightSum(3); // Not exactly certain why this works but it does.
        } else {
            linYears.setVisibility(View.VISIBLE);
            //textYearsLbl.setVisibility(View.VISIBLE);
        }

        //long td = System.currentTimeMillis() / 1000;
        final long timeDiff = (System.currentTimeMillis() / 1000) - mTime;
        //System.out.println("!!- " + "timediff=" + timeDiff);
        //textFuture = (TextView) findViewById(R.id.textViewFuture);
        if (timeDiff < 0 && mDirection == 0) { // future count up
            //textFuture.setTypeface(font);
            textYears.setText("0");
            textDays.setText("0");
            textHour.setText("0");
            textMins.setText("0"); textSecs.setText("0");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
            textFuture.setText("Scheduled start: " + sdf.format((long) mTime * 1000));
            textFuture.setTextColor(mTextcolor);
            textFuture.setVisibility(View.VISIBLE);
            //timerIsRunning = false;
        } else {
            textFuture.setVisibility(View.INVISIBLE);
            //timerIsRunning = true;

        }
        //if(counter has a background image) {
        //relLayout.setBackground(setBackgroundImage("/storage/sdcard0/DCIM/.thumbnails/1.jpg"));
        //}
        //startTimer(myEvent);
        //System.out.println("!!- " + "create view");
        startTimer();
        return rootView;
    }
    public Drawable setBackgroundImage (String pathName) {
        //String pathName = "/sdcard/gif001.gif";
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        //BitmapDrawable bd = new BitmapDrawable(res, bitmap);
        //View view = findViewById(R.id.container);
        //view.setBackgroundDrawable(bd);
        return new BitmapDrawable(res, bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void startTimer() {
        //System.out.println("!!- " + rowID);
        //long millis = myEvent.get_evtime();
        long millis = mTime;
        //String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (millis*1000)); //test code
        //System.out.println("!!- millis=" + millis + "/" + date);
        millis *= 1000;
        //DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        //DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        //String myDate = dtf.print(dt);

        //textInfo.setText(myDate);
        long td = System.currentTimeMillis() / 1000;

        //final int timeDiff = ((int) td) - mTime ;
        final long timeDiff = td - mTime ;

        //final int millisToStart = 86500000; //86400000 = milliseconds in 1 day
        final long millisToStart = 86500000; //86400000 = milliseconds in 1 day

        cdt = new CountDownTimer(millisToStart, 1000) {
            public void onTick(long millisUntilFinished) {
                //int modDays, secs;
                long  secs;
                int modDays;
                long modSecs, mins;
                //progressMade = (int) millisUntilFinished;
                //secs = timeDiff + ((millisToStart / 1000) - ((int) (millisUntilFinished / 1000)));
                secs = timeDiff + ((millisToStart / 1000) - ((int) (millisUntilFinished / 1000)));
                //int millisUntilStart = myEvent.get_evtime();
                //System.out.println("!!- " + "timediff=" + timeDiff + " secs=" + secs + " until start=" + millisUntilStart);
                if (!(secs < 0 && mDirection == 0)) { // future count up but not there yet
                    //timerIsRunning = true; // doing this every second until a better way is found
                    // so dont display ANYTHING
                    if (mDirection == 1) // 1 = countdown
                        secs *= -1;
                    //System.out.println("!!- secs=" + secs);
                    modSecs = secs % 60;
                    mins = (secs / 60) % 60;
                    // System.out.println("!!- secs =" + secs + "/modsecs=" + modSecs + "/mins=" +mins);
                    long hours = TimeUnit.SECONDS.toHours(secs) % 24;
                    int days = (int) TimeUnit.SECONDS.toDays(secs);
                    double years = days / 365.25;
                    if (mUsedayyear == 0)
                        modDays = days;
                    else
                        modDays = (int) Math.floor(days % 365.25);
                    //System.out.println("!-" + hours);
                    //textSecs.setText(String.valueOf((timeDiff - (millisUntilFinished / 1000)) + 20000));
                    textSecs.setText(String.valueOf(modSecs));
                    textMins.setText(String.valueOf(mins));
                    textHour.setText(String.valueOf(hours));
                    //textDays.setText(String.valueOf(modDays));
                    textDays.setText(String.valueOf(modDays));
                    textYears.setText(String.valueOf((int) Math.floor(years)));
/*
                    if (secs < 0) {
                        // go to a new new activity?
                        //cdt.cancel();
                        //textSecs.setText("Yippee!");
                        Intent cdf = new Intent(getBaseContext(), CountdownFinished.class);
                        startActivity(cdf);
                        finish();
                        // return;
                    }
*/
                }
            }
            public void onFinish(){
                textSecs.setText("done!");
            }
        }.start();
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_counter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            //System.out.println("!!- " + mId);
            Intent intent = new Intent(getActivity(), EventEditor.class);
            intent.putExtra("ROW_ID", mId);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}