package com.itsadate.iad_a;

//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.drawable.PaintDrawable;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

//import android.preference.DialogPreference;
//import android.util.AttributeSet;
//import java.util.ArrayList;
//import java.util.Date;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;

public class ColorPickerPreference2 extends Activity  {

    //RelativeLayout relLayout;
    View linLayout;
    TextView ty,td,th,tm,ts,title,info; // Years,days,hours,mins,secs, event title & info
    //ListView loremlist;
    RadioGroup lc; // List colors
    //TextView listViewText;
    //Button btnSimplePref;
    //Button btnFancyPref;
    //TextView txtCaption1;
    //Boolean fancyPrefChosen = false;
    //View  myLayout1Vertical;
    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.btnPrefC1,
            R.id.btnPrefC2,
            R.id.btnPrefC3,
            R.id.btnPrefC4,
            R.id.btnPrefC21,
            R.id.btnPrefC22,
            R.id.btnPrefC23,
            R.id.btnPrefC24 };
    private static final int[] THEME_COLORS = {
            -16777216,      // Black
            -16711936,      // Green
            -16776961,      // Blue
            -16711681,      // Cyan
            -256,           // Yellow
            -65281,         // Magenta
            -65536,         // Red
            -1,             // White
    };
    //String[] listContent = {
    //        "25th Wedding Anniversary",
    //        "School's out for summer",
    //        "Since I quit smoking",
    //        "Happy birthday to you"
    //};

    private Button[] butt = new Button[BUTTON_IDS.length];

    //final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_002";
    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;
    // obtain an editor to add data to my SharedPreferences object
    SharedPreferences.Editor myEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_timer);

        //myLayout1Vertical = (View)findViewById(R.id.linLayout1Vertical);
        //relLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        linLayout = findViewById(R.id.linearLayoutBG);
        //loremlist = (ListView) findViewById(R.id.listViewHS); // Used to show sample
        lc = (RadioGroup) findViewById(R.id.timerColors); // BG,Row,Text

        // create a reference to the shared preferences object
        mySharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        // obtain an editor to add data to (my)SharedPreferences object
        myEditor = mySharedPreferences.edit();

        for (int i = 0; i < BUTTON_IDS.length; i++) {

            final int b = i;
            butt[b] = (Button) findViewById(BUTTON_IDS[b]); // Fetch the view id from array
            butt[b].setBackgroundColor(THEME_COLORS[b]);
            butt[b].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPreferences(b);
                }
            });
        }

        //LinearLayout linCounters = (LinearLayout) findViewById(R.id.linLayoutCounters);
        ty = (TextView) findViewById(R.id.textYears);
        td = (TextView) findViewById(R.id.textDays);
        th = (TextView) findViewById(R.id.textHour);
        tm = (TextView) findViewById(R.id.textMins);
        ts = (TextView) findViewById(R.id.textSecs);
        title = (TextView) findViewById(R.id.textEvTitle);
        info = (TextView) findViewById(R.id.textInfo);

        setPreferences(9); // Start with a value that wont match
    }

    // public void onClick(View v) {
    public void setPreferences(int b) {

        int buttonIndex = b;

        // extract the  pairs, use default param for missing data
        int backColor = mySharedPreferences.getInt("timerBgColor",Color.WHITE);
        int counterColor = mySharedPreferences.getInt("timerCounterColor",Color.GRAY);
        int textColor = mySharedPreferences.getInt("timerTextColor",Color.WHITE);

        // Which option is selected?
        if( b < 9) { // Entered from oncreate
            int radioButtonID = lc.getCheckedRadioButtonId();
            View radioButton = lc.findViewById(radioButtonID);
            buttonIndex = lc.indexOfChild(radioButton);
        //} else {
        //    buttonIndex = 1;
        }
        //System.out.println("!!- " + b);
        // clear all previous selections
        myEditor.clear();

        switch (buttonIndex) {
            case 0:
                backColor = THEME_COLORS[b];
                break;
            case 1:
                counterColor = THEME_COLORS[b];
                break;
            case 2:
                textColor = THEME_COLORS[b];
                break;
            default:
                //myEditor.putInt("listBgColor",Color.WHITE);
                break;
        }

        //Toast.makeText(getApplicationContext(), "" + v.getId(), Toast.LENGTH_SHORT).show();
        // what button has been clicked?
        //if (v.getId() == btnSimplePref.getId()) {
        // myEditor.putInt("listBgColor", Color.MAGENTA);// black background
        //    myEditor.putInt("listTextColor", Color.YELLOW);// black background
        //myEditor.putInt("textSize", 12); 		  // humble small font
        //} else { // case btnFancyPref
        //    myEditor.putInt("backColor", Color.BLUE); // fancy blue
        //    myEditor.putInt("textSize", 20); 		  // fancy big
        //    myEditor.putString("textStyle", "bold");  // fancy bold
        //    myEditor.putInt("layoutColor", Color.GREEN);//fancy green
        //}
        //int backColor = mySharedPreferences.getInt("listBgColor",Color.BLACK);
        myEditor.putInt("timerBgColor",backColor);
        myEditor.putInt("timerTextColor",textColor);
        myEditor.putInt("timerCounterColor",counterColor);
        myEditor.commit();

        refreshList(backColor, counterColor, textColor);

    }

    public void refreshList(final int backColor, final int counterColor, final int textColor) {

        linLayout.setBackgroundColor(backColor);

        title.setTextColor(textColor);
        info.setTextColor(textColor);

        ty.setTextColor(counterColor);
        td.setTextColor(counterColor);
        th.setTextColor(counterColor);
        tm.setTextColor(counterColor);
        ts.setTextColor(counterColor);
    }

}

