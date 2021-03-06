package com.itsadate.iad_a;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EventEditor extends Activity
        implements EventDialog.OnDataPass { //Required for interaction between fragment & activity

    private View myView;
    private TextView textUpDown;
    private Button dateButton;
    private Button timeButton;
    private Uri selectedImage; // Image selected from gallery
    //private Button bgimagebutton;
    private int pYear;
    private int pMonth;
    private int pDay;
    private int mHour;
    private int mMinute;
    private int idx_cd;
    private int idx_dy;
    //private int chk_sec;
    private int timeInSeconds;
    private String tranType;
    private int rowID;
    private String activityDataIn;
    private String activityDataOut;
    String AMPM;

    private RadioButton countUp = null;
    private RadioButton countDown = null;
    private RadioButton daysOnly = null;
    private RadioButton daysAndYears = null;
    private CheckBox includeSec = null;

    private RadioGroup cd;
    private RadioGroup dy;
    ImageView imgView;

    // These two vars are used for BG image select
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private static Bitmap Image = null;

    EditText hsEditText;
    EditText optionalInfo;
    MyDBHandler dbHandler;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap currentImage;
    int imageHeight;
    int imageWidth;
    String imageType;

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
        if (idx_dy == 0) {
            dy.check(R.id.radioButtonDaysOnly);
        } else {
            dy.check(R.id.radioButtonYearsAndDays);
        }

        final LocalDateTime dt = new LocalDateTime(pYear, pMonth + 1, pDay, 0, 0);
        String month = dt.monthOfYear().getAsShortText();
        //pDisplayDate.setText(pDay + " " + month  + " " + pYear);
        dateButton.setText(pDay + " " + month + " " + pYear);
        timeButton.setText(mHour + ":" + pad(mMinute) + " " + AMPM);
        //timeButton.setText(mHour + ":" + (mHour < 12 ? mHour + " AM" : mHour -12 + " PM"));

    }

    private static String pad(int c) {
        if (c >= 10) return String.valueOf(c);
        else return "0" + String.valueOf(c);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)@Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("!!- oncreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_editor);
        myView = findViewById(R.id.viewID); // reference to view to be used in addbuttonpressed method
        hsEditText = (EditText) findViewById(R.id.hsEditText);
        optionalInfo = (EditText) findViewById(R.id.editTextInfo);
        textUpDown = (TextView) findViewById(R.id.textViewDirection);

        dateButton = (Button) findViewById(R.id.buttonCounterDate);
        timeButton = (Button) findViewById(R.id.buttonCounterTime);
        //bgimagebutton = (Button) findViewById(R.id.buttonTimerBGImage);

        cd = (RadioGroup) findViewById(R.id.radioDirection); // Count up/down
        dy = (RadioGroup) findViewById(R.id.radioYearsDays); // Days/Days+years

        countUp = (RadioButton) findViewById(R.id.radioButtonCountUp);
        countDown = (RadioButton) findViewById(R.id.radioButtonCountDown);
        daysOnly = (RadioButton) findViewById(R.id.radioButtonDaysOnly);
        daysAndYears = (RadioButton) findViewById(R.id.radioButtonYearsAndDays);
        //includeHrs = (CheckBox) findViewById(R.id.checkBoxHours);
        //includeMin = (CheckBox) findViewById(R.id.checkBoxMins);
        includeSec = (CheckBox) findViewById(R.id.checkBoxSecs);

        tranType = "add";
        //tranType = "update";
        String formattedDate;
        //Log.d("trantype", tranType);
        dbHandler = new MyDBHandler(this, null, null, 1);
        //System.out.println("!!- trantype is " + tranType);
        // If this is an edit there will be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tranType = "update";
            rowID = bundle.getInt("ROW_ID");
            Events myEvent = dbHandler.getMyEvent(rowID);
            hsEditText.setText(myEvent.get_eventname());
            optionalInfo.setText(myEvent.get_eventinfo());
            idx_cd = myEvent.get_direction();
            idx_dy = myEvent.get_dayyears();
            //boolean secsCheckedBool = (myEvent.get_incsec() != 0);
            includeSec.setChecked(myEvent.get_incsec() != 0); // false if 0 otherwise true
            //chk_sec = myEvent.get_incsec();
            //System.out.println("!!- "  + "days & years from db is " + myEvent.get_dayyears());
            long millis = myEvent.get_evtime();
            millis *= 1000;

            formattedDate = formatMyDate(millis);
            //System.out.println("!!- " + formattedDate);

            //DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm");
            pYear = Integer.parseInt(formattedDate.split("-")[0]);
            pMonth = (Integer.parseInt(formattedDate.split("-")[1]) - 1);
            pDay = Integer.parseInt(formattedDate.split("-")[2]);
            mHour = Integer.parseInt(formattedDate.split("-")[3]);
            mMinute = Integer.parseInt(formattedDate.split("-")[4]);
            AMPM = formattedDate.split("-")[5];
            //imgDecodableString = myEvent.get_bgimage();
        } else {
            //long millis =
            formattedDate = formatMyDate(System.currentTimeMillis());
            //System.out.println("!!- " + formattedDate);
            pYear = Integer.parseInt(formattedDate.split("-")[0]);
            pMonth = (Integer.parseInt(formattedDate.split("-")[1]) - 1);
            pDay = Integer.parseInt(formattedDate.split("-")[2]);
            mHour = Integer.parseInt(formattedDate.split("-")[3]);
            mMinute = Integer.parseInt(formattedDate.split("-")[4]);
            AMPM = formattedDate.split("-")[5];

            idx_cd = 1;
        }

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
        cd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idx_cd = countDown.isChecked() ? 1 : 0; // convert direction button to int
                //Toast.makeText(getApplicationContext(), "button is "  + countUp.isChecked(), Toast.LENGTH_SHORT).show();
                updateDisplay();
            }
        });
        dy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group2, int checkedId2) {
                idx_dy = daysAndYears.isChecked() ? 1 : 0; // convert days/years button to int
                updateDisplay();
            }
        });

        // Inflate a "Done" custom action bar view to serve as the "Up" affordance.
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_cust_event_editor, null);

        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {@Override
                                            public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                }
                });
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {@Override
                                            public void onClick(View v) {
                    try {
                        addButtonClicked(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);

        imgView = (ImageView) findViewById(R.id.imgView);
        //if(event has a background image) {
            //imgView.setImageBitmap(
                    //decodeSampledBitmapFromFile(imgDecodableString, 100, 100));
        //}

        activityDataIn = collateActivityInfo();
    }

    public String formatMyDate(long millisIn) {

        DateTime dt = new DateTime(millisIn, DateTimeZone.getDefault());
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-hh-mm-a");

        return dtf.print(dt);
    }

    public void addButtonClicked(View view) throws IOException {

        String EventTitle = hsEditText.getText().toString();
        String EventInfo = optionalInfo.getText().toString();
        if (EventTitle.isEmpty()) {
            Toast mytoast = Toast.makeText(getApplicationContext(), "Please  title your event", Toast.LENGTH_SHORT);
            //        Toast.makeText(getApplicationContext(), "Please  title your event", Toast.LENGTH_SHORT).show();
            mytoast.setGravity(Gravity.CENTER, 0, 0);
            mytoast.show();
            return;
        }

        String givenDateString = dateButton.getText() + " " + timeButton.getText(); //e.g. 7 Jul 2014 15:30
        //System.out.println("!!- "  + "date & time=" + givenDateString);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());
        try {
            Date mDate = sdf.parse(givenDateString);
            //long timeInMillis = mDate.getTime(); // Time entered in millis
            timeInSeconds = (int)(mDate.getTime() / 1000);
            int diffInSecs = validateInDate(timeInSeconds);
            //System.out.println("!!- "  + "time in secs is  " + timeInSeconds);
            //if (idx_cd == 0 && diffInSecs < 0) { // Count up selected but date is in future
            //pDisplayDate.setTextColor(Color.RED);
            //                textTime.setTextColor(Color.RED);
            //Toast.makeText(getApplicationContext(), "Counter will start " + givenDateString, Toast.LENGTH_LONG).show();
            //addButton.setEnabled(false);
            //return;
            //} else {
            if (idx_cd == 1 && diffInSecs > 0) { // Count down selected but date is not in future
                //pDisplayDate.setTextColor(Color.RED);
                //                    textTime.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "This must be a future date", Toast.LENGTH_SHORT).show();
                //addButton.setEnabled(false);
                return;
            }
            //}
        } catch (ParseException e) {
            //System.out.println("!!- " + pDisplayDate.getText() + dbTime + "bad!");
            e.printStackTrace();
        }
        //Events event = new Events(EventTitle, idx, timeInSeconds, cidx);
        //int rowsInDB = dbHandler.getRowCount()
        //System.out.println("!!- "  + timeInSeconds);

        if(selectedImage != null) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            byte[] data = getBitmapAsByteArray(bitmap);
            //System.out.println("!!- " + data.length);
        }
        //System.out.println("!!- trantpe is " + tranType);

        if (tranType.equals("update")) {
            Events event = new Events(rowID, EventTitle, EventInfo, idx_cd, timeInSeconds, "A",
                    //includeHrs.isChecked() ? 1 : 0,
                    "R",
                    //includeMin.isChecked() ? 1 : 0,
                    0,
                    includeSec.isChecked() ? 1 : 0,
                    idx_dy,
                    imgDecodableString, // path to background image if one is selected
                    "[1,2,3]"
            );
            //System.out.println("!!- "  + "sending  " + (daysOnly.isChecked() ? 1 : 0));

            dbHandler.updateEvent(event);
            //Toast.makeText(getApplicationContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            Events event = new Events(EventTitle, EventInfo, idx_cd, timeInSeconds, "A",
                    //includeHrs.isChecked() ? 1 : 0,
                    "R",
                    //includeMin.isChecked() ? 1 : 0,
                    0,
                    includeSec.isChecked() ? 1 : 0,
                    idx_dy,
                    imgDecodableString, // path to background image if one is selected
                    "[2,3,4]"
            );
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

        //activityDataIn = collateActivityInfo();
    }

    @Override
    public void onBackPressed() {

        activityDataOut = collateActivityInfo();

        if (!activityDataIn.equals(activityDataOut)) {
            // Create the fragment and show it as a dialog.
            EventDialog eventDialog = new EventDialog();
            Bundle bundle = new Bundle();
            bundle.putString("dialogMessage", "Save changes before exit?");
            bundle.putString("buttonPos", "Yes");
            bundle.putString("buttonNeg", "No");
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

        // User tried to exit with unsaved changes
        if (data.equals("Yes")) {
            try {
                addButtonClicked(myView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
        //Toast.makeText(getApplicationContext(), "Edit activity - "  + data, Toast.LENGTH_SHORT).show();
        //Log.d("LOG","hello " + data);
    }

    // Concatenate values into a string for comparison
    public String collateActivityInfo() {
       // System.out.println("!!- " + imgDecodableString);
        return hsEditText.getText().toString() + optionalInfo.getText().toString() +
                countUp.isChecked() + dateButton.getText() + timeButton.getText() +
                daysOnly.isChecked() + includeSec.isChecked() + imgDecodableString;
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

        if (AMPM.equals("PM")) {
            mHour += 12;
        }
        DatePickerFragment dialog = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_id", 2); // 2=Time Picker
        bundle.putInt("hour", mHour);
        bundle.putInt("minute", mMinute);
        dialog.setArguments(bundle);
        dialog.setCallBack(ontime);
        dialog.show(getFragmentManager(), "dialog");
    }

    OnDateSetListener ondate = new OnDateSetListener() {@Override
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
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (mHour < 12) {
            AMPM = "AM";
        } else if (mHour == 12) {
            AMPM = "PM";
        } else {
            AMPM = "PM";
            mHour -= 12;
        }

        updateDisplay();
        }
    };

    public void loadImagefromGallery(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("!!- " + "RESULT_OK "  + RESULT_OK);
        selectedImage = null;
        if(resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.getData();
            //System.out.println("!!- selectedimage=" + selectedImage);

            // from http://square.github.io/picasso/
            Picasso.with(this).load(selectedImage).resize(100, 100).error(R.drawable.courteney).into(imgView);
        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
/*
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
*/
}