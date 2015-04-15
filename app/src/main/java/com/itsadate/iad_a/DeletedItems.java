package com.itsadate.iad_a;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;

import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import android.widget.ListView;

import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DeletedItems extends Activity
        implements EventDialog.OnDataPass {

    private ListView mainListView;
    private View linLayout;
    private MyDBHandler dbHandler;
    private String rowID []; // Array of _id column form database
    private String rowidsSelected;
    private String opType;
    //private int rowColor, textColor;
    private static final String MyPREFERENCES = "MyPreferences_001"; // archived items pref file
    private ArrayList<ArchItem> architems = new ArrayList<>();
    private ArrayAdapter<ArchItem> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arch_listview);
        //System.out.println("!!- " + "here");
        dbHandler = new MyDBHandler(this, null, null, 1);

        // Get instance to shared pref class
        SharedPreferences pref = getSharedPreferences (MyPREFERENCES, MODE_PRIVATE);
        int bgColor = pref.getInt("listBgColor", -16776961); // Get background color from pref file
        //int archRowColor = pref.getInt("listRowColor", -16776961); // Get row color from pref file
        //textColor = pref.getInt("listTextColor", -1); // Get text color from pref file

        //System.out.println("!!- " + bgColor);

        dbHandler = new MyDBHandler(this, null, null, 1);

        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_cust_deleted_events, null);

        customActionBarView.findViewById(R.id.actionbar_delete).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //deleteButtonClicked(v);
                        deleteButtonClicked();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_restore).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //restoreButtonClicked(v);
                        restoreButtonClicked();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);

        linLayout = findViewById(R.id.linLayoutArchBG);
        linLayout.setBackgroundColor(bgColor);
        // Find the ListView resource.
        mainListView = (ListView) findViewById(R.id.mainListView);
        TextView titletext = (TextView) findViewById(R.id.textViewNumArchived);

        // get a reference to to actionbar's Delete button
        final TextView txtDelete =  ( TextView ) findViewById(R.id.actionbar_delete_button);
        final FrameLayout actionBarDelete = ( FrameLayout ) findViewById(R.id.actionbar_delete);
        final FrameLayout actionBarRestore = (FrameLayout) findViewById(R.id.actionbar_restore);
        final CheckBox chkAll = (CheckBox) findViewById(R.id.checkAll);

        actionBarDelete.setEnabled(false); // start with the Delete button disabled
        actionBarRestore.setEnabled(false); // start with the Restore button disabled

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new DeletedItemsAdapter(this, architems);
        mainListView.setAdapter(listAdapter);

        // When item is tapped, toggle checked properties of CheckBox & archived event
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item,
                                            int position, long id) {

                        listitemClicked(position, item, false);

                        int rowsChecked = getCheckedItemCount();
                        //System.out.println("!!- " + rowsChecked + " checked");
                        if (rowsChecked > 0) {
                            actionBarDelete.setEnabled(true);
                            actionBarRestore.setEnabled(true);
                        } else {
                            actionBarDelete.setEnabled(false);
                            actionBarRestore.setEnabled(false);
                        }

                        //final int totalRows = mainListView.getChildCount();
                        final int totalRows = listAdapter.getCount();
                        //System.out.println("!!- " + " total rows =" +totalRows);
                        if (rowsChecked == totalRows) {
                            //System.out.println("!!- " + " here");
                            chkAll.setChecked(true);
                        } else {
                            chkAll.setChecked(false);
                        }
                        //listAdapter.notifyDataSetChanged();
                        //Toast.makeText(getApplicationContext(), "checked " + getCheckedItemCount(), Toast.LENGTH_SHORT).show();
                    }
                });

        chkAll.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                //is chkAll checked?
                    //System.out.println("!!- " + " items=" + listAdapter.getCount());
                    //SelectViewHolder viewHolder = new SelectViewHolder;
                if (((CheckBox) v).isChecked() && listAdapter.getCount() > 0) {
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        View v2 = mainListView.getAdapter().getView(i, null, null);
                        listitemClicked(i, v2, true);
                    }
                    int rowsChecked = getCheckedItemCount(); // dont comment out
                    actionBarDelete.setEnabled(true);
                    actionBarRestore.setEnabled(true);
                }
                if (!((CheckBox) v).isChecked()) {
                    //for (int i=0; i < mainListView.getChildCount(); i++) {
                    for (int i=0; i < listAdapter.getCount(); i++) {
                        architems.get(i).setChecked(false);
                    }
                    actionBarDelete.setEnabled(false);
                    actionBarRestore.setEnabled(false);
                }
                listAdapter.notifyDataSetChanged(); // update the view with the checked/unchecked items
                //int rowsChecked = getCheckedItemCount();
            }
        });

        //ArrayList<ArchItem> architem;
        architems = loadItems();

        //int numarch = listAdapter.getCount();
        titletext.setText(listAdapter.getCount() + " Event(s) archived");

    }

    public void listitemClicked(int position, View item, boolean checkAll) {

        //System.out.println("!!- "  + item);
        ArchItem architem = listAdapter.getItem(position);
        if (checkAll)
            architem.setChecked(true); // select all is checked
        else
            architem.toggleChecked();
        SelectViewHolder viewHolder = (SelectViewHolder) item.getTag();
        //System.out.println("!!- "  + item.getTag());
        viewHolder.getCheckBox().setChecked(architem.isChecked());

    }

    public ArrayList<ArchItem> loadItems() {

        //deletedEvents.clear();
        String evstring = dbHandler.getActiveEventIDs("I");
        // System.out.println("!!- "  + evstring);
        String[] foods = evstring.split(":");
        rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        //int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (int i = 0; i < foods.length; i++) {
                //System.out.println("!!- " + i + foods.length);
                Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
                //architems.add(new ArchItem(myEvent.get_eventname(),formatDateTime(myEvent.get_evtime(), myEvent.get_direction()),false)); // <-- clue
                architems.add(new ArchItem(myEvent.get_eventname(),formatDateTime(myEvent.get_evtime(), myEvent.get_direction()),false)); // <-- clue
                rowID[i] = foods[i]; // store _id from database
            }
        }
        return architems;
    }

    public String formatDateTime(int eventTime,int direction){
        String[] d = new String[] {"up from ","down to "};
        long millis = eventTime;
        millis *= 1000;
        DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        //System.out.println("!!- " + dtf.print(dt));
        return "Count " + d[direction] + " " + dtf.print(dt);

    }

    public void restoreButtonClicked() {

        opType = "R"; //this is a delete operation
        boolean response = setupDialog("Restore ");

        //Toast.makeText(getApplicationContext(), "Restore clicked ", Toast.LENGTH_SHORT).show();
    }

    public void deleteButtonClicked() {

        opType = "D"; //this is a delete operation
        boolean response = setupDialog("Permanently delete? ");
        //Toast.makeText(getApplicationContext(), "Delete clicked ", Toast.LENGTH_SHORT).show();

    }

    public boolean setupDialog(String message) {
        //
        EventDialog eventDialog = new EventDialog();

        Bundle bundle = new Bundle();
        //System.out.println("!!- " + " rows selected=" + rowidsSelected);
        String[] dIDs = rowidsSelected.split(",");
        bundle.putString("dialogMessage", message + dIDs.length + " events?");
        eventDialog.setArguments(bundle);
        //eventDialog.show(fm, "fragment_edit_name");
        eventDialog.show(getFragmentManager(), "dialog");

        return true;
    }

    @Override
    public void onDataPass(String data) {

        if (data .equals("Yes")) { // Yes button was clicked in Alertdialog
            if (opType .equals("D")) {
                dbHandler.deleteSpecificEvents(rowidsSelected);
            } else {
                //Toast.makeText(getApplicationContext(), "will restore " + rowidsSelected, Toast.LENGTH_SHORT).show();
                dbHandler.restoreSpecificEvents(rowidsSelected);
            }
            finish();
        }
    }

    //Returns the number of checked items
    private int getCheckedItemCount() {
        int rowsChecked = 0;
        rowidsSelected = "";
        for (int i = 0; i < listAdapter.getCount(); i++) { // should this be architems.size() ?
            if (architems.get(i).isChecked()) {
                rowsChecked++;
                rowidsSelected += rowID[i] + ",";
            }
        }

        if (rowsChecked > 0)
            rowidsSelected = rowidsSelected.substring(0,rowidsSelected.length()-1); //delete last character from string
        //System.out.println("!!- " + rowidsSelected);
        return rowsChecked;
    }

}