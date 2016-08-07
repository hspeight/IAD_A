package com.itsadate.iad_a;

public class Events {

    private int _id;
    private String _eventname;
    private String _eventinfo;
    private int _direction;
    private int _evtime;
    private String _evstatus;
    private String _evtype;
    private int _incmin;
    private int _incsec;
    private int _dayyears;
    private String _bgimage;
    private String _timeunits;

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
                  String evtype, int incmin, int incsec, int dayyears, String bgimage, String tunits) {
        this._id = id;
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._evtype = evtype;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
        this._timeunits = tunits;
    }
    // constructor
    public Events(String eventname, String eventinfo, int direction, int evtime, String evstatus,
                  String  evtype, int incmin, int incsec, int dayyears, String bgimage, String tunits) {
        this._eventname = eventname;
        this._eventinfo = eventinfo;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
        this._evtype = evtype;
        this._incmin = incmin;
        this._incsec = incsec;
        this._dayyears = dayyears;
        this._bgimage = bgimage;
        this._timeunits = tunits;

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
    public String get_type() {
        return _evtype;
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
    public String get_timeunits() {
        return _timeunits;
    }
    public void set_eventname(String _eventname) {
        this._eventname = _eventname;
    }
    public void set_eventinfo(String _eventinfo) {
        this._eventinfo = _eventinfo;
    }
    public void set_timeunits(String _timeunits) {
        this._timeunits = _timeunits;
    }
    public void set_evtype(String _evtype) {
        this._evtype = _evtype;
    }
    public void set_evstatus(String _evstatus) {
        this._evstatus = _evstatus;
    }
    public void set_evtime(int _evtime) {
        this._evtime = _evtime;
    }
    public void set_direction(int _direction) {
        this._direction = _direction;
    }
}