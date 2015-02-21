package com.itsadate.iad_a;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class EventEditor extends Activity {
    //public class EventEditor extends Activity implements View.OnClickListener {

    private TextView pDisplayDate;
    private TextView textTime;
    private TextView textUpDown;
    //private Button addButton;
    private int pYear;
    private int pMonth;
    private int pDay;
    private int mHour;
    private int mMinute;
    private int idx;
    //private int cidx;
    private int timeInSeconds;
    private String tranType;
    private int countDirection;
    //private int useTime;
    private String rowID;
    private String activityDataIn;
    private String activityDataOut;

    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
    static final int DATE_DIALOG_ID = 0;

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            textTime.setText(new StringBuilder() .append(pad(mHour)).append(":") .append(pad(mMinute)));

            //updateDisplay();
            //updateTime();
        }
    };

    EditText hsEditText;

    MyDBHandler dbHandler;

    /** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;

                    updateDisplay();
                }
            };

    /** Updates the date in the TextView */
    private void updateDisplay() {

        //addButton.setEnabled(true);

        //  boolean invalidDate = false;
        final DateTime dtNow = new DateTime();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioDirection);
        int radioButtonID = rg.getCheckedRadioButtonId();
        View radioButton = rg.findViewById(radioButtonID);
        idx = rg.indexOfChild(radioButton);

        if (idx == 1) {
            textUpDown.setText("Target Date & Time:");
        } else {
            textUpDown.setText("Start Date & Time:");
        }

        pDisplayDate.setTextColor(Color.BLUE);
        textTime.setTextColor(Color.BLUE);
        final LocalDateTime dt = new LocalDateTime(pYear, pMonth + 1, pDay, 0, 0);
        String month = dt.monthOfYear().getAsShortText();
        pDisplayDate.setText(pDay + " " + month  + " " + pYear);

        //System.out.println("!!- "  + mHour + "/" + mMinute);
        //int valDate = validateInDate(dt.toString());
/*
        if (idx == 0 && valDate == 1) { // Count up selected but date is in future
            pDisplayDate.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Future date not allowed", Toast.LENGTH_SHORT).show();
            addButton.setEnabled(false);
        } else {
            if (idx == 1 && valDate != 1) { // Count down selected but date is not in future
                pDisplayDate.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "This must be a future date", Toast.LENGTH_SHORT).show();
                addButton.setEnabled(false);
            }
        }
*/
       // CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxTime);
       // if (checkBox.isChecked()) {
            //cidx = 1;
            textTime.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new TimePickerDialog(EventEditor.this,
                            t,
                            dtNow.getHourOfDay(),
                            dtNow.getMinuteOfHour(),
                            true).show();
                }
                //    Toast.makeText(getApplicationContext(), "Time field clicked", Toast.LENGTH_SHORT).show();
            });
            //if ((tranType.equals("update")) && (useTime == 1))
            if (tranType.equals("update"))
                textTime.setText(pad(mHour) + ":" + pad(mMinute));
            else
                textTime.setText(pad(dtNow.getHourOfDay()) + ":" + pad(dtNow.getMinuteOfHour()));
        //} else {
            //cidx = 0;
            //textTime.setText("");
        //}

    }
/*
    // updates the time we display in the TextView
    private void updateTime() {
        textTime.setText(
                new StringBuilder()
                        //       .append(pad(mHour)).append(":")
                        //      .append(pad(mMinute)));
                        .append(pad(mHour)).append(":")
                        .append(pad(mMinute)));
    }
*/
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
        hsEditText = (EditText) findViewById(R.id.hsEditText);
        textUpDown = (TextView) findViewById(R.id.textViewDirection);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioDirection);
        //CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxTime);
        pDisplayDate = (TextView) findViewById(R.id.inputDate);
        textTime = (TextView) findViewById(R.id.textViewTime);
        //addButton = (Button) findViewById(R.id.buttonAdd);
        tranType = "add";

        dbHandler = new MyDBHandler(this, null, null, 1);

        // If this is an edit there will be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            tranType = "update";
            rowID = bundle.getString("ROW_ID");
            Events myEvent = dbHandler.getMyEvent(rowID);
            hsEditText.setText(myEvent.get_eventname());
            countDirection = myEvent.get_direction();
            //useTime = myEvent.get_evusetime();

            if (countDirection == 1)
                radioGroup.check(R.id.radioButtonCountDown);
            else
                radioGroup.check(R.id.radioButtonCountUp);
            //if (useTime == 1) {
                // cidx = 1;
            //    checkBox.setChecked(true);
            //}

            long millis = myEvent.get_evtime();
            millis *= 1000;
            DateTime dt = new DateTime(millis, DateTimeZone.getDefault());
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");

            pYear = Integer.parseInt(dtf.print(dt).split("-")[0]);
            pMonth = (Integer.parseInt(dtf.print(dt).split("-")[1]) - 1);
            pDay = Integer.parseInt(dtf.print(dt).split("-")[2]);
            mHour = Integer.parseInt(dtf.print(dt).split("-")[3]);
            mMinute = Integer.parseInt(dtf.print(dt).split("-")[4]);
            //addButton.setText("Update");
        } else {
            final Calendar cal = Calendar.getInstance();
            pYear = cal.get(Calendar.YEAR);
            pMonth = cal.get(Calendar.MONTH);
            pDay = cal.get(Calendar.DAY_OF_MONTH);
            //mHour = cal.get(Calendar.HOUR_OF_DAY);
        }

        /** Listener for click event of the date field */
        pDisplayDate.setOnClickListener(new View.OnClickListener() {
            //pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        //final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioDirection);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                updateDisplay();
            }
        });

        //CheckBox chkTime = (CheckBox) findViewById(R.id.checkBoxTime);
        //checkBox.setOnClickListener(new View.OnClickListener() {
            //@Override
          //  public void onClick(View v) {
          //      updateDisplay();
          //  }
        //});
        /** Display the current date in the TextView */
        updateDisplay();

        // BEGIN_INCLUDE (inflate_set_custom_view)
        // Inflate a "Done" custom action bar view to serve as the "Up" affordance.
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                // "Done"
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

        //String dbTime = "00:01"; // 1 minute past midnight
        //CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxTime);
        //if (checkBox.isChecked()) {
           String dbTime = (String) textTime.getText(); // Use time entered by user
        //}

        String givenDateString = pDisplayDate.getText() + " " + dbTime; //e.g. 7 Jul 2014 15:30
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        try {
            Date mDate = sdf.parse(givenDateString);
            //long timeInMillis = mDate.getTime(); // Time entered in millis
            timeInSeconds = (int)(mDate.getTime() / 1000);
            int diffInSecs = validateInDate(timeInSeconds);
            //System.out.println("!!- "  + "diff in secs is " + diffInSecs);
            if (idx == 0 && diffInSecs < 0) { // Count up selected but date is in future
                pDisplayDate.setTextColor(Color.RED);
                textTime.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "Future date not allowed", Toast.LENGTH_SHORT).show();
                //addButton.setEnabled(false);
                return;
            } else {
                if (idx == 1 && diffInSecs > 0) { // Count down selected but date is not in future
                    pDisplayDate.setTextColor(Color.RED);
                    textTime.setTextColor(Color.RED);
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
            Events event = new Events(Integer.parseInt(rowID), EventTitle, idx, timeInSeconds, 0);
            dbHandler.updateEvent(event);
            Toast.makeText(getApplicationContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            Events event = new Events(EventTitle, idx, timeInSeconds, 0);
            dbHandler.addEvent(event);
            Toast.makeText(getApplicationContext(), "Your event has been created", Toast.LENGTH_SHORT).show();
        }
        finish(); // return to previous activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_editor, menu);
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


    /*
        public boolean deleteEvent(String RowID){

            //Toast.makeText(getApplicationContext(), "Delete clicked "  + rowID, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
           // System.out.println("!!- here");
            builder.setTitle("Delete Event?")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Click OK to delete the event")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean result = dbHandler.deleteEvent(rowID);
                            if (result) {
                                Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                                //onPause();; // call onpause so that on onresume can be called to refresh list
                                //onResume();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
    */
   // @Override
   // public void onClick(View view) {

   // }

/*
    @Override
    protected void onPause() {
        super.onPause();
        activityDataOut = hsEditText.getText().toString();
        System.out.println("!!- pausing with " + activityDataOut);

        //onResume();
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        activityDataIn = hsEditText.getText().toString();
        System.out.println("!!- resumed with  " + activityDataIn);
    }

    @Override
    public void onBackPressed() {
        if (! activityDataIn .equals(hsEditText.getText().toString()) ) {
            // Create the fragment and show it as a dialog.
            EventDialog eventDialog = new EventDialog();
            //eventDialog.show(fm, "fragment_edit_name");
            eventDialog.show(getFragmentManager(), "dialog");
        } else {
            // No changes detected
            finish();
        }
    }

    public void doPositiveClick(View v) {
        // Do stuff here.
        //Log.i("FragmentAlertDialog", "Positive click!");
        addButtonClicked(v);
    }
    public void doNegativeClick() {
        // Do stuff here.
        //Log.i("FragmentAlertDialog", "Positive click!");
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }

}

