package com.itsadate.iad_a;

public class Events {

    private int _id;
    private String _eventname;
    private int _direction;
    private int _evtime;
    private String _evstatus;

    // empty constructor
    public Events() {
    }
    // constructor
    public Events(int id, String eventname, int direction, int evtime, String evstatus) {
        this._id = id;
        this._eventname = eventname;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
    }
    // constructor
    public Events(String eventname, int direction, int evtime, String evstatus) {
        this._eventname = eventname;
        this._direction = direction;
        this._evtime = evtime;
        this._evstatus = evstatus;
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
    public int get_evtime() {
        return _evtime;
    }
    public String get_evstatus() {
        return _evstatus;
    }
/*
    public int get_evusetime() {
        return _evusetime;
    }
*/
}