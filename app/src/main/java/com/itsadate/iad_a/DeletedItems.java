package com.itsadate.iad_a;
// courtesy of http://wptrafficanalyzer.in/blog/implementing-checkall-and-uncheckall-for-a-listview-in-android/

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DeletedItems extends ListActivity
        implements EventDialog.OnDataPass { //Required for interaction between fragment & activity

    public String rowID []; // Array of _id column form database
    MyDBHandler dbHandler;
    ArrayList deletedEvents = new ArrayList();
    String rowidsSelected;
    String opType;

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleted_items);
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

        // load deleted events from database
        loadItems();

        /** Defining array adapter to store items for the listview **/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, deletedEvents);

        /** Setting the arrayadapter for this listview  **/
        getListView().setAdapter(adapter);

        // get a reference to to actionbar's Delete button
        final TextView txtDelete =  ( TextView ) findViewById(R.id.actionbar_delete_button);
        final FrameLayout actionBarDelete =  ( FrameLayout ) findViewById(R.id.actionbar_delete);
        final FrameLayout actionBarRestore =  ( FrameLayout ) findViewById(R.id.actionbar_restore);
        actionBarDelete.setEnabled(false); // start with the Delete button disabled
        actionBarRestore.setEnabled(false); // start with the Restore button disabled

        /** Defining checkbox click event listener **/
        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox chk = (CheckBox) v;
                int itemCount = getListView().getCount();

                for(int i=0 ; i < itemCount ; i++){
                    boolean hs = chk.isChecked();
                    //System.out.println("!!- ischecked = " + hs);
                    getListView().setItemChecked(i, chk.isChecked());
                }
                itemCount = getCheckedItemCount();
                System.out.println("!!- " + "item count = " + itemCount);
                if (itemCount > 0) { //disable Delete button if no items are checked
                    //System.out.println("!!- " + "item count=" + itemCount + "setting true");
                    //actionbar_delete
                    //txtDelete.setEnabled(true);
                    actionBarDelete.setEnabled(true);
                    actionBarRestore.setEnabled(true);
                } else {
                   // System.out.println("!!- " + "item count=" + itemCount + "setting false");
                    //txtDelete.setEnabled(false);
                    actionBarDelete.setEnabled(false);
                    actionBarRestore.setEnabled(false);
                }
            }
        };


        /** Defining click event listener for the listitem checkbox */
        OnItemClickListener itemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                CheckBox chk = (CheckBox) findViewById(R.id.chkAll);
                int checkedItemCount = getCheckedItemCount();
                //System.out.println("!!- " + "Def " + checkedItemCount + " checked");
                if (getListView().getCount() == checkedItemCount) {
                    chk.setChecked(true);
                    //System.out.println("!!- " + "All checked");
                } else {
                    chk.setChecked(false);
                    //System.out.println("!!- " + "Not all checked");
                }
                if (checkedItemCount > 0) {
                    actionBarDelete.setEnabled(true);
                    actionBarRestore.setEnabled(true);
                //    System.out.println("!!- " + "checked count = " + checkedItemCount + "/" + getListView().getCount() + " setting true");
                //    txtDelete.setEnabled(true);
                } else {
                    actionBarDelete.setEnabled(false);
                    actionBarRestore.setEnabled(false);
                //    System.out.println("!!- " + "checked count = " + checkedItemCount + "setting false");
                //    txtDelete.setEnabled(false);
                }
            }
        };

        /** Getting reference to checkbox available in the main.xml layout */
        CheckBox chkAll =  ( CheckBox ) findViewById(R.id.chkAll);

        /** Setting a click listener for the checkbox **/
        chkAll.setOnClickListener(clickListener);

        /** Setting a click listener for the listitem checkbox **/
        getListView().setOnItemClickListener(itemClickListener);

    }

    public void restoreButtonClicked() {

        opType = "R"; //this is a delete operation
        boolean response = setupDialog("Restore ");



       // Toast.makeText(getApplicationContext(), "Restore clicked ", Toast.LENGTH_SHORT).show();

        //}
    }

    public void deleteButtonClicked() {

        opType = "D"; //this is a delete operation
        boolean response = setupDialog("Permanently delete? ");

        //if (EventTitle.isEmpty()) {
       // EventDialog eventDialog = new EventDialog();

        //Bundle bundle = new Bundle();
        //String[] dIDs = rowidsSelected.split(",");
        //bundle.putString("dialogMessage", "Permanently delete " + dIDs.length + " events?");
        //eventDialog.setArguments(bundle);
        //eventDialog.show(fm, "fragment_edit_name");
        //eventDialog.show(getFragmentManager(), "dialog");

        //dbHandler.deleteSpecificEvents(rowidsSelected);
        //finish();
        //startActivity(getIntent());
        //loadItems();

        //Toast.makeText(getApplicationContext(), "Delete clicked ", Toast.LENGTH_SHORT).show();

        //}
    }

    public boolean setupDialog(String message) {

        EventDialog eventDialog = new EventDialog();

        Bundle bundle = new Bundle();
        String[] dIDs = rowidsSelected.split(",");
        bundle.putString("dialogMessage", message + dIDs.length + " events?");
        eventDialog.setArguments(bundle);
        //eventDialog.show(fm, "fragment_edit_name");
        eventDialog.show(getFragmentManager(), "dialog");

        return true;

    }

    @Override
    public void onDataPass(String data) {

        if (data .equals("Yes")) {
            if (opType .equals("D")) {
                dbHandler.deleteSpecificEvents(rowidsSelected);
            } else {
                Toast.makeText(getApplicationContext(), "will restore " + rowidsSelected, Toast.LENGTH_SHORT).show();
                //dbHandler.restoreSpecificEvents(rowidsSelected);
            }
            finish();

        //} else {
        //    finish();
        }
    }

    public void loadItems() {

        deletedEvents.clear();
        String evstring = dbHandler.getActiveEventIDs("I");
       // System.out.println("!!- "  + evstring);
        String[] foods = evstring.split(":");
        rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (i = 0; i < foods.length; i++) {
                //System.out.println("!!- " + i + foods.length);
                Events myEvent = dbHandler.getMyEvent(foods[i]);
                //listDataHeader.add(myEvent.get_eventname());
                //List<String> child = new ArrayList<>();
                deletedEvents.add(myEvent.get_eventname());
                //deletedEvents.add(formatDateTime(myEvent.get_evtime(), myEvent.get_direction()));
                //listDataChild.put(listDataHeader.get(i), child);
                rowID[i] = foods[i]; // store _id from database
            }
        }
    }

     //Returns the number of checked items
    private int getCheckedItemCount(){

        int cnt = 0;
        SparseBooleanArray positions = getListView().getCheckedItemPositions();
        int itemCount = getListView().getCount();
        rowidsSelected = "";
        for(int i=0;i<itemCount;i++){
            if(positions.get(i)) {
                //build up a comma deleited row of id's to be used in the db delete query
                rowidsSelected += rowID[i] + ",";
                cnt++;
            }
        }
        if (cnt > 0)
            rowidsSelected = rowidsSelected.substring(0,rowidsSelected.length()-1); //delete last character from string
        return cnt;
    }
}