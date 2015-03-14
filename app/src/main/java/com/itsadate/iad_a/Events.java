package com.itsadate.iad_a;

public class Events {

    private int _id;
    private String _eventname;
    private int _direction;
    private int _evtime;
    private String _evstatus;
    private int _inchrs;
    private int _incmin;
    private int _incsec;
    private int _dayyears;

    // empty constructor
    public Events() {
    }
    // constructor
    public Events(int id, String eventname, int direction, int evtime, String evstatus,
                  int inchrs, int incmin, int incsec, int dayyears) {
        this._id = id;
        this._eventname = eventname;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._inchrs = inchrs;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
    }
    // constructor
    public Events(String eventname, int direction, int evtime, String evstatus,
                  int inchrs, int incmin, int incsec, int dayyears) {
        this._eventname = eventname;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._inchrs = inchrs;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
    }
/*
    public void set_id(int _id) {
        this._id = _id;
    }
*/
/*
    public void set_eventname(String _eventname) {
        this._eventname = _eventname;
    }
*/
    public int get_id() {
        return _id;
    }
    public String get_eventname() {
        return _eventname;
    }
    public int get_direction() {
        return _direction;
    }
    public int get_dayyears() {
        return _dayyears;
    }
    public int get_evtime() {
        return _evtime;
    }
    public String get_evstatus() {
        return _evstatus;
    }
    public int get_inchrs() {
        return _inchrs;
    }
    public int get_incmin() {
        return _incmin;
    }
    public int get_incsec() {
        return _incsec;
    }
}