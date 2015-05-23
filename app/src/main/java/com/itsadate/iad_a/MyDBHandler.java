package com.itsadate.iad_a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 27;
    private static final String DATABASE_NAME = "events.db";
    public static final  String TABLE_EVENTS = "events";
    public static final  String COLUMN_ID = "_id";
    public static final  String COLUMN_EVENT_NAME = "eventname";
    public static final  String COLUMN_EVENT_INFO = "eventinfo";
    public static final  String COLUMN_EVENT_DIR = "direction";
    public static final  String COLUMN_EVENT_TIME = "evtime";
    public static final  String COLUMN_EVENT_STATUS = "evstatus"; // (A)ctive/(I)nactive
    public static final  String COLUMN_EVENT_TYPE = "evtype"; // Sample
    public static final  String COLUMN_EVENT_INC_MIN = "incmin";
    public static final  String COLUMN_EVENT_INC_SEC = "incsec";
    public static final  String COLUMN_EVENT_DAYSONLY = "daysonly";
    public static final  String COLUMN_EVENT_BGIMAGE = "bgimage";
    public static final  String COLUMN_EVENT_UNITS = "timeunits";

    private static final String DEBUG_TAG = "DBH";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DEBUG_TAG, "in create");
        //System.out.println("!!- 1");
        String query = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_NAME + " TEXT, " +
                COLUMN_EVENT_INFO + " TEXT, " +
                COLUMN_EVENT_DIR + " INTEGER, " +
                COLUMN_EVENT_TIME + " INTEGER, " +
                COLUMN_EVENT_STATUS + " TEXT, " +
                COLUMN_EVENT_TYPE + " TEXT, " +
                COLUMN_EVENT_INC_MIN + " INTEGER, " +
                COLUMN_EVENT_INC_SEC + " INTEGER, " +
                COLUMN_EVENT_DAYSONLY + " INTEGER, " +
                COLUMN_EVENT_BGIMAGE + " TEXT, " +
                COLUMN_EVENT_UNITS + " TEXT " +
                ");";
        Log.i(DEBUG_TAG, "query is " + query);
        try{
            db.execSQL(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DEBUG_TAG, "Upgrading");
        //System.out.println("!!- 2");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);

    }

    //Add a new row to the database
    public void addEvent(Events event) {
        //System.out.println("!!- 3");
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.get_eventname());
        values.put(COLUMN_EVENT_INFO, event.get_eventinfo());
        values.put(COLUMN_EVENT_DIR, event.get_direction());
        values.put(COLUMN_EVENT_TIME, event.get_evtime());
        values.put(COLUMN_EVENT_STATUS, event.get_evstatus()); // (A)ctive/(I)nactive
        values.put(COLUMN_EVENT_TYPE, event.get_type()); // (R)eal/(S)ample
        values.put(COLUMN_EVENT_INC_MIN, event.get_incmin());
        values.put(COLUMN_EVENT_INC_SEC, event.get_incsec());
        values.put(COLUMN_EVENT_DAYSONLY, event.get_dayyears());
        values.put(COLUMN_EVENT_BGIMAGE, event.get_bgimage());
        values.put(COLUMN_EVENT_UNITS, event.get_timeunits());
        // add code here for new fields
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();

    }

    public String getEventIDs(String status) {
        String query;
        String dbString = "";
        SQLiteDatabase db = getReadableDatabase();
        if (status.equals("A")) { // get everything
            query = "SELECT _id FROM " + TABLE_EVENTS + " WHERE " + COLUMN_EVENT_STATUS + " = \"" + status + "\";";
        } else { // dont get samples for (I)nactive
            query = "SELECT _id FROM " + TABLE_EVENTS + " WHERE " + COLUMN_EVENT_STATUS +
                                    " = \"" + status + "\" AND " + COLUMN_EVENT_TYPE + " = \"R\";";

        }

        // System.out.println("!!- "  + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            //System.out.println("!!- "  + "in while " + c.getCount());
            if (c.getString(c.getColumnIndex("_id")) != null) {
                dbString += c.getString(c.getColumnIndex("_id"));
                dbString += ":"; // Delimiter between record IDs
                c.moveToNext();
            }
        }
        //System.out.println("!!- "  + dbString);
        db.close();
        return dbString;
    }

    // Getting a single event
    public  Events getMyEvent(int rowid) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, new String[] { COLUMN_ID,
                        COLUMN_EVENT_NAME,COLUMN_EVENT_INFO,COLUMN_EVENT_DIR,COLUMN_EVENT_TIME,
                        COLUMN_EVENT_STATUS, COLUMN_EVENT_TYPE,COLUMN_EVENT_INC_MIN,COLUMN_EVENT_INC_SEC,
                        COLUMN_EVENT_DAYSONLY,COLUMN_EVENT_BGIMAGE,COLUMN_EVENT_UNITS }, COLUMN_ID + "=?",
                new String[] { String.valueOf(rowid) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        //System.out.println("!!- "  + "checkbox is " + Integer.parseInt(cursor.getString(4)));
        db.close();
        return new Events(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5),
                cursor.getString(6),
                Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)),
                Integer.parseInt(cursor.getString(9)),
                cursor.getString(10)
         );
    }

    // Updating a single event
    public int updateEvent(Events myEvent) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EVENT_NAME, myEvent.get_eventname());
        cv.put(COLUMN_EVENT_INFO, myEvent.get_eventinfo());
        cv.put(COLUMN_EVENT_DIR, myEvent.get_direction());
        cv.put(COLUMN_EVENT_TIME, myEvent.get_evtime());
        cv.put(COLUMN_EVENT_TYPE, myEvent.get_type());
        cv.put(COLUMN_EVENT_INC_MIN, myEvent.get_incmin());
        cv.put(COLUMN_EVENT_INC_SEC, myEvent.get_incsec());
        cv.put(COLUMN_EVENT_DAYSONLY, myEvent.get_dayyears());
        cv.put(COLUMN_EVENT_BGIMAGE, myEvent.get_bgimage());
        cv.put(COLUMN_EVENT_UNITS, myEvent.get_timeunits());
        //System.out.println("!!- " + " days only is " + myEvent.get_dayyears());
        db.update(TABLE_EVENTS, cv, "_id = " + myEvent.get_id(), null);
        db.close();

        return 1; //needs a proper return code

    }

    //Mark event as inactive
    public boolean deleteEvent (int eventID) {

        boolean result = false;

        SQLiteDatabase db = getWritableDatabase();
        try {
            /* Changed so that record is now updated with status flag (I)nactive */
            //db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + COLUMN_ID + "=\"" + eventID + "\";");
            db.execSQL("UPDATE " + TABLE_EVENTS + " SET " + COLUMN_EVENT_STATUS + " = \"I\" WHERE " + COLUMN_ID + "=\"" + eventID + "\";");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        //System.out.println("!!-  result is " + result);
        return result;
    }

    //Delete all rows
    public boolean deleteAllEvents (String type) {

        boolean result = false;

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + COLUMN_EVENT_TYPE + " = \"" + type + "\";");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return result;
    }
    public boolean deleteSpecificEvents (String rowIDs) {

        boolean result = false;

        SQLiteDatabase db = getWritableDatabase();
        //System.out.println("!!- " + "rows to delete = " + rowIDs);
        try {
            db.execSQL("DELETE FROM " + TABLE_EVENTS +  " WHERE _id IN (" + rowIDs + ");");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return result;
    }

    public void manageSampleEvents(String status) {
        // Doesnt delete them. Just sets them to (A)ctive or (I)nactive.
        SQLiteDatabase db = getWritableDatabase();
        //System.out.println("!!- " + "rows to delete = " + rowIDs);
        try {
            db.execSQL("UPDATE " + TABLE_EVENTS +   " SET " + COLUMN_EVENT_STATUS + " = \"" + status + "\" WHERE " +
                                                                COLUMN_EVENT_TYPE + " = \"S\";");
            //result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        //return result;
    }

    public boolean restoreSpecificEvents (String rowIDs) {

        boolean result = false;

        SQLiteDatabase db = getWritableDatabase();
        //System.out.println("!!- " + "rows to delete = " + rowIDs);
        try {
            db.execSQL("UPDATE " + TABLE_EVENTS +  " SET " + COLUMN_EVENT_STATUS + " = \"A\" WHERE _ID IN (" + rowIDs + ");");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();
        return result;
    }

    // Get row count
    public int getRowCount(String status) {

        String whereClause;
        if (status .equals("ALL"))
            whereClause = "evstatus > \"\"";
        else
            whereClause = "evstatus=\"" + status + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_EVENTS, whereClause);

    }

}
