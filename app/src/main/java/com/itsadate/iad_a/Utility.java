package com.itsadate.iad_a;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class Utility extends Activity {

   // private Button insertButton;
    //private Button clearButton;

    MyDBHandler dbHandler;

    ArrayList<String> dummydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        //insertButton = (Button) findViewById(R.id.buttonInsert);
        //clearButton = (Button) findViewById(R.id.buttonClear);

        dbHandler = new MyDBHandler(this, null, null, 1);

    }

    public void insertButtonClicked(View view) {

        //dummydata = new ArrayList<String>();

        Events event = new Events("Title 1 from updater", 1, 1423361834, 1);
        dbHandler.addEvent(event);

    }

    public void clearButtonClicked(View view) {

        Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_utility, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
