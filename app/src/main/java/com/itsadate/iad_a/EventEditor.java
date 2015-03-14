package com.itsadate.iad_a;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
//import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.joda.time.DateTime;
//import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EventEditor extends Activity
        implements EventDialog.OnDataPass { //Required for interaction between fragment & activity

    //public class EventEditor extends Activity implements View.OnClickListener {

    private View myView;
    //private TextView pDisplayDate;
//    private TextView textTime;
    private TextView textUpDown;
    private Button dateButton;
    private Button timeButton;
    private int pYear;
    private int pMonth;
    private int pDay;
    private int mHour;
    private int mMinute;
    private int idx_cd;
    private int idx_dy;
    private int chk_hrs;
    private int chk_min;
    private int chk_sec;
    private int timeInSeconds;
    private String tranType;
    //private int countDirection;
    private String rowID;
    private String activityDataIn;
    private String activityDataOut;
    //private String collateActivityInfo;

    private RadioButton countUp = null;
    private RadioButton countDown = null;
    private RadioButton daysOnly = null;
    private RadioButton daysAndYears = null;
    private CheckBox includeHrs = null;
    private CheckBox includeMin = null;
    private CheckBox includeSec = null;

    private RadioGroup cd;
    private RadioGroup dy;

    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
    static final int DATE_DIALOG_ID = 0;

    EditText hsEditText;
    MyDBHandler dbHandler;

    /** Updates the date in the TextView */
    private void updateDisplay() {

        //addButton.setEnabled(true);

        //  boolean invalidDate = false;
        final DateTime dtNow = new DateTime();

        if (idx_cd == 1) {
            textUpDown.setText("Target Date & Time:");
            cd.check(R.id.radioButtonCountDown);
        } else {
            textUpDown.setText("Start Date & Time:");
            cd.check(R.id.radioButtonCountUp);
        }
        if (idx_dy == 1) {
            dy.check(R.id.radioButtonYearsAndDays);
        } else {
            dy.check(R.id.radioButtonDaysOnly);
        }
        if (chk_hrs == 1) { includeHrs.setChecked(true); }
        if (chk_min == 1) { includeMin.setChecked(true); }
        if (chk_sec == 1) { includeSec.setChecked(true); }

        //pDisplayDate.setTextColor(Color.BLUE);
//        textTime.setTextColor(Color.BLUE);
        final LocalDateTime dt = new LocalDateTime(pYear, pMonth + 1, pDay, 0, 0);
        String month = dt.monthOfYear().getAsShortText();
        //pDisplayDate.setText(pDay + " " + month  + " " + pYear);
        dateButton.setText(pDay + " " + month  + " " + pYear);
        timeButton.setText(mHour + ":" + mMinute);

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    //private int validateInDate(String inDate) {
    private int validateInDate(int timeInSecs) {

        //DateTime cd = new DateTime(); //current date
        long epoch1 = System.currentTimeMillis() / 1000;
        int epoch = (int) epoch1;
        //int epoch = (int) System.currentTimeMillis() / 1000;
        //return(DateTimeComparator.getDateOnlyInstance().compare(inDate, cd));
        //System.out.println("!!- " + epoch + "/" + timeInSecs + "/" + (epoch - timeInSecs));
        return epoch - timeInSecs;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_editor);
        myView = findViewById(R.id.viewID); // reference to view to be used in addbuttonpressed method
        hsEditText = (EditText) findViewById(R.id.hsEditText);
        textUpDown = (TextView) findViewById(R.id.textViewDirection);
        //final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioDirection);

        dateButton = (Button) findViewById(R.id.buttonCounterDate);
        timeButton = (Button) findViewById(R.id.buttonCounterTime);

        cd = (RadioGroup) findViewById(R.id.radioDirection); // Count up/down
        dy = (RadioGroup) findViewById(R.id.radioYearsDays); // Days/Days+years
        //final RadioGroup cd = (RadioGroup) findViewById(R.id.radioDirection); // Count up/down
        //final RadioGroup dy = (RadioGroup) findViewById(R.id.radioYearsDays); // Days/Days+years
        //final int radioButtonID = cd.getCheckedRadioButtonId();
        //final int radioButtonIDdy = dy.getCheckedRadioButtonId();
        //final View radioButtoncd = cd.findViewById(radioButtonID);
        //final View radioButtondy = dy.findViewById(radioButtonIDdy);
        //idx_cd = cd.indexOfChild(radioButtoncd);
        //idx_dy = dy.indexOfChild(radioButtondy);

        countUp = (RadioButton) findViewById(R.id.radioButtonCountUp);
        countDown = (RadioButton) findViewById(R.id.radioButtonCountDown);
        daysOnly = (RadioButton) findViewById(R.id.radioButtonDaysOnly);
        daysAndYears = (RadioButton) findViewById(R.id.radioButtonYearsAndDays);
        includeHrs = (CheckBox) findViewById(R.id.checkBoxHours);
        includeMin = (CheckBox) findViewById(R.id.checkBoxMins);
        includeSec = (CheckBox) findViewById(R.id.checkBoxSecs);

        tranType = "add";

        dbHandler = new MyDBHandler(this, null, null, 1);

        // If this is an edit there will be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            tranType = "update";
            rowID = bundle.getString("ROW_ID");
            Events myEvent = dbHandler.getMyEvent(rowID);
            hsEditText.setText(myEvent.get_eventname());
            idx_cd = myEvent.get_direction();
            idx_dy = myEvent.get_dayyears();
            chk_hrs = myEvent.get_inchrs();
            chk_min = myEvent.get_incmin();
            chk_sec = myEvent.get_incsec();

            long millis = myEvent.get_evtime();
            millis *= 1000;
            DateTime dt = new DateTime(millis, DateTimeZone.getDefault());
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");

            pYear = Integer.parseInt(dtf.print(dt).split("-")[0]);
            pMonth = (Integer.parseInt(dtf.print(dt).split("-")[1]) - 1);
            pDay = Integer.parseInt(dtf.print(dt).split("-")[2]);
            mHour = Integer.parseInt(dtf.print(dt).split("-")[3]);
            mMinute = Integer.parseInt(dtf.print(dt).split("-")[4]);
        } else {
            final Calendar cal = Calendar.getInstance();
            pYear = cal.get(Calendar.YEAR);
            pMonth = cal.get(Calendar.MONTH);
            pDay = cal.get(Calendar.DAY_OF_MONTH);
            mHour = cal.get(Calendar.HOUR_OF_DAY);
            mMinute = cal.get(Calendar.MINUTE);
            idx_cd = 1; idx_dy = 1;
            chk_hrs = 1; chk_min = 0; chk_sec =1;
        }
        //if (idx_cd == 1)
            //cd.check(R.id.radioButtonCountDown);
        //else
            //cd.check(R.id.radioButtonCountUp);
        //if (idx_dy == 1)
            //dy.check(R.id.radioButtonYearsAndDays);
        //else
            //dy.check(R.id.radioButtonDaysOnly);

        updateDisplay();

        /** Listener for click event of the date button */
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        /** Listener for click event of the time button */
        timeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        //final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioDirection);
        cd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idx_cd = countDown.isChecked() ? 1 : 0; // convert direction button to int
                //Toast.makeText(getApplicationContext(), "button is "  + countUp.isChecked(), Toast.LENGTH_SHORT).show();
                updateDisplay();
            }
        });

        // Inflate a "Done" custom action bar view to serve as the "Up" affordance.
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_cust_event_editor, null);

        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addButtonClicked(v);
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
        // END_INCLUDE (inflate_set_custom_view)
       // setContentView(R.layout.activity_done_button);

    }

    public void addButtonClicked(View view) {

        String EventTitle = hsEditText.getText().toString();
        if(EventTitle.isEmpty()) {
            Toast mytoast = Toast.makeText(getApplicationContext(),"Please  title your event",Toast.LENGTH_SHORT);
            //        Toast.makeText(getApplicationContext(), "Please  title your event", Toast.LENGTH_SHORT).show();
            mytoast.setGravity(Gravity.CENTER, 0, 0);
            mytoast.show();
            return;
        }

        String givenDateString = dateButton.getText() + " " + timeButton.getText(); //e.g. 7 Jul 2014 15:30
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        try {
            Date mDate = sdf.parse(givenDateString);
            //long timeInMillis = mDate.getTime(); // Time entered in millis
            timeInSeconds = (int)(mDate.getTime() / 1000);
            int diffInSecs = validateInDate(timeInSeconds);
            //System.out.println("!!- "  + "diff in secs is " + diffInSecs);
            if (idx_cd == 0 && diffInSecs < 0) { // Count up selected but date is in future
                //pDisplayDate.setTextColor(Color.RED);
//                textTime.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "his must be a past date", Toast.LENGTH_SHORT).show();
                //addButton.setEnabled(false);
                return;
            } else {
                if (idx_cd == 1 && diffInSecs > 0) { // Count down selected but date is not in future
                    //pDisplayDate.setTextColor(Color.RED);
//                    textTime.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "This must be a future date", Toast.LENGTH_SHORT).show();
                    //addButton.setEnabled(false);
                    return;
                }
            }
        } catch (ParseException e) {
            //System.out.println("!!- " + pDisplayDate.getText() + dbTime + "bad!");
            e.printStackTrace();
        }
        //Events event = new Events(EventTitle, idx, timeInSeconds, cidx);
        //int rowsInDB = dbHandler.getRowCount()
        //System.out.println("!!- "  + dbHandler.getRowCount());
        if (tranType.equals("update")) {
            Events event = new Events(Integer.parseInt(rowID), EventTitle, idx_cd, timeInSeconds, "A", 1,0,1,idx_dy);
            dbHandler.updateEvent(event);
            //Toast.makeText(getApplicationContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            Events event = new Events(EventTitle, idx_cd, timeInSeconds, "A", 0,1,0,idx_dy);
            dbHandler.addEvent(event);
            //Toast.makeText(getApplicationContext(), "Your event has been created", Toast.LENGTH_SHORT).show();
        }
        finish(); // return to previous activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_event_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
        //    case R.id.action_delete:
        //        // deleteEvent();
        //        return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        activityDataIn = collateActivityInfo();
    }

    @Override
    public void onBackPressed() {

        activityDataOut = collateActivityInfo();

        if (! activityDataIn .equals(activityDataOut) ) {
            // Create the fragment and show it as a dialog.
            EventDialog eventDialog = new EventDialog();
            Bundle bundle = new Bundle();
            bundle.putString("dialogMessage", "Save changes before exit?");
            eventDialog.setArguments(bundle);
            //eventDialog.show(fm, "fragment_edit_name");
            eventDialog.show(getFragmentManager(), "dialog");
        } else {
            // No changes detected
            finish();
        }
    }

    @Override
    public void onDataPass(String data) {

        if (data .equals("Yes")) {
            addButtonClicked(myView);
        } else {
            finish();
        }
        //Toast.makeText(getApplicationContext(), "Edit activity - "  + data, Toast.LENGTH_SHORT).show();
        //Log.d("LOG","hello " + data);
    }

    // Concatenate values into a string for comparison
    public String collateActivityInfo() {

        return hsEditText.getText().toString()
                + countUp.isChecked()
                + dateButton.getText()
                + timeButton.getText()
                + daysOnly.isChecked()
                + includeHrs.isChecked()
                + includeMin.isChecked()
                + includeSec.isChecked();
    }

    private void showDatePicker() {
// needs to set correct date. new=current. update=db value
        DatePickerFragment dialog = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_id", 1); // 1=Date Picker
        bundle.putInt("year", pYear);
        bundle.putInt("month", pMonth);
        bundle.putInt("day", pDay);
        dialog.setArguments(bundle);
        dialog.setCallBack(ondate);

        dialog.show(getFragmentManager(), "dialog");
    }

    private void showTimePicker() {

        DatePickerFragment dialog = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_id", 2); // 2=Time Picker
        bundle.putInt("hour", mHour);
        bundle.putInt("minute", mMinute);
        dialog.setArguments(bundle);
        dialog.setCallBack(ontime);
        dialog.show(getFragmentManager(), "dialog");
    }

    OnDateSetListener ondate = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
    /*
            Toast.makeText(
                    EventEditor.this,
                    String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                            + "-" + String.valueOf(dayOfMonth),
                    Toast.LENGTH_SHORT).show();
    */
            pYear = year;
            pMonth = monthOfYear;
            pDay = dayOfMonth;
            //System.out.println("!!- " + "here2");
            updateDisplay();
        }
    };

    OnTimeSetListener ontime = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            mHour = hourOfDay;
            mMinute = minute;
    /*
            Toast.makeText(
                    EventEditor.this,
                    String.valueOf(mHour) + ":" + String.valueOf(mMinute),
                    Toast.LENGTH_SHORT).show();
    */
            updateDisplay();
        }
    };

}
