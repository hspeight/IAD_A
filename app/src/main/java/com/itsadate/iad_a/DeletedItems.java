package com.itsadate.iad_a;


import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DeletedItems extends Activity
        implements EventDialog.OnDataPass {

    public String rowID []; // Array of _id column form database
    String opType;
    String rowidsSelected;
    MyDBHandler dbHandler;
    ArrayList deletedEvents = new ArrayList();

    private ListView mainListView;
    //private mItems[] itemss;
    private ArrayAdapter<mItems> listAdapter;
    ArrayList<String> checked = new ArrayList<String>();
    ArrayList<mItems> planetList = new ArrayList<mItems>();

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arch_listview);

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

        // Find the ListView resource.
        mainListView = (ListView) findViewById(R.id.mainListView);
        //mainListView.setBackgroundColor(Color.MAGENTA);

        // get a reference to to actionbar's Delete button
        final TextView txtDelete =  ( TextView ) findViewById(R.id.actionbar_delete_button);
        final FrameLayout actionBarDelete =  ( FrameLayout ) findViewById(R.id.actionbar_delete);
        final FrameLayout actionBarRestore =  (FrameLayout) findViewById(R.id.actionbar_restore);
        actionBarDelete.setEnabled(false); // start with the Delete button disabled
        actionBarRestore.setEnabled(false); // start with the Restore button disabled

        // When item is tapped, toggle checked properties of CheckBox and
        // Planet.
        mainListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item,
                                            int position, long id) {
                        mItems planet = listAdapter.getItem(position);
                        planet.toggleChecked();
                        SelectViewHolder viewHolder = (SelectViewHolder) item
                                .getTag();
                        viewHolder.getCheckBox().setChecked(planet.isChecked());
                        int rowsChecked = getCheckedItemCount();
                        if (rowsChecked > 0) {
                            actionBarDelete.setEnabled(true);
                            actionBarRestore.setEnabled(true);
                        } else {
                            actionBarDelete.setEnabled(false);
                            actionBarRestore.setEnabled(false);
                        }
                        //Toast.makeText(getApplicationContext(), "checked " + getCheckedItemCount(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Create and populate planets.
        //itemss = (mItems[]) getLastNonConfigurationInstance();

        ArrayList<mItems> planetList;
/*
        planetList.add(new mItems("DJ-Android"));
        planetList.add(new mItems("Android"));
        planetList.add(new mItems("iPhone"));
        planetList.add(new mItems("BlackBerry"));
        planetList.add(new mItems("Java"));
        planetList.add(new mItems("PHP"));
        planetList.add(new mItems(".Net"));
*/
        planetList = loadItems();
        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new SelectArralAdapter(this, planetList);
        mainListView.setAdapter(listAdapter);

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

    public ArrayList<mItems> loadItems() {


        deletedEvents.clear();
        String evstring = dbHandler.getActiveEventIDs("I");
        // System.out.println("!!- "  + evstring);
        String[] foods = evstring.split(":");
        rowID = new String[foods.length]; // set number of array elements equal to number of events returned
        int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (i = 0; i < foods.length; i++) {
                //System.out.println("!!- " + i + foods.length);
                Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
                planetList.add(new mItems(myEvent.get_eventname(),formatDateTime(myEvent.get_evtime(), myEvent.get_direction()),false)); // <-- clue
                //System.out.println("!!- " + mItems(i));
                        //listDataHeader.add(myEvent.get_eventname());
                //List<String> child = new ArrayList<>();
                //deletedEvents.add(myEvent.get_eventname() + "\n" +
                //        formatDateTime(myEvent.get_evtime(), myEvent.get_direction()));
                //String s = formatDateTime(myEvent.get_evtime(), myEvent.get_direction());
                //System.out.println("!!- s=" + (s));
                //System.out.println("!!- " + myEvent.get_evtime() + "/" + myEvent.get_direction());
                //deletedEvents.add(formatDateTime(myEvent.get_evtime(), myEvent.get_direction()));
                //listDataChild.put(listDataHeader.get(i), child);
                rowID[i] = foods[i]; // store _id from database
            }
        }
        return planetList;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, Menu.NONE, "Products");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:

                for (int i = 0; i < checked.size(); i++) {
                    Log.d("pos : ", "" + checked.get(i));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Holds planet data. */
    private static class mItems {
        private String name = "";
        private String time = "";
        private boolean checked = false;

        public mItems() {
        }

        public mItems(String name) {
            this.name = name;
        }
        public mItems(String name, String time) {
            this.name = name;
            this.time = time;
        }

        public mItems(String name, String time, boolean checked) {
            this.name = name;
            this.time = time;
            this.checked = checked;
        }

        public String getName() {
            return name;
        }
        public String getTime() {
            return time;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String toString() {
            return name;
        }

        public void toggleChecked() {
            checked = !checked;
        }
    }

    /** Holds child views for one row. */
    private static class SelectViewHolder {
        private CheckBox checkBox;
        private TextView textView;
        private TextView textView2;

        public SelectViewHolder() {
        }

        public SelectViewHolder(TextView textView, TextView textView2, CheckBox checkBox) {
            this.checkBox = checkBox;
            this.textView = textView;
            this.textView2 = textView2;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getTextView2() {
            return textView2;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    /** Custom adapter for displaying an array of Planet objects. */
    private static class SelectArralAdapter extends ArrayAdapter<mItems> {
        private LayoutInflater inflater;

        public SelectArralAdapter(Context context, List<mItems> planetList) {
            super(context, R.layout.arch_item, R.id.rowTextView, planetList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Planet to display
            mItems planet = (mItems) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox;
            TextView textView;
            TextView textView2;

            // Create a new row view
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.arch_item, null);
                RelativeLayout singleRow = (RelativeLayout) convertView.findViewById(R.id.relLayout);

                // Find the child views.
                textView = (TextView) convertView
                        .findViewById(R.id.rowTextView);
                textView2 = (TextView) convertView
                        .findViewById(R.id.rowTextView2);
                checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

                //singleRow.setBackgroundColor(Color.BLACK);
                //textView.setTextColor(Color.YELLOW);
                //textView2.setTextColor(Color.WHITE);

                // Optimization: Tag the row with it's child views, so we don't
                // have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new SelectViewHolder(textView,textView2, checkBox));
                // If CheckBox is toggled, update the planet it is tagged with.
/*
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        mItems planet = (mItems) cb.getTag();
                        planet.setChecked(cb.isChecked());
                        //int rowsChecked = 0;
                        //for (int i = 0; i < 3; i++) {
                        //    //if(planet.setChecked(cb.isChecked())) {
                            //    rowsChecked += 1;
                        //        System.out.println("!!- "  + i + "$" + cb.isChecked() + "!" );
                            //}// converts boolean to o or 1
                        //}
                        //System.out.println("!!- "  + rowsChecked + " checked");
                        // System.out.println("!!- "  +  viewHolder.getCheckBox().isChecked());
                    }
                });
*/
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call
                // findViewById().
                SelectViewHolder viewHolder = (SelectViewHolder) convertView
                        .getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
                textView2 = viewHolder.getTextView2();
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(planet);
            // Display planet data
            checkBox.setChecked(planet.isChecked());
            textView.setText(planet.getName());
            textView2.setText(planet.getTime());
            return convertView;
        }
    }
    //Returns the number of checked items
    private int getCheckedItemCount() {
        int rowsChecked = 0;
        rowidsSelected = "";
        for (int i = 0; i < listAdapter.getCount(); i++) {
            if (planetList.get(i).isChecked()) {
                rowsChecked++;
                rowidsSelected += rowID[i] + ",";
            }
        }

        if (rowsChecked > 0)
            rowidsSelected = rowidsSelected.substring(0,rowidsSelected.length()-1); //delete last character from string
        //System.out.println("!!- " + rowidsSelected);
        return rowsChecked;
    }
/*
    public Object onRetainNonConfigurationInstance() {
        return itemss;
    }
*/
}