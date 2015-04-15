package com.itsadate.iad_a;

public class Events {

    private int _id;
    private String _eventname;
    private String _eventinfo;
    private int _direction;
    private int _evtime;
    private String _evstatus;
    private int _inchrs;
    private int _incmin;
    private int _incsec;
    private int _dayyears;
    private String _bgimage;

    // empty constructor
    public Events() {
    }

    // constructor
    public Events(int id, String eventname) {
        this._id = id;
        this._eventname = eventname;
    }
    // constructor
    public Events(int id, String eventname, String eventinfo, int direction, int evtime, String evstatus,
                  int inchrs, int incmin, int incsec, int dayyears, String bgimage) {
        this._id = id;
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._inchrs = inchrs;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
    }
    // constructor
    public Events(String eventname, String eventinfo, int direction, int evtime, String evstatus,
                  int inchrs, int incmin, int incsec, int dayyears, String bgimage) {
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._inchrs = inchrs;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
    }

    public int get_id() {
        return _id;
    }
    public String get_eventname() {
        return _eventname;
    }
    public String get_eventinfo() {
        return _eventinfo;
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
    public String get_bgimage() {
        return _bgimage;
    }
    public void set_eventname(String _eventinfo) {
        this._eventinfo = _eventinfo;
    }
}