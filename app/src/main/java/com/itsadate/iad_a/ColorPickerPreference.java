package com.itsadate.iad_a;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
//import android.widget.Toast;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.widget.TextView;
//import android.widget.Toast;

public class ColorPickerPreference extends Activity  {

    RelativeLayout relLayout;
    View linLayout;
    ListView loremlist;
    RadioGroup lc; // List colors
    TextView listViewText;
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
    String[] listContent = {
            "25th Wedding Anniversary",
            "Count down to new year's eve",
            "Since I quit smoking",
            "Happy birthday to you",
            "Counting down to NYE",
            "Vacation starts in .."
    };

    //int[] rainbow = context.getResources().getIntArray(R.array.listTextColors);
    //int[] listtxtcolor = {getResources().getColor(android.R.color.holo_blue_bright)};

    private Button[] butt = new Button[BUTTON_IDS.length];

    //final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_001";
    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;
    // obtain an editor to add data to my SharedPreferences object
    SharedPreferences.Editor myEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs);

        //myLayout1Vertical = (View)findViewById(R.id.linLayout1Vertical);
        //relLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        linLayout = findViewById(R.id.linearLayoutBG);
        loremlist = (ListView) findViewById(R.id.listViewHS); // Used to show sample
        lc = (RadioGroup) findViewById(R.id.eventListColors); // BG,Row,Text

        refreshList(-16711936);

        // create a reference to the shared preferences object
        //mySharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        // obtain an editor to add data to (my)SharedPreferences object
        //myEditor = mySharedPreferences.edit();
        // has a Preferences file been already created?
        /*
        if (mySharedPreferences != null
                && mySharedPreferences.contains("listBgColor")) {
            // object and key found, show all saved values
            //applySavedPreferences();
        } else {
            Toast.makeText(getApplicationContext(),
                    "No Preferences found", Toast.LENGTH_SHORT).show();
        }
        */
/*
        for (int i = 0; i < BUTTON_IDS.length; i++) {

            final int b = i;
            butt[b] = (Button) findViewById(BUTTON_IDS[b]); // Fetch the view id from array
            butt[b].setBackgroundColor(THEME_COLORS[b]);
            butt[b].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPreferences(b);
                    //System.out.println("!!- " + "!!!!!!!!!!!!!!!!!!!!!!!!!");
                    //Intent myIntent = new Intent(Start.this, MainActivity.class);
                    //myIntent.putExtra("players", b);
                    //startActivity(myIntent);

                    //startActivity(new Intent(Start.this, MainActivity.class));
                }
            });
        }

        setPreferences(9); // Start with a value that wont match
*/
    }

    // public void onClick(View v) {
    /*
    public void setPreferences(int b) {

        int buttonIndex = b;

        // extract the  pairs, use default param for missing data
        int backColor = mySharedPreferences.getInt("listBgColor",Color.WHITE);
        int textColor = mySharedPreferences.getInt("listTextColor",Color.WHITE);
        int rowColor = mySharedPreferences.getInt("listRowColor",Color.GRAY);
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
                //myEditor.putInt("listRowColor",THEME_COLORS[b]);
                rowColor = THEME_COLORS[b];
                break;
            case 2:
                //TextView textView = (TextView) rowView.findViewById(R.id.rowListTextView);
                //listViewText = (TextView) v.findViewById(android.R.id.text1);
                //listViewText.setTextColor(THEME_COLORS[b]);
                textColor = THEME_COLORS[b];
                //setTextColor(THEME_COLORS[b]);
                //myEditor.putInt("listTextColor",THEME_COLORS[b]);

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
        myEditor.putInt("listBgColor",backColor);
        myEditor.putInt("listTextColor",textColor);
        myEditor.putInt("listRowColor",rowColor);
        myEditor.commit();

        refreshList(backColor, rowColor, textColor);
       // onPause(); // call onpause so that on onresume can be called to refresh list
       // onResume();
        //applySavedPreferences(backColor, -256, textColor); // Background, row & text
    }

*/
    //public void refreshList(final int backColor, final int rowColor, final int textColor) {
    public void refreshList(final int backColor) {

        linLayout.setBackgroundColor(backColor);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, listContent) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                listViewText = (TextView) view.findViewById(android.R.id.text1);
                listViewText.setTextColor(THEME_COLORS[5]);
                listViewText.setBackgroundColor(THEME_COLORS[1]);
                return view;
            }
        };
    // setting list adapter
        loremlist.setAdapter(adapter);
    }

}

